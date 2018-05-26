package de.haw_landshut.hawmobile.news;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

//import android.app.Fragment;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.*;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.haw_landshut.hawmobile.*;
import de.haw_landshut.hawmobile.base.AppointmentDao;
import de.haw_landshut.hawmobile.base.HAWDatabase;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
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
    private RecyclerView recyclerView;
    private SpannedAdapter spannedAdapter;
    private List<Spanned> spanned = new ArrayList<>();
    private LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    private int page_count = 0;
    private OnFragmentInteractionListener mListener;
    private LoadAppointmentsTask loadAppointmentsTask;

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
        Log.d("","OnCreate");
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        HAWDatabase database = ((MainActivity) getActivity()).getDatabase();
        dao = database.appointmentDao();

        //Termine
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPref.registerOnSharedPreferenceChangeListener(prefListener);

        if(loadAppointmentsTask != null && loadAppointmentsTask.isCancelled())
            loadAppointmentsTask = null;

        if(loadAppointmentsTask == null) {
            loadAppointmentsTask = new LoadAppointmentsTask(getContext());
            loadAppointmentsTask.execute();
        }

        //Termine ende
        String prefFaculty = sharedPref.getString("pref_faculty", "IF");

        setFaculty(prefFaculty);
    }

    void setFaculty(String prefFaculty) {
        if (getActivity() != null) {
            switch (prefFaculty) {
                case "BW":
                    faculty = "betriebswirtschaft";
                    getActivity().setTitle(R.string.news_bw);
                    page_count = 0;
                    break;
                case "EW":
                    faculty = "elektrotechnik-und-wirtschaftsingenieurwesen";
                    getActivity().setTitle(R.string.news_ew);

                    page_count = 0;
                    break;
                case "IF":
                    faculty = "informatik";
                    getActivity().setTitle(R.string.news_if);
                    page_count = 0;
                    break;
                case "IS":
                    faculty = "interdisziplinaere-studien";
                    getActivity().setTitle(R.string.news_ids);
                    page_count = 0;
                    break;
                case "MA":
                    faculty = "maschinenbau";
                    getActivity().setTitle(R.string.news_ma);
                    page_count = 0;
                    break;
                case "SA":
                    faculty = "soziale-arbeit";
                    getActivity().setTitle(R.string.news_sa);
                    page_count = 0;
                    break;
                default:
                    faculty = "informatik";
                    getActivity().setTitle(R.string.news_if);
                    page_count = 0;
                    break;
            }
        }
    }

    private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        private final String TAG = "PreferenceListener";

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.d("","SharedPrefencesChanged");

            if(key.equals("pref_switch_notifications") || key.equals("pref_notification_time")){
                Log.d("TAG","Listener: Notification changed");
                boolean prefNotificationEnabled = sharedPref.getBoolean("pref_switch_notifications", false);
                int prefNotificationTime = sharedPref.getInt("pref_notification_time", 600);

                if(getActivity() == null)
                    return;

                AlarmManager am = getActivity().getSystemService(AlarmManager.class);
                Intent notifIntent = new Intent(getActivity(), AlarmReceiver.class);
                PendingIntent pendingNotifIntent = PendingIntent.getBroadcast(getActivity(), 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (prefNotificationEnabled) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, prefNotificationTime / 100);
                    calendar.set(Calendar.MINUTE, prefNotificationTime % 100);
                    calendar.set(Calendar.SECOND, 0);

                    am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingNotifIntent);

                    if(loadAppointmentsTask != null && loadAppointmentsTask.isCancelled())
                        loadAppointmentsTask = null;

                    if(loadAppointmentsTask == null) {
                        loadAppointmentsTask = new LoadAppointmentsTask(getContext());
                        loadAppointmentsTask.execute();
                    }
                } else {
                    if (pendingNotifIntent != null)
                        am.cancel(pendingNotifIntent);
                }
            }
            else if(key.equals("pref_faculty")){
                Log.d(TAG, "Listener: faculty changed");
                String prefFaculty = sharedPref.getString("pref_faculty", "IF");
                setFaculty(prefFaculty);
                getWebsiteContent();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getWebsiteContent();
//        getActivity().recreate();
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
        Log.d("","OnCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_overview, container, false);
        recyclerView = view.findViewById(R.id.NewsRecyclerView);
        assert recyclerView != null;
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;

    }


    @Override
    public void onAttach(Context context) {
        Log.d("","OnAttach");
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
        Log.d("","OnDetach");
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

    public interface OnLoadMoreListener {

        void onLoadMore();
    }


    public class getNews extends AsyncTask<Void, Void, Void> {
        HashMap<String, String> cookies;
        HashMap<String, String> formData;
        Boolean max_page = false;
        private boolean loading;

        @Override
        protected Void doInBackground(Void... voids) {
            page_count++;
            Log.d("startpage-count: ", page_count + "");
            try {
                if (page_count < 2 && !max_page) {
                    formData = new HashMap<>();
                    spanned = new ArrayList<>();
                    formData.put("utf8", "e2 9c 93");
                    formData.put("user", Credentials.getUsername());
                    formData.put("pass", Credentials.getPassword());
                    formData.put("logintype", "login");
                    formData.put("redirect_url", "nc/hochschule/fakultaeten/" + faculty + "/infos-zum-laufenden-studienbetrieb/schwarzes-brett.html");
                    formData.put("tx_felogin_pi1[noredirect]", "0");
                    formData.put("submit", "");
                    Connection.Response loginForm = Jsoup.connect("https://www.haw-landshut.de/hochschule/fakultaeten/informatik/infos-zum-laufenden-studienbetrieb.html")
                            .method(Connection.Method.GET)
                            .execute();
                    cookies = new HashMap<>(loginForm.cookies());
                    Connection.Response document = Jsoup.connect("https://www.haw-landshut.de/nc/hochschule/fakultaeten/" + faculty + "/infos-zum-laufenden-studienbetrieb/schwarzes-brett.html")
                            .cookies(cookies)
                            .data(formData)
                            .method(Connection.Method.POST)
                            .execute();
                    Document doc = document.parse();
                    Elements elements = doc.getElementsByAttributeValue("class", "col-lg-9 col-sm-12");
                    if (!elements.isEmpty())
                       for (Element e : elements) {
                        spanned.add(fromHtml(String.valueOf(e)));
                    }
                    else
                    max_page = true;
                    Log.d("spanned_size:",spanned.size()+"");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(page_count < 2){
            if (getActivity() != null) {
                getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                spannedAdapter = new SpannedAdapter(spanned, getContext());
                recyclerView.setAdapter(spannedAdapter);

                spannedAdapter.setmOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {page_count++;

                        spanned.add(null);
                        spannedAdapter.notifyItemInserted(spanned.size() - 1);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Log.d("Advanced_page_count: ", page_count + "");
                                spanned.remove(spanned.size() - 1);
                                spannedAdapter.notifyItemRemoved(spanned.size());
//                                if(!formData.isEmpty()) {
//                                    formData.remove("redirect_url");
//                                    formData.put("redirect_url", "nc/hochschule/fakultaeten/" + faculty + "/infos-zum-laufenden-studienbetrieb/schwarzes-brett/page/" + page_count + ".html");
//                                }
//                                else {
//
                                try {
                                    Connection.Response loginForm = Jsoup.connect("https://www.haw-landshut.de/hochschule/fakultaeten/informatik/infos-zum-laufenden-studienbetrieb.html")
                                            .method(Connection.Method.GET)
                                            .execute();
                                    cookies = new HashMap<>(loginForm.cookies());
                                    formData = new HashMap<>();
                                    formData.put("utf8", "e2 9c 93");
                                    formData.put("user", Credentials.getUsername());
                                    formData.put("pass", Credentials.getPassword());
                                    formData.put("logintype", "login");
                                    formData.put("redirect_url", "nc/hochschule/fakultaeten/" + faculty + "/infos-zum-laufenden-studienbetrieb/schwarzes-brett/page/" + page_count + ".html");
                                    formData.put("tx_felogin_pi1[noredirect]", "0");
                                    formData.put("submit", "");
//                                }
                                    Connection.Response document = null;

                                    document = Jsoup.connect("https://www.haw-landshut.de/nc/hochschule/fakultaeten/" + faculty + "/infos-zum-laufenden-studienbetrieb/schwarzes-brett/page/" + page_count + ".html")
                                            .cookies(cookies)
                                            .data(formData)
                                            .method(Connection.Method.POST)
                                            .execute();

                                    Document doc = document.parse();

                                    Elements elements = doc.getElementsByAttributeValue("class", "col-lg-9 col-sm-12");
                                    if (elements.isEmpty()) {
                                        max_page = true;
                                    } else {
                                        for (Element e : elements) {
                                            spanned.add(fromHtml(String.valueOf("<br>" + e)));
                                        }

                                        spannedAdapter.notifyDataSetChanged();
                                        spannedAdapter.setLoaded();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 5000);
                    }
                });
            }

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

    public class SpannedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView textView;

            MyViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.OneElement);
            }
        }

        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;
        private NewsOverview.OnLoadMoreListener mOnLoadMoreListener;
        private boolean isLoading;
        private int visibleThreshold = 5;
        private List<Spanned> spannedArrayList;
        private Context context;

        public SpannedAdapter(List<Spanned> spannedArrayList, Context context) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int totalItemCount, lastVisibleItem;
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null)
                            mOnLoadMoreListener.onLoadMore();
                        isLoading = true;
                    }
                }


            });
            this.spannedArrayList = spannedArrayList;
            this.context = context;
        }

        public void setmOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }

        @Override
        public int getItemViewType(int position) {
            return spanned.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        private Context getContext() {
            return context;
        }

        @Override
        public int getItemCount() {
            return spanned == null ? 0 : spanned.size();
        }

        public void setLoaded() {
            isLoading = false;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.news_row, parent, false);
                return new MyViewHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(NewsOverview.this.getContext()).inflate(R.layout.news_bottom_loading, parent, false);
                return new LoadingViewHolder(view);
            }
            Context context = parent.getContext();

            View view = LayoutInflater.from(context).inflate(R.layout.news_row, parent, false);
            return new de.haw_landshut.hawmobile.news.NewsOverview.SpannedAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MyViewHolder) {
                Spanned span = spanned.get(position);
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                myViewHolder.textView.setText(span);
            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBarBottom);
        }
    }
}
