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
    private HAWDatabase database;
    private List<String> appointments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        setTitle(R.string.appointments);

        database = MainActivity.getHawDatabase();
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
                appointments.add(ap.date + "<br>" + ap.appointment);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            findViewById(R.id.appointment_loading_panel).setVisibility(View.GONE);
            final AppointmentArrayAdapter adapter = new AppointmentArrayAdapter(listView.getContext(), appointments);
            listView.setAdapter(adapter);
            listView.setPadding(30, 30, 30, 30);
        }
    }

    public class AppointmentArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final List<String> values;

        public AppointmentArrayAdapter(Context context, List<String> values) {
            super(context, R.layout.appointment_layout, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.appointment_layout, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.date);
            TextView textView2 = (TextView) rowView.findViewById(R.id.appointment);

            String s[] = values.get(position).split("<br>");

            Calendar calendar = Calendar.getInstance();
            String today = new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());
            if(s[0].equals(today)) {
                rowView.setBackgroundColor(Color.LTGRAY);
            }

            textView.setText(s[0]);
            textView2.setText(s[1].replaceAll("</br>", "\r\n"));

            return rowView;
        }
    }
}