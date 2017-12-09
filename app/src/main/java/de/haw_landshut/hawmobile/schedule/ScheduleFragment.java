package de.haw_landshut.hawmobile.schedule;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import de.haw_landshut.hawmobile.MainActivity;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.ProfData;
import de.haw_landshut.hawmobile.base.ScheduleDao;
import org.w3c.dom.Text;

import java.util.prefs.Preferences;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View.OnClickListener ocl;
    public BottomSheetBehavior mBottomSheetBehavior1;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences preference;

    private OnFragmentInteractionListener mListener;


    public ScheduleFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance() {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//        ocl = new OnClickLabel();
        preference=getActivity().getPreferences(Context.MODE_PRIVATE);
        if(!preference.getBoolean("Profs inserted",false)) {
            ScheduleDao scheduleDao = MainActivity.getHawDatabase().scheduleDao();
            scheduleDao.insertAlleProfs(new ProfData("Johannes", "Busse"), new ProfData("Matthias", "Dorfner"), new ProfData("Ludwig", "Griebl"), new ProfData("Peter", "Hartmann"), new ProfData("Wolfgang", "Jürgensen"), new ProfData("Abdelmajid", "Khelil"), new ProfData("Monika", "Messerer"), new ProfData("Markus", "Mock"), new ProfData("Dieter", "Nazareth"), new ProfData("Martin", "Pellkofer"), new ProfData("Gudrun", "Schiedermeier"), new ProfData("Peter", "Scholz"), new ProfData("Christian", "See"), new ProfData("Andreas", "Siebert"), new ProfData("Johann", "Uhrmann"), new ProfData("Jürgen", "Wunderlich"), new ProfData("Thomas", "Franzke"), new ProfData("Michael", "Bürker"), new ProfData("Patrick", "Dieses"), new ProfData("Marcus", "Fischer"), new ProfData("Dieter", "Greipl"), new ProfData("Sandra", "Gronover"), new ProfData("Michael", "Gumbsheimer"), new ProfData("Burkhard", "Jaeger"), new ProfData("Alexander", "Kumpf"), new ProfData("Michael", "Leckebusch"), new ProfData("Maren", "Martens"), new ProfData("Bernd", "Mühlfriedel"), new ProfData("Martin", "Prasch"), new ProfData("Heinz-Werner", "Schuster"), new ProfData("Hanns", "Robby"), new ProfData("Valentina", "Speidel"), new ProfData("Thomas", "Stauffert"), new ProfData("Karl", "Stoffel"), new ProfData("Manuel", "Strunz"), new ProfData("Thomas", "Zinser"), new ProfData("Stefan-Alexander", "Arlt"), new ProfData("Andrea", "Badura"), new ProfData("Andreas", "Breidenassel"), new ProfData("Petra", "Denk"), new ProfData("Andreas", "Dieterle"), new ProfData("Guido", "Dietl"), new ProfData("Armin", "Englmaier"), new ProfData("Christian", "Faber"), new ProfData("Thomas", "Faldum"), new ProfData("Jürgen", "Gebert"), new ProfData("Jürgen", "Giersch"), new ProfData("Michaela", "Gruber"), new ProfData("Artem", "Ivanov"), new ProfData("Johann", "Jaud"), new ProfData("Benedict", "Kemmerer"), new ProfData("Alexander", "Kleimaier"), new ProfData("Carl-Gustaf", "Kligge"), new ProfData("Dieter", "Koller"), new ProfData("Raimund", "Kreis"), new ProfData("Jörg", "Mareczek"), new ProfData("Sebastian", "Meißner"), new ProfData("Fritz", "Pörnbacher"), new ProfData("Mathias", "Rausch"), new ProfData("Stefanie", "Remmele"), new ProfData("Goetz", "Roderer"), new ProfData("Carsten", "Röh"), new ProfData("Magda", "Schiegl"), new ProfData("Markus", "Schmitt"), new ProfData("Markus", "Schneider"), new ProfData("Martin", "Soika"), new ProfData("Peter", "Spindler"), new ProfData("Reimer", "Studt"), new ProfData("Holger", "Timinger"), new ProfData("Klaus", "Timmer"), new ProfData("Petra", "Tippmann-Krayer"), new ProfData("Hubertus", "C."), new ProfData("Jürgen", "Welter"), new ProfData("Thomas", "Wolf"), new ProfData("Norbert", "Babel"), new ProfData("Walter", "Fischer"), new ProfData("Martin", "Förg"), new ProfData("Bernhard", "Gubanka"), new ProfData("Diana", "Hehenberger-Risse"), new ProfData("Josef", "Hofmann"), new ProfData("Peter", "Holbein"), new ProfData("Barbara", "Höling"), new ProfData("Otto", "Huber"), new ProfData("Marcus", "Jautze"), new ProfData("Hubert", "Klaus"), new ProfData("Jan", "Köll"), new ProfData("Detlev", "Maurer"), new ProfData("Karl-Heinz", "Pettinger"), new ProfData("Franz", "Prexler"), new ProfData("Ralph", "Pütz"), new ProfData("Karl", "Reiling"), new ProfData("Wolfgang", "Reimann"), new ProfData("Tim", "Rödiger"), new ProfData("Sven", "Roeren"), new ProfData("Holger", "Saage"), new ProfData("Manfred", "Strohe"), new ProfData("Volker", "Weinbrenner"), new ProfData("Sigrid", "A."), new ProfData("Hubert", "Beste"), new ProfData("Stefan", "Borrmann"), new ProfData("Clemens", "Dannenbeck"), new ProfData("Christoph", "Fedke"), new ProfData("Bettina", "Kühbeck"), new ProfData("Katrin", "Liel"), new ProfData("Johannes", "Lohner"), new ProfData("Dominique", "Moisl"), new ProfData("Karin", "E."), new ProfData("Maria", "Ohling"), new ProfData("Mihri", "Özdoğan"), new ProfData("Andreas", "Panitz"), new ProfData("Barbara", "Thiessen"), new ProfData("Ralph", "Viehhauser"), new ProfData("Mechthild", "Wolff"), new ProfData("Eva", "Wunderer"));
        preference.edit().putBoolean("Profs inserted",true).apply();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_schedule, container, false);




        TextView m1 = (TextView)view.findViewById(R.id.schedule_tv_h1_monday);
        //m1.setOnClickListener(ocl);
        TextView m2 = (TextView)view.findViewById(R.id.schedule_tv_h3_monday);
        m2.setOnClickListener(ocl);
        TextView m3 = (TextView)view.findViewById(R.id.schedule_tv_h2_monday);
        m3.setOnClickListener(ocl);
        TextView m4 = (TextView)view.findViewById(R.id.schedule_tv_h4_monday);
        m4.setOnClickListener(ocl);
        TextView m5 = (TextView)view.findViewById(R.id.schedule_tv_h5_monday);
        m5.setOnClickListener(ocl);
        TextView m6 = (TextView)view.findViewById(R.id.schedule_tv_h6_monday);
        m6.setOnClickListener(ocl);

        View bottomSheet = view.findViewById(R.id.bottom_sheet1);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);


        m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

                }
                else {
                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }
            }
        });





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
}
