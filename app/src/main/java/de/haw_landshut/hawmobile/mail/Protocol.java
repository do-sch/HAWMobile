package de.haw_landshut.hawmobile.mail;

import android.os.AsyncTask;
import android.util.Log;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.event.FolderEvent;
import javax.mail.event.FolderListener;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
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

    public static void main(String... args){
        try {
            final Store store = Session.getDefaultInstance(props).getStore("imap");

            Scanner keyb = new Scanner(System.in);

            String username = keyb.nextLine();

            String password = keyb.nextLine();

            System.out.printf("username = %s,  password = %s\n", username, password);

            store.connect(username, password);

            //Alle Ordner
            Folder[] folders = store.getDefaultFolder().list();
            for (Folder f : folders)
                System.out.println(f.getName());


            Folder f = store.getFolder("INBOX");

            int newMessages = f.getNewMessageCount();


        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    public static boolean tryConnect(){
        class TestAccount extends AsyncTask<String, Void, Boolean>{
            @Override
            protected Boolean doInBackground(String... strings) {
                try {
                    final Store store = Session.getDefaultInstance(props).getStore("imap");

                    Log.d("strings[0]", strings[0]);
                    Log.d("strings[1]", strings[1]);

                    store.connect(strings[0], strings[1]);



                    Log.d("count personal namesp", store.getPersonalNamespaces().length+"");
                    for (Folder folder : store.getPersonalNamespaces()) {
                        Log.d("personal namespace", folder.getFullName());
                    }

                    for (Folder folder : store.getSharedNamespaces()){
                        Log.d("shared namespace", folder.getFullName());
                    }


                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    public static boolean receiveEmails(String username, String password){

//        class TestAccount extends AsyncTask<String, Void, Boolean>{
//            @Override
//            protected Boolean doInBackground(String... strings) {
//                try {
//                    final Store store = Session.getDefaultInstance(props).getStore("imap");
//
//                    Log.d("strings[0]", strings[0]);
//                    Log.d("strings[1]", strings[1]);
//
//                    store.connect(strings[0], strings[1]);
//
//
//                    Log.d("count personal namesp", store.getPersonalNamespaces().length+"");
//                    for (Folder folder : store.getPersonalNamespaces()) {
//                        Log.d("personal namespace", folder.getFullName());
//                    }
//
//                    for (Folder folder : store.getSharedNamespaces()){
//                        Log.d("shared namespace", folder.getFullName());
//                    }
//
//
//                } catch (MessagingException e) {
//                    e.printStackTrace();
//                }
//                return true;
//            }
//        }
//
//        new TestAccount().execute(username, password);

        return true;
    }



}
