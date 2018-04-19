package de.haw_landshut.hawmobile.news;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import de.haw_landshut.hawmobile.MainActivity;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.Appointment;
import de.haw_landshut.hawmobile.base.AppointmentDao;
import de.haw_landshut.hawmobile.base.HAWDatabase;

public class AlarmReceiver extends BroadcastReceiver {
    public final String TAG = "AlarmReceiver";

    private AppointmentDao dao;
    private HAWDatabase database;
    private List<Appointment> appointments;
    private SharedPreferences sharedPref;

    private String title = "", betreff = "", nachricht = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        database = MainActivity.getHawDatabase();
        //database = Room.databaseBuilder(context.getApplicationContext(), HAWDatabase.class, "haw").build();
        dao = database.appointmentDao();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        new CheckAppointmentTask().execute(context);
    }

    private class CheckAppointmentTask extends AsyncTask<Context, Integer, Void> {
        private final String TAG = "CheckAppointmentTask";

        @Override
        protected Void doInBackground(Context... context) {
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DAY_OF_MONTH, 1);

            Log.d(TAG, "Load tomorrows appointments: " + SimpleDateFormat.getDateInstance().format(tomorrow.getTime()));
            appointments = dao.getAppointmentByStartDate(LoadAppointmentsTask.dateAsInt(SimpleDateFormat.getDateInstance().format(tomorrow.getTime())));

            if (appointments.size() == 0) {
                Log.d(TAG, "No appointments found");
                return null;
            }

            Log.d(TAG, "Create Notifications");
            title = "HAWMobile Termine";
            betreff = "Erinnerung für den " + LoadAppointmentsTask.dateAsString(appointments.get(0).start) + "\r\n";

            StringBuilder tmp = new StringBuilder("");
            for (int i = 0; i < appointments.size(); i++) {
                tmp.append("\r\n").append(appointments.get(i).appointment);
            }
            nachricht = tmp.toString();

            NotificationManager mNotificationManager = (NotificationManager) context[0].getSystemService(Context.NOTIFICATION_SERVICE);

            boolean prefVibrate = sharedPref.getBoolean("pref_switch_vibrate", false);
            long vibrationTime = prefVibrate ? 100 : 0;

            // Create an Intent for the activity you want to start
            Intent resultIntent = new Intent(context[0], AppointmentActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context[0]);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notif = new NotificationCompat.Builder(context[0].getApplicationContext(), "channel_0")
                    .setSmallIcon(R.drawable.announcement_icon)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .setContentText(betreff)
                    .setVibrate(new long[]{vibrationTime, vibrationTime, vibrationTime, vibrationTime})
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(betreff + nachricht))
                    .build();


            mNotificationManager.notify(1, notif);

            return null;
        }
    }

}