package de.haw_landshut.hawmobile.news;

import android.app.Notification;
import android.app.NotificationManager;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.Appointment;
import de.haw_landshut.hawmobile.base.AppointmentDao;
import de.haw_landshut.hawmobile.base.HAWDatabase;

public class AlarmReceiver extends BroadcastReceiver {
    public final String TAG = "AlarmReceiver";

    private AppointmentDao dao;
    private HAWDatabase database;
    private List<Appointment> appointments;

    private String title = "", text = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        database = Room.databaseBuilder(context.getApplicationContext(), HAWDatabase.class, "haw").build();
        dao = database.appointmentDao();

        new CheckAppointmentTask().execute(context);

    }

    private class CheckAppointmentTask extends AsyncTask<Context, Integer, Void> {
        private final String TAG = "CheckAppointmentTask";

        @Override
        protected Void doInBackground(Context... context) {
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DAY_OF_MONTH, 1);

            Log.d(TAG,"Load tomorrows appointments");
            appointments = dao.getAppointmentByDate(new SimpleDateFormat("dd.MM.yyyy").format(tomorrow.getTime()));

            if(appointments.size() == 0){
                Log.d(TAG, "No appointments found");
                return null;
            }

            Log.d(TAG, "Create Notifications");
            title = "HAWMobile Termine";
            text = "Erinnerung für den " + appointments.get(0).date + "\r\n";
            for (int i=0; i<appointments.size(); i++) {
                text += "\r\n" + appointments.get(i).appointment;
            }

            NotificationManager mNotificationManager = (NotificationManager) context[0].getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notif = new NotificationCompat.Builder(context[0].getApplicationContext(),"channel_0")
                    .setSmallIcon(R.drawable.announcement_icon)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setVibrate(new long[]{100,100,100,100})
                    .build();

            mNotificationManager.notify(1,notif);

            return null;
        }
    }

}