package de.haw_landshut.hawmobile.mail;

import android.util.Log;
import com.sun.mail.imap.AppendUID;
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

        props.setProperty("mail.smtp.host", "asmtp.haw-landshut.de");
        props.setProperty("mail.smtp.port", "587"); //STARTTLS
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.starttls.required", "true");
        props.setProperty("mail.smtp.auth", "true");

    }

    private static Store store;
    private static Session session;

    public static void main(String... args){
        try {
            final Store store = Session.getDefaultInstance(props).getStore("imap");

            Scanner keyb = new Scanner(System.in);

            String username = keyb.nextLine();

            String password = keyb.nextLine();

            System.out.printf("username = %s,  password = %s\n", username, password);

            store.connect(username, password);

            for (Folder f : store.getDefaultFolder().list())
                System.out.println(f.getName());

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

//                dao.insertAllFolders(new EMailFolder(imapFolder.getName(), imapFolder.getUIDValidity(), imapFolder.getUIDNext()));

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

                //TODO: Auf Flag.SEEN überprüfen
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

    public static void markAsSeen(final EMailDao dao, final long uid, final String foldername){

        try{
            final Store store = Protocol.store;

            final EMail eMail = dao.getEmailFromUidAndFolder(uid, foldername);

            final IMAPFolder folder = ((IMAPFolder) store.getDefaultFolder().getFolder(foldername));
            if (!folder.isOpen())
                folder.open(Folder.READ_WRITE);

            folder.getMessageByUID(eMail.getUid()).setFlag(Flags.Flag.SEEN, true);

            folder.close();

            //schon in der MarkAsSeen getan.. eigentlich unnötig
            eMail.setSeen(true);

            dao.updateEMails(eMail);

        } catch (MessagingException e){
            e.printStackTrace();
        }

    }

    public static void markAsUnread(final EMailDao dao, final EMail... eMails){

        try{

            final Store store = Protocol.store;

            final IMAPFolder folder = ((IMAPFolder) store.getDefaultFolder().getFolder(eMails[0].getFoldername()));

            if (!folder.isOpen())
                folder.open(Folder.READ_WRITE);

            final int eMailsLength = eMails.length;
            final long uids[] = new long[eMailsLength];
            for (int i = 0; i < eMailsLength; i++) {
                EMail email = eMails[i];
                uids[i] = email.getUid();
            }

            final Message[] markasUnread = folder.getMessagesByUID(uids);

            for (int i = 0; i < eMailsLength; i++) {
                Message m = markasUnread[i];
                m.setFlag(Flags.Flag.SEEN, false);
                eMails[i].setSeen(false);
            }

            folder.close();

            dao.updateEMails(eMails);
        } catch (MessagingException e){
            e.printStackTrace();
        }
    }

    public static void deleteOrMoveMessages(final EMailDao dao, final String dstFoldername, final EMail... eMails){
        try{
            final Store store = Protocol.store;

            final IMAPFolder srcFolder = ((IMAPFolder) store.getFolder(eMails[0].getFoldername()));
            final IMAPFolder dstFolder;

            if(!srcFolder.isOpen())
                srcFolder.open(Folder.READ_WRITE);

            if(dstFoldername == null) {
                dstFolder = null;
            } else {
                dstFolder = ((IMAPFolder) store.getFolder(dstFoldername));
                if(!dstFolder.isOpen())
                    dstFolder.open(Folder.READ_WRITE);
            }

            final int eMailsLenght = eMails.length;
            final long[] uids = new long[eMailsLenght];
            for (int i = 0; i < eMailsLenght; i++) {
                uids[i] = eMails[i].getUid();
            }

            final Message[] messages = srcFolder.getMessagesByUID(uids);

            if(dstFolder != null) {
                //Wenn Ordner angegeben verschiebe alle übergebenen Nachrichten nach
                final AppendUID[] newuids = srcFolder.moveUIDMessages(messages, dstFolder);
                final String oldFolderName = srcFolder.getName();
                for (int i = 0; i < eMailsLenght; i++) {
                    dao.moveEMailToNewFolder(dstFoldername, oldFolderName, uids[i], newuids[i].uid);
                }
            } else {
                //Wenn kein Ordner angegeben, markiere Ordner als gelöscht
                for (int i = 0; i < eMailsLenght; i++) {
                    messages[i].setFlag(Flags.Flag.DELETED, true);
                    dao.deleteEMail(eMails[i]);
                }
            }
            srcFolder.close(true);

            if(dstFolder != null)
                dstFolder.close(false);

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
        if (store == null || !store.isConnected()) {
            Protocol.store = getSession().getStore("imap");
            Protocol.store.connect(Credentials.getUsername(), Credentials.getPassword());
            Log.i("Protocol", "logged in as user " + Credentials.getUsername());
        }
    }

    public static void logout() throws MessagingException{
        if (Protocol.store != null) {
            Protocol.store.close();
            store = null;
            Log.i("Protocol", "logged out");
        }
    }

    public static Store getStore(){
        try{
            if(store == null || !store.isConnected()) {
                Protocol.store = Session.getDefaultInstance(props).getStore("imap");
                Protocol.store.connect(Credentials.getUsername(), Credentials.getPassword());
                Log.i("Protocol", "logged in as user " + Credentials.getUsername());
            }
                return store;
        } catch (MessagingException e){
            e.printStackTrace();
            Log.e("Protocol", "Connection Problems");
        }
        return null;
    }

    public static Session getSession(){
        if (session == null)
            Protocol.session = Session.getDefaultInstance(props);
        return Protocol.session;
    }

}
