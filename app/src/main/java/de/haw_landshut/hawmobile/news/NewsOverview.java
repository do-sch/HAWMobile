package de.haw_landshut.hawmobile.news;


import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import de.haw_landshut.hawmobile.Credentials;
import de.haw_landshut.hawmobile.R;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsOverview.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsOverview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsOverview extends Fragment {
    private TextView textView;
    Connection.Response document;

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
        NewsOverview fragment = new NewsOverview();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_overview, container, false);
        textView =  view.findViewById(R.id.textFromWeb);
        getWebsiteContent();
        return view;

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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


    private void getWebsiteContent()
    {
        new getIt().execute();
    }
    public class getIt extends AsyncTask<Void,Void,Void>
    {
        String content;

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                HashMap<String, String> cookies = new HashMap<>();
                HashMap<String, String> formData = new HashMap<>();
                Connection.Response loginForm = Jsoup.connect("https://www.haw-landshut.de/hochschule/fakultaeten/informatik/infos-zum-laufenden-studienbetrieb.html")
                        .method(Connection.Method.GET)
                        .execute();
                cookies.putAll(loginForm.cookies());
                formData.put("utf8", "e2 9c 93");
                formData.put("user", Credentials.getUsername());
                formData.put("pass", Credentials.getPassword());
                formData.put("logintype","login");
                formData.put("redirect_url","nc/hochschule/fakultaeten/informatik/infos-zum-laufenden-studienbetrieb/schwarzes-brett.html");
                formData.put("tx_felogin_pi1[noredirect]", "0");
                formData.put("submit", "");

                Connection.Response document = Jsoup.connect("https://www.haw-landshut.de/nc/hochschule/fakultaeten/informatik/infos-zum-laufenden-studienbetrieb/schwarzes-brett.html")
                        .cookies(cookies)
                        .data(formData)
                        .method(Connection.Method.POST)
                        .execute();

                //debug info start
                String have = "Infos zum laufenden Studienbetrieb: Hochschule Landshut";
                String doc = document.parse().toString();
                //debug info end
                List<String> allMatches = new ArrayList<String>();
                //
                //
                Matcher m = Pattern.compile("<div class=\"list_date\".*>\\s*([[:ascii:]\\s\\wäüöß]*?)\\s*</div>[\\s]*<h2>([[:ascii:]üäöß]*?)</h2>[[:ascii:]üäöß]*?</p>\\s*<p>([[:ascii:]\\s\\wäüöß]*?)</p>\\s*<p>([[:ascii:]üäöß]*?)</p>(\\s*[[:ascii:]üäöß]*?)</div>")
                        .matcher(doc);
                while (m.find()) {
                    allMatches.add(m.group(1)+"\n");
                    allMatches.add(m.group(2).toUpperCase()+"\n");
                    allMatches.add(m.group(3));
                    allMatches.add(m.group(4));
                    allMatches.add(m.group(5)+"\n\n");
                }
                Log.d(doc,"debug");





                //debug info start
                Boolean hav = doc.contains(have);
               content = "\ndebug info: Website loaded:\nSchwartes Brett: "+!hav+"\nInfo zum Studienbetrieb: "+hav+"\n\n"+allMatches.size();
               //debug info end
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            textView.setText(content);
            textView.setMovementMethod(new ScrollingMovementMethod());
        }
    }


//TODO: not working

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().setTitle("Schwarzes Brett");
    }
}
