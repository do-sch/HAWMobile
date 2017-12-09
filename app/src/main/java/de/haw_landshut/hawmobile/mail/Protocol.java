package de.haw_landshut.hawmobile.mail;

import android.util.Log;
import com.sun.mail.imap.DefaultFolder;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPNestedMessage;
import com.sun.mail.pop3.POP3Folder;
import de.haw_landshut.hawmobile.Credentials;
import de.haw_landshut.hawmobile.base.EMail;
import de.haw_landshut.hawmobile.base.EMailDao;
import de.haw_landshut.hawmobile.base.EMailFolder;

import javax.mail.*;
import java.io.IOException;
import java.util.*;

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

            System.out.println("Next:" + folder.getUIDNext());
            System.out.println(":::::::::::::::::::::::::::::::::::");

            for (final Message m : folder.getMessages()){
                System.out.println("UID: " + folder.getUID(m));
                System.out.println("Subject: " + m.getSubject());
                System.out.println();
            }


            store.close();


        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void loadAllMessagesAndFolders(final EMailDao dao){
        try {
            final Store store = Protocol.store;
            final List<EMail> mails = new ArrayList<>();
            final EMailFolder[] eMailFolders;
            final Folder[] list = store.getDefaultFolder().list();
            final int listLength = list.length;

            eMailFolders = new EMailFolder[listLength];
            for (int i = 0; i < listLength; i++) {
                final Folder folder = list[i];
                if (!folder.isOpen())
                    folder.open(Folder.READ_ONLY);

                IMAPFolder imapFolder = ((IMAPFolder) folder);
                eMailFolders[i] = new EMailFolder(imapFolder.getName(), imapFolder.getUIDValidity(), imapFolder.getUIDNext());

                for (final Message m : imapFolder.getMessages()) {
                    final EMail em = new EMail(m, imapFolder.getUID(m), imapFolder.getName());

                    mails.add(em);

                }

                imapFolder.close();

            }

            dao.insertAllFolders(eMailFolders);
            dao.insertAllEMails(mails.toArray(new EMail[mails.size()]));
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }

    public static void updateAllFolders(EMailDao eMailDao){
        try {
            final Store store = Protocol.store;
            final List<EMail> mails = new ArrayList<>();

            final Folder[] list = store.getDefaultFolder().list();
            for (Folder folder : list) {

                //Öffne Ordner wenn nicht offen
                if (!folder.isOpen())
                    folder.open(Folder.READ_ONLY);

                final IMAPFolder imapFolder = ((IMAPFolder) folder);


                final long oldNextuid = eMailDao.getFolderNextuid(imapFolder.getName());
                final long newNextuid = imapFolder.getUIDNext();
                if(newNextuid != oldNextuid) {
                    //Wenn UIDs eines Ordners nicht mehr gültig
                    final long validaty = imapFolder.getUIDValidity();
                    if (eMailDao.getFolderUIDValidaty(imapFolder.getName()) != validaty) {
                        //Lösche Alle Emails des Ordners und lade sie neu in die Datenbank
                        eMailDao.deleteAllEMailsFromFolder(imapFolder.getName());
                        final EMail[] folderMails = new EMail[imapFolder.getMessageCount()];
                        for (int i = 0; i < imapFolder.getMessages().length; i++) {
                            final Message m = imapFolder.getMessage(i + 1);
                            folderMails[i] = new EMail(m, imapFolder.getUID(m), folder.getName());
                        }
                        eMailDao.insertAllEMails(folderMails);
                    } else {
                        //get new Messages
                        final Message[] newMessages = imapFolder.getMessagesByUID(oldNextuid-1, newNextuid-1);
                        final EMail[] newEmails = new EMail[newMessages.length];
                        for (int i = 0; i < newMessages.length; i++) {
                            final Message m = newMessages[i];
                            newEmails[i] = new EMail(m, imapFolder.getUID(m), imapFolder.getName());
                        }
                        eMailDao.insertAllEMails(newEmails);

                        //check for deleted Messages
                        if (imapFolder.getMessageCount() != eMailDao.getMessageCountInFolder(imapFolder.getName())){
                            final List<EMail> oldMails = eMailDao.getAllEmailsFromFolder(imapFolder.getName(), eMailDao.getFolderNextuid(imapFolder.getName()) - 1);
                            for (EMail m : oldMails){
                                if (imapFolder.getMessageByUID(m.getUid()) == null)
                                    eMailDao.deleteEMail(m);
                            }
                        }

                    }
                }

                imapFolder.close();

            }

        }catch (MessagingException e){
            e.printStackTrace();
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
