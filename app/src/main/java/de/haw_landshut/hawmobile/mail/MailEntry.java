package de.haw_landshut.hawmobile.mail;

import android.util.Log;
import de.haw_landshut.hawmobile.base.EMail;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MailEntry {

    private String subject, sender, content, date;

    //TODO: MailEntry anstelle von EMail verwenden => sorgt für mehr performance
    public MailEntry(String subject, String sender, String content, String date){
        this.subject = subject;
        this.sender = sender;
        this.content = content;
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public static MailEntry[] getEntriesFromBase(List<EMail> messages){

        final MailEntry[] entries = new MailEntry[messages.size()];

        for (int i = 0; i < messages.size(); i++){

            final EMail m = messages.get(i);

            final DateFormat df = new SimpleDateFormat("dd.MM.yy");
            entries[i] = new MailEntry(m.getSubject(), m.getSenderMails(), m.getText(), df.format(m.getDate()));

        }

        return entries;
    }

//    public static MailEntry[] getEntriesFromMessages(Message[] messages){
//
//        final MailEntry[] entries = new MailEntry[messages.length];
//
//        for (int i = 0; i < messages.length; i++){
//
//            Message m = messages[i];
//            String subject = "Subject";
//            String date = "01.01.2017";
//            String sender = "Sender";
//            String content = "This is some of the Content of an E-Mail";
//
//            try{
//                subject = m.getSubject();
//                Date mdate = m.getReceivedDate();
//                date = mdate.getDate()+"."+mdate.getMonth()+"."+mdate.getYear();
//                StringBuilder s = new StringBuilder();
//                for (Address a : m.getFrom())
//                    s.append(a.toString());
//                sender = s.toString();
//                content = m.getContentType().equals("Text") ? ((String) m.getContent()).substring(0, 40) : "Other Message";
//
//            } catch (MessagingException | IOException e){
//                e.printStackTrace();
//            }
//
//            Log.d("entry", subject);
//
//            entries[i] = new MailEntry(subject, sender, content, date);
//
//        }
//
//        return entries;
//
//    }
}
