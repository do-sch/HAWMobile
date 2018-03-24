package de.haw_landshut.hawmobile.mail;

import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.sun.mail.imap.IMAPFolder;
import de.haw_landshut.hawmobile.Credentials;
import de.haw_landshut.hawmobile.MainActivity;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.Contact;
import de.haw_landshut.hawmobile.base.EMail;
import de.haw_landshut.hawmobile.base.EMailDao;
import de.haw_landshut.hawmobile.base.HAWDatabase;

import javax.mail.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MailService extends Job {
    public static final String TAG = "haw_landshut.hawmobile.mail.lookForMails";
    private int i = 0;

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {

        //da die Klasse Credentials beim Beenden der App bereinigt wird, werden die Anmeldedaten der App null
        //mit loadCredentilasFromAccountManager werden sie wieder gefüllt
        if (Credentials.getUsername() == null)
            Credentials.loadCredentialsFromAccountManager(getContext());

        if (Credentials.getUsername() == null)
            return Result.FAILURE;

        final PendingIntent pi = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), MainActivity.class), 0);

        final HAWDatabase hawDatabase = Room.databaseBuilder(getContext(), HAWDatabase.class, "haw").build();
        final Store store = Protocol.getStore();
        final EMailDao dao = hawDatabase.eMailDao();

        try {

            final IMAPFolder imapFolder = ((IMAPFolder) store.getDefaultFolder().getFolder(MailOverview.INBOX));

            //Öffne Ordner wenn nicht offen
            if (!imapFolder.isOpen())
                imapFolder.open(Folder.READ_ONLY);

            final long oldNextuid = dao.getFolderNextuid(MailOverview.INBOX);
            final long newNextuid = imapFolder.getUIDNext();
            final long oldUidvalidaty = dao.getFolderUIDValidaty(MailOverview.INBOX);
            final long newUidvalidaty = imapFolder.getUIDValidity();

            if (oldUidvalidaty != newUidvalidaty) {
                //TODO: alles neu fetchen
            }

            if (newNextuid != oldNextuid) {

                final Message[] newMessages = imapFolder.getMessagesByUID(oldNextuid, newNextuid-1);

                if (newMessages.length != 0) {
                    //iteriere durch alle Nachrichten die neu erfasst wurden
                    for (final Message message : newMessages) {
                        final EMail mail = new EMail(message, imapFolder.getUID(message), MailOverview.INBOX);
                        final boolean seen = message.getFlags().contains(Flags.Flag.SEEN);


                        dao.insertAllEMails(mail);
                        dao.deleteLowestUIDMailsFromFolder(MailOverview.INBOX, MailOverview.MESSAGESAVECOUNT);

                        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());


                        if (!seen) {//TODO: vibration, sound

                            if (prefs.getBoolean("notifications_new_message", true)) {

                                //TODO: set Mail and Intent
                                final Intent notificationIntent = new Intent(getContext(), MailView.class);


                                final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext())
                                        .setContentTitle(mail.getSenderMail())
                                        .setContentText(mail.getSubject())
                                        .setAutoCancel(true)
                                        .setContentIntent(pi)
                                        .setSmallIcon(R.drawable.mail_icon)
                                        .setShowWhen(true)
                                        .setColor(Color.RED)
                                        .setLocalOnly(true);


                                final String soundUri = prefs.getString("notifications_new_message_ringtone", "");
                                if (!soundUri.isEmpty())
                                    notificationBuilder.setSound(Uri.parse(soundUri));

                                if (prefs.getBoolean("notifications_new_message_vibrate", true))
                                    notificationBuilder.setVibrate(new long[] {1000});


                                NotificationManagerCompat.from(getContext())
                                        .notify(new Random().nextInt(), notificationBuilder.build());
                            }
                        }
                    }

                    if (MailOverview.instance != null) {
                        Log.d("MailService", "updateAdapter");
                        MailOverview.instance.updateAdapter();
                    }
                }

                dao.updateFolderStuff(MailOverview.INBOX, newNextuid, newUidvalidaty);

                //aktuallisiert MailOverview, wenn offen

            }

            imapFolder.close(true);

            store.close();

        } catch (MessagingException e){
            e.printStackTrace();
            return Result.FAILURE;
        } finally {
            hawDatabase.close();
        }

        return Result.SUCCESS;

    }

    private static void insertAddresses(final EMailDao dao, final Address[] addresses){
        for(final Address a : addresses){
            dao.insertContacts(new Contact(a));
        }
    }

    static void schedulePeriodic(final SharedPreferences prefs) {
//        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final int syncfrequency = Integer.valueOf(prefs.getString("sync_frequency", "-1"));

        System.out.println("syncfrequency = " + syncfrequency);

        if (syncfrequency == -1)
            return;

        int id = new JobRequest.Builder(MailService.TAG)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setPeriodic(TimeUnit.MINUTES.toMillis(syncfrequency))
                .setUpdateCurrent(true)
//                .startNow()
                .build()
                .schedule();

        Log.d("MailService", "Started Job with id " + id + " with Periode " + syncfrequency);
    }
}
