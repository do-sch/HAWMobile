package de.haw_landshut.hawmobile.news;


import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.haw_landshut.hawmobile.Credentials;
import de.haw_landshut.hawmobile.R;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import java.io.IOException;


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

//        String test="Hello Test";
//        Log.d("NewOverview", test);
//        textView.setText(test);
//        textView.setTextColor(Color.BLUE);
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


    public void getWebsiteContent()
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
//                org.jsoup.nodes.Document doc = Jsoup.connect("https://www.haw-landshut.de/nc/hochschule/fakultaeten/informatik/infos-zum-laufenden-studienbetrieb/schwarzes-brett.html")
//                        .data("user", Credentials.getUsername())
//                        .data("pass",Credentials.getPassword())
//                        .data("submit","")
//                        .post();
/**
                Connection.Response login = Jsoup.connect("https://www.haw-landshut.de/hochschule/fakultaeten/informatik/infos-zum-laufenden-studienbetrieb.html")
                        .data("user", Credentials.getUsername())
                        .data("pass",Credentials.getPassword())
                        .data("submit","")
                        .method(Connection.Method.POST)
                        .execute();
                org.jsoup.nodes.Document doc = Jsoup.connect("https://www.haw-landshut.de/nc/hochschule/fakultaeten/informatik/infos-zum-laufenden-studienbetrieb/schwarzes-brett.html")
                        .cookies(login.cookies())
                        .get();

                //TODO : not finished yet
                String want,have;

                have = "Infos zum laufenden Studienbetrieb: Hochschule Landshut";
                //if login successfully return wanted
                want = "Schwarzes Brett: Hochschule Landshut";
                String docu = doc.toString();
                Boolean search = docu.contains(want);


                content = search + doc.toString();
**/

                Connection.Response loginForm = Jsoup.connect("https://www.haw-landshut.de/hochschule/fakultaeten/informatik/infos-zum-laufenden-studienbetrieb.html")
                        .method(Connection.Method.GET)
                        .execute();

                org.jsoup.nodes.Document document = Jsoup.connect("https://www.haw-landshut.de/nc/hochschule/fakultaeten/informatik/infos-zum-laufenden-studienbetrieb/schwarzes-brett.html")
                        .data("cookieexists", "false")
                        .data("user", Credentials.getUsername())
                        .data("pass", Credentials.getPassword())
                        .data("submit","")
                        .cookies(loginForm.cookies())
                        .post();
                //debug info
                String want = "Schwarzes Brett: Hochschule Landshut";
                String have = "Infos zum laufenden Studienbetrieb: Hochschule Landshut";
                String search = document.toString();
                Boolean hav = search.contains(have);
                Boolean wan = search.contains(want);
               content = "Login page: "+hav+"\nLogged in Schwartes Brett: "+wan+"\n"+document.toString();

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

}
