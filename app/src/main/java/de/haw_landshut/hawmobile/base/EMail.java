package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;
import android.util.Log;

import javax.activation.MimeType;
import javax.mail.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.ParameterList;
import java.io.IOException;
import java.util.Date;

@Entity(primaryKeys = {"uid", "foldername"})
public class EMail {

    public EMail(){

    }

    public EMail(Message message, long uid, String foldername){
        try {

            this.setUid(uid);
            this.setAnswered(message.isSet(Flags.Flag.ANSWERED));
            this.setSeen(message.isSet(Flags.Flag.SEEN));
            this.setSubject(message.getSubject());
            this.setFoldername(foldername);
            this.setDate(message.getReceivedDate());
            this.setSenderMails(getFromAddresses(message.getFrom()));
            this.setBcc(addresses2strings(message.getRecipients(Message.RecipientType.BCC)));
            this.setCc(addresses2strings(message.getRecipients(Message.RecipientType.CC)));
            this.setFoldername(foldername);
            this.setText(getText(message));

        } catch (MessagingException | IOException e){
            e.printStackTrace();
        }
    }

    private long uid;

    @NonNull
    private String foldername;

    private String encoding;

    private String subject, senderMails;

    private String[] cc, bcc;

    private Date date;

    private boolean seen, answered, isHtml;

    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    private String text;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSenderMails() {
        return senderMails;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    public void setSenderMails(String senderMails) {
        this.senderMails = senderMails;
    }

    public String getFoldername() {
        return foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean html) {
        isHtml = html;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    private static String[] addresses2strings(Address[] addresses){
        if(addresses == null || addresses.length == 0)
            return null;

        final int addressesLength = addresses.length;
        final String[] strings = new String[addresses.length];
        for (int i = 0; i < addressesLength; i++) {
            Address address = addresses[i];
            final InternetAddress internetAddress = ((InternetAddress) address);

            strings[i] = internetAddress.getPersonal() == null ? ((InternetAddress) address).getAddress() : internetAddress.getPersonal()+" ("+internetAddress.getAddress()+")";
        }
        return strings;
    }

    private static String getFromAddresses(Address[] addresses){

        for(Address address : addresses){
            InternetAddress internetAddress = ((InternetAddress) address);
            final String name = internetAddress.getPersonal();
            if (name == null)
                return internetAddress.getAddress();
            return name;
        }

        return null;
    }

    private String getText(Message p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            isHtml = p.isMimeType("text/html");
            encoding = p.getContentType();
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }

    private String getText(Part p) throws MessagingException, IOException{
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            isHtml = p.isMimeType("text/html");
//            Log.d("getTextP", new ContentType(p.getContentType()).getParameter("charset"));
            encoding = p.getContentType();
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }
}
