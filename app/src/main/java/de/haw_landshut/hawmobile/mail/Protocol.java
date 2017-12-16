package de.haw_landshut.hawmobile.mail;

import android.os.AsyncTask;
import android.util.Log;
import com.sun.mail.imap.IMAPFolder;
import de.haw_landshut.hawmobile.Credentials;
import de.haw_landshut.hawmobile.base.Contact;
import de.haw_landshut.hawmobile.base.EMail;
import de.haw_landshut.hawmobile.base.EMailDao;
import de.haw_landshut.hawmobile.base.EMailFolder;

import javax.mail.*;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Protocol {

    //Folder-Names: INBOX, Drafts, Sent, Junk, Trash
    public static final Properties props = new Properties();
    static{
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imap.host", "mail.haw-landshut.de");//STARTTLS
        props.setProperty("mail.imap.port", "143");
        props.setProperty("mail.imap.starttls.enable", "true");
        props.setProperty("mail.imap.starttls.required", "true");

//        props.setProperty("mail.smtp.host", "asmtp.haw-landshut.de");
//        props.setProperty("mail.smtp.port", "587"); //STARTTLS
//        props.setProperty("mail.smtp.starttls.enable", "true");
//        props.setProperty("mail.smtp.starttls.required", "true");
//        props.setProperty("mail.smtp.auth", "true");

    }

    private static Store store;

    public static void main(String... args){
        try {
            final Store store = Session.getDefaultInstance(props).getStore("imap");

            Scanner keyb = new Scanner(System.in);

            String username = keyb.nextLine();

            String password = keyb.nextLine();

            System.out.printf("username = %s,  password = %s\n", username, password);

            store.connect(username, password);

            IMAPFolder folder = ((IMAPFolder) store.getDefaultFolder().getFolder("Trash"));

            if(!folder.isOpen())
                folder.open(Folder.READ_ONLY);

            Message[] messages = folder.getMessages();

            for(Message m : messages){
                System.out.println(m.getContentType());

            }


            store.close();


        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void loadAllMessagesAndFolders(final EMailDao dao, final MailOverview.Mail2BaseTask asyncTask /*, final MailEntryAdapter adapter*/){
        try {
            final Store store = Protocol.store;
            final Folder[] list = store.getDefaultFolder().list();
            final int listLength = list.length;
            int messageCount = 0, messageIndex = 0;

            for (Folder folder : list){
                if (!folder.isOpen())
                    folder.open(Folder.READ_ONLY);
                messageCount += folder.getMessageCount();
            }

            for (final Folder folder : list) {
                IMAPFolder imapFolder = ((IMAPFolder) folder);

                dao.insertAllFolders(new EMailFolder(imapFolder.getName(), imapFolder.getUIDValidity(), imapFolder.getUIDNext()));

                Message[] messages = imapFolder.getMessages();
                for (int mindex = 0, messagesLength = messages.length; mindex < messagesLength; mindex++, messageIndex++) {

                    Message m = messages[mindex];
                    //insert Addresses
                    insertAddresses(dao, m.getFrom());
                    final EMail em = new EMail(m, imapFolder.getUID(m), imapFolder.getName());

                    asyncTask.tellProgress(messageIndex, messageCount);

                    dao.insertAllEMails(em);
                }

                imapFolder.close(false);

            }
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }

    public static void updateAllFolders(EMailDao dao){
        try {
            final Store store = Protocol.store;

            final Folder[] list = store.getDefaultFolder().list();
            for (Folder folder : list) {

                //Öffne Ordner wenn nicht offen
                if (!folder.isOpen())
                    folder.open(Folder.READ_ONLY);

                final IMAPFolder imapFolder = ((IMAPFolder) folder);


                final long oldNextuid = dao.getFolderNextuid(imapFolder.getName());
                final long newNextuid = imapFolder.getUIDNext();
                if(newNextuid != oldNextuid) {
                    //Wenn UIDs eines Ordners nicht mehr gültig
                    final long validaty = imapFolder.getUIDValidity();
                    if (dao.getFolderUIDValidaty(imapFolder.getName()) != validaty) {
                        //Lösche Alle Emails des Ordners und lade sie neu in die Datenbank
                        dao.deleteAllEMailsFromFolder(imapFolder.getName());
                        for (int i = 0; i < imapFolder.getMessages().length; i++) {
                            final Message m = imapFolder.getMessage(i + 1);
                            dao.insertAllEMails(new EMail(m, imapFolder.getUID(m), folder.getName()));
                            insertAddresses(dao, m.getFrom());
                        }
                    } else {
                        //get new Messages
                        final Message[] newMessages = imapFolder.getMessagesByUID(oldNextuid-1, newNextuid-1);
                        final EMail[] newEmails = new EMail[newMessages.length];
                        for (int i = 0; i < newMessages.length; i++) {
                            final Message m = newMessages[i];
                            newEmails[i] = new EMail(m, imapFolder.getUID(m), imapFolder.getName());
                            insertAddresses(dao, m.getFrom());
                        }
                        dao.insertAllEMails(newEmails);

                        //TODO: was passiert, wenn eine Message gelöscht und eine neue anstattdessen hierher verschoben wird
                        //check for deleted Messages
                        if (imapFolder.getMessageCount() != dao.getMessageCountInFolder(imapFolder.getName())){
                            final List<EMail> oldMails = dao.getAllEmailsFromFolder(imapFolder.getName(), dao.getFolderNextuid(imapFolder.getName()) - 1);
                            for (EMail m : oldMails){
                                if (imapFolder.getMessageByUID(m.getUid()) == null)
                                    dao.deleteEMail(m);
                            }
                        }

                    }
                }

                imapFolder.close(true);

            }

        }catch (MessagingException e){
            e.printStackTrace();
        }
    }

    private static void insertAddresses(final EMailDao dao, final Address[] addresses){
        for(final Address a : addresses){
            dao.insertContacts(new Contact(a));
        }
    }

    public static void login() throws MessagingException{
        if(store == null || !store.isConnected()) {
            Protocol.store = Session.getDefaultInstance(props).getStore("imap");
            Protocol.store.connect(Credentials.getUsername(), Credentials.getPassword());
            Log.i("Protocol", "logged in as user " + Credentials.getUsername());
        }
    }

    public static void logout() throws MessagingException{
        Protocol.store.close();
        store = null;
        Log.i("Protocol", "logged out");
    }









}
