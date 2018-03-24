package de.haw_landshut.hawmobile.news;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.*;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import de.haw_landshut.hawmobile.*;
import de.haw_landshut.hawmobile.base.Appointment;
import de.haw_landshut.hawmobile.base.AppointmentDao;
import de.haw_landshut.hawmobile.base.HAWDatabase;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsOverview.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsOverview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsOverview extends Fragment {
    //Termine Variablen
    private AppointmentDao dao;

    private SharedPreferences sharedPref;
    //Termine Ende

    private String faculty;
    private ListView listView;
    private OnFragmentInteractionListener mListener;

    public NewsOverview() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewsOverview.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsOverview newInstance() {
        NewsOverview fragment;
        fragment = new NewsOverview();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HAWDatabase database = ((MainActivity) getActivity()).getDatabase();
        dao = database.appointmentDao();

        //Termine
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPref.registerOnSharedPreferenceChangeListener(prefListener);

        new LoadAppointmentsTask().execute();
        //Termine ende
        String prefFaculty = sharedPref.getString("pref_faculty", "IF");

        setFaculty(prefFaculty);
    }
    void setFaculty(String prefFaculty){
        if(getActivity()!=null){
            switch (prefFaculty) {
                case "BW":
                    faculty = "betriebswirtschaft";
                    getActivity().setTitle(R.string.news_bw);
                    break;
                case "EW":
                    faculty = "elektrotechnik-und-wirtschaftsingenieurwesen";
                    getActivity().setTitle(R.string.news_ew);
                    break;
                case "IF":
                    faculty = "informatik";
                    getActivity().setTitle(R.string.news_if);
                    break;
                case "IS":
                    faculty = "interdisziplinaere-studien";
                    getActivity().setTitle(R.string.news_ids);
                    break;
                case "MA":
                    faculty = "maschinenbau";
                    getActivity().setTitle(R.string.news_ma);
                    break;
                case "SA":
                    faculty = "soziale-arbeit";
                    getActivity().setTitle(R.string.news_sa);
                    break;
                default:
                    faculty = "informatik";
                    getActivity().setTitle(R.string.news_if);
                    break;
            }
        }
    }

    private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        private final String TAG = "PreferenceListener";
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            boolean prefNotificationEnabled = sharedPref.getBoolean("pref_switch_notifications", false);
            int prefNotificationTime = sharedPref.getInt("pref_notification_time", 600);
            String prefFaculty = sharedPref.getString("pref_faculty", "IF");

            setFaculty(prefFaculty);

            new getNews().execute();

            while(getActivity() == null){
                try {
                    Log.d(TAG,"Wait for activity...");
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            AlarmManager am = getActivity().getSystemService(AlarmManager.class);
            Intent notifIntent = new Intent(getActivity(), AlarmReceiver.class);
            PendingIntent pendingNotifIntent = PendingIntent.getBroadcast(getActivity(), 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (prefNotificationEnabled) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, prefNotificationTime / 100);
                calendar.set(Calendar.MINUTE, prefNotificationTime % 100);
                calendar.set(Calendar.SECOND, 0);

                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingNotifIntent);

                new LoadAppointmentsTask().execute();
            } else {
                if (pendingNotifIntent != null)
                    am.cancel(pendingNotifIntent);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            case R.id.app_bar_appointments:
                startActivity(new Intent(getActivity(), AppointmentActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_overview, container, false);
        listView = view.findViewById(R.id.NewsListView);
        getWebsiteContent();
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * for News (Schwarzes Brett)
     */

    private void getWebsiteContent() {
        new getNews().execute();
    }

    public class getNews extends AsyncTask<Void, Void, Void> {
        List<Spanned> spanned = new ArrayList<>();
        HashMap<String, String> cookies;
        HashMap<String, String> formData;
        int position;
        int count=0;
        @Override
        protected Void doInBackground(Void... voids) {
            count++;
            Log.d("count: ",""+count);
            try {

                if(count<2) {
                    formData = new HashMap<>();

                    Connection.Response loginForm = Jsoup.connect("https://www.haw-landshut.de/hochschule/fakultaeten/informatik/infos-zum-laufenden-studienbetrieb.html")
                            .method(Connection.Method.GET)
                            .execute();
                    cookies = new HashMap<>(loginForm.cookies());
                    formData.put("utf8", "e2 9c 93");
                    formData.put("user", Credentials.getUsername());
                    formData.put("pass", Credentials.getPassword());
                    formData.put("logintype", "login");
                    formData.put("redirect_url", "nc/hochschule/fakultaeten/" + faculty + "/infos-zum-laufenden-studienbetrieb/schwarzes-brett.html");
                    formData.put("tx_felogin_pi1[noredirect]", "0");
                    formData.put("submit", "");


                    Log.d("count","incount_1");
                    Connection.Response document = Jsoup.connect("https://www.haw-landshut.de/nc/hochschule/fakultaeten/"+faculty+"/infos-zum-laufenden-studienbetrieb/schwarzes-brett.html")
                            .cookies(cookies)
                            .data(formData)
                            .method(Connection.Method.POST)
                            .execute();
                    Document doc = document.parse();
                    Elements elements = doc.getElementsByAttributeValue("class", "col-lg-9 col-sm-12");
                    for (Element e : elements) {
                        spanned.add(fromHtml(String.valueOf("<br>" + e)));
                    }
                    position = spanned.size();
                    formData.clear();
                    cookies.clear();
                    doc = null;
                }
                else{
                    formData = new HashMap<>();

                    Connection.Response loginForm = Jsoup.connect("https://www.haw-landshut.de/hochschule/fakultaeten/informatik/infos-zum-laufenden-studienbetrieb.html")
                            .method(Connection.Method.GET)
                            .execute();
                    cookies = new HashMap<>(loginForm.cookies());
                    formData.put("utf8", "e2 9c 93");
                    formData.put("user", Credentials.getUsername());
                    formData.put("pass", Credentials.getPassword());
                    formData.put("logintype", "login");
                    formData.put("redirect_url", "nc/hochschule/fakultaeten/" + faculty + "/infos-zum-laufenden-studienbetrieb/schwarzes-brett/page/"+count+".html");
                    formData.put("tx_felogin_pi1[noredirect]", "0");
                    formData.put("submit", "");
                    Connection.Response document = Jsoup.connect("https://www.haw-landshut.de/nc/hochschule/fakultaeten/"+faculty+"/infos-zum-laufenden-studienbetrieb/schwarzes-brett/page/"+count+".html")
                            .cookies(cookies)
                            .data(formData)
                            .method(Connection.Method.POST)
                            .execute();
                    Document doc = document.parse();
                    Log.d("titel:",doc.location());
                    Elements elements = doc.getElementsByAttributeValue("class", "col-lg-9 col-sm-12");
                    for (Element e : elements) {
                        spanned.add(fromHtml(String.valueOf("<br>" + e)));
                    }
                    position = spanned.size();
                    onPostExecute(null);
                    Log.d("position:",position+"");
                    listView.setSelection(position-14);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(getView() != null) {
                getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                ArrayAdapter<Spanned> adapter = new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, spanned);
                listView.setAdapter(adapter);

                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }
                    Boolean flag_loading=false;
                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                        {
                            if(!flag_loading)
                            {
                                flag_loading = true;
                                doInBackground();
                                listView.deferNotifyDataSetChanged();
                            }
                        }

                    }
                });
            }
        }

        @SuppressWarnings("deprecation")
        Spanned fromHtml(String html) {
            Spanned result;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
            } else {
                result = Html.fromHtml(html);
            }
            return result;
        }
    }



    private class LoadAppointmentsTask extends AsyncTask<Void, Integer, Void> {
        private final String TAG = "LoadAppointmentsTask";
        private String[] downloadedAppointments;

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "Get data from database.");

            while (dao == null)
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            dao.deleteAllAppointments(); //Debug
            List<Appointment> appointments = dao.getAllAppointments();

            if (appointments.size() == 0) {
                Log.d(TAG, "Database is empty.");
                Log.d(TAG, "Get data from internet.");

                String result = downloadAppointments();
                if (result != null) {
                    downloadedAppointments = result.trim().split("\n");

                    String tmp[];
                    for (String downloadedAppointment : downloadedAppointments) {
                        Log.d(TAG, downloadedAppointment);
                        tmp = downloadedAppointment.trim().split(" - ");
                        dao.insertAppointment(new Appointment(tmp[0].trim(), tmp[1].trim()));
                    }
                }
                appointments = dao.getAllAppointments();
            }
            if (appointments.size() == 0) {
                Log.d(TAG, "No appointments found.");
                return null;
            } else {
                Log.d(TAG, "Appointments loaded");
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        private String downloadAppointments() {
            String path = "https://drive.google.com/uc?export=download&id=12tWQQN6Zd51Hni1NmJm0KyHQukVTrg_r";
            String fileName = "Termine.txt";
            String result = "";
            File file;
            URL url;
            InputStream input;
            OutputStream output;
            HttpURLConnection connection;

            if (getActivity() == null)
                return null;

            try {
                file = File.createTempFile(fileName, null, getActivity().getCacheDir());

                url = new URL(path);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "Connection failed");
                    return null;
                }

                input = connection.getInputStream();
                output = new FileOutputStream(file, false);

                byte data[] = new byte[4096];
                while (input.read(data) != -1) {
                    output.write(data);
                }

                output.close();
                input.close();
                connection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
                String line;

                while ((line = br.readLine()) != null) {
                    //Log.d(TAG, line);
                    result += line + "\n";
                }
            } catch (IOException e) {
                System.out.println("Fehler Datei list.txt Datei nicht vorhanden.");
                System.exit(1);
            }


            return result;
        }
    }
}
