package de.haw_landshut.hawmobile.mail;

import android.util.Log;
import de.haw_landshut.hawmobile.Credentials;
import de.haw_landshut.hawmobile.base.EMail;

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

    public static void main(String... args){
        try {
            final Store store = Session.getDefaultInstance(props).getStore("imap");

            Scanner keyb = new Scanner(System.in);

            String username = keyb.nextLine();

            String password = keyb.nextLine();

            System.out.printf("username = %s,  password = %s\n", username, password);

            store.connect(username, password);

            for (Folder f : store.getDefaultFolder().list()){
                System.out.println(f.getName());
            }

            store.close();


        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

//    public static Map<String, MailEntry[]> fetchAllMessages(){
//
//        try {
//            final Store store = login();
//            final Map<String, MailEntry[]> map = new HashMap<>();
//
//            for(Folder f : store.getDefaultFolder().list()){
//                if(!f.isOpen())
//                    f.open(Folder.READ_ONLY);
//                map.put(f.getName(), MailEntry.getEntriesFromMessages(f.getMessages()));
//                f.close();
//            }
//
//            store.close();
//
//            return map;
//
//        } catch (MessagingException e){
//            e.printStackTrace();//TODO: Fehlermeldung
//        }
//
//        return null;
//    }

    public static EMail[] loadAllMessages(){
        try {
            final Store store = login();
            final List<EMail> mails = new ArrayList<>();

            Folder[] list = store.getDefaultFolder().list();
            for (Folder f : list) {
                if (!f.isOpen())
                    f.open(Folder.READ_ONLY);

                for (final Message m : f.getMessages()) {
                    final EMail em = new EMail(m, f.getName());

                    mails.add(em);

                }

                f.close();

            }

            return mails.toArray(new EMail[mails.size()]);
        }catch (MessagingException e){
            e.printStackTrace();
        }
        return null;
    }

    private static Store login() throws MessagingException{
        final Store store = Session.getDefaultInstance(props).getStore("imap");

        store.connect(Credentials.getUsername(), Credentials.getPassword());

        return store;
    }

    private static String addr2str(Address[] addresses){

        StringBuilder stringBuilder = new StringBuilder();
        for (Address a : addresses)
            stringBuilder.append(", ").append(a.toString());
        stringBuilder.delete(0, 2);

        return stringBuilder.toString();

    }






}
