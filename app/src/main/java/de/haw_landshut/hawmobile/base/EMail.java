package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import javax.mail.*;
import java.io.IOException;
import java.util.Date;

@Entity
public class EMail {

    public EMail(){

    }

    public EMail(Message message, String foldername){
        try {

            this.setMsgnum(message.getMessageNumber());
            this.setAnswered(message.isSet(Flags.Flag.ANSWERED));
            this.setSeen(message.isSet(Flags.Flag.SEEN));
            this.setSubject(message.getSubject());
            this.setFoldername(foldername);
            this.setDate(message.getReceivedDate());
            this.setSenderMails(getFromAddresses(message.getFrom()));
            this.setBcc(getFromAddresses(message.getRecipients(Message.RecipientType.BCC)));
            this.setCc(getFromAddresses(message.getRecipients(Message.RecipientType.CC)));
            this.setFoldername(foldername);

        } catch (MessagingException m){
            m.printStackTrace();
        }
    }

    @PrimaryKey(autoGenerate = true)
    private int emkey;

    private int msgnum;

    private String subject, foldername, cc, bcc, senderMails;

    private Date date;

    private boolean seen, answered, isHtml;

    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    private String text;


    public int getMsgnum() {
        return msgnum;
    }

    public void setMsgnum(int msgnum) {
        this.msgnum = msgnum;
    }

    public int getEmkey() {
        return emkey;
    }

    public void setEmkey(int emkey) {
        this.emkey = emkey;
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

    public void setSenderMails(String senderMails) {
        this.senderMails = senderMails;
    }

    public String getFoldername() {
        return foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
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

    private static String getFromAddresses(Address[] addresses){
        StringBuilder stb = new StringBuilder();

        if(addresses == null)
            return null;

        for(Address a : addresses)
            stb.append(a.toString() + ", ");

        stb.deleteCharAt(stb.length()-1);

        return stb.toString();
    }

    private String getText(Message p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            isHtml = p.isMimeType("text/html");
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
