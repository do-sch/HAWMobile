package de.haw_landshut.hawmobile.news;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.haw_landshut.hawmobile.MainActivity;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.Appointment;
import de.haw_landshut.hawmobile.base.AppointmentDao;
import de.haw_landshut.hawmobile.base.HAWDatabase;

public class AppointmentActivity extends AppCompatActivity {
    private ListView listView;

    private AppointmentDao dao;
    private ArrayList<Appointment> appointments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        setTitle(R.string.appointments);

        HAWDatabase database = MainActivity.getHawDatabase();
        if(database != null)
            dao = database.appointmentDao();

        listView = findViewById(R.id.appointment_list_view);
        new loadAppointments().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class loadAppointments extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (Appointment ap: dao.getAllAppointments()
                 ) {
                appointments.add(ap);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            findViewById(R.id.appointment_loading_panel).setVisibility(View.GONE);
            final AppointmentArrayAdapter adapter = new AppointmentArrayAdapter(listView.getContext(), appointments);
            listView.setAdapter(adapter);
        }
    }

    public class AppointmentArrayAdapter extends ArrayAdapter<Appointment> {
        private final Context context;
        private final ArrayList<Appointment> values;

        public AppointmentArrayAdapter(Context context, ArrayList<Appointment> values) {
            super(context, R.layout.appointment_layout, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.appointment_layout, parent, false);
            TextView date1 = rowView.findViewById(R.id.date);
            TextView date2 = rowView.findViewById(R.id.date2);
            TextView dateFromTill = rowView.findViewById(R.id.date_from_till);
            TextView appointment = rowView.findViewById(R.id.appointment);
            ImageView alertIcon = rowView.findViewById(R.id.date_alert_icon);

            Calendar calendar = Calendar.getInstance();
            int today = LoadAppointmentsTask.dateAsInt(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            int tomorrow = LoadAppointmentsTask.dateAsInt(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
            if(tomorrow >= values.get(position).start && today <= values.get(position).end)
                alertIcon.setVisibility(View.VISIBLE);
            else
                alertIcon.setVisibility(View.INVISIBLE);

            date1.setText(LoadAppointmentsTask.dateAsString(values.get(position).start));

            if(values.get(position).start != values.get(position).end)
                date2.setText(LoadAppointmentsTask.dateAsString(values.get(position).end));
            else
                dateFromTill.setVisibility(View.INVISIBLE);

            appointment.setText(values.get(position).appointment);

            return rowView;
        }
    }
}