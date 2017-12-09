package de.haw_landshut.hawmobile.schedule;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import de.haw_landshut.hawmobile.MainActivity;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.FaecherData;
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
    public static BottomSheetBehavior mBottomSheetBehavior1;
    View bottomSheet;
    public static TextView currentTV;
    public static EditText et_fach;
    public static EditText et_prof;
    public static EditText et_raum;
    Button edit;
    Button save;
    Button cancel;


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

        ocl = new OnClickLabel();
        preference=getActivity().getPreferences(Context.MODE_PRIVATE);
        //new BeginnInsertion().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        edit = (Button)view.findViewById(R.id.btn_edit);
        save = (Button)view.findViewById(R.id.btn_save);
        cancel = (Button)view.findViewById(R.id.btn_cancel);
        et_fach = (EditText)view.findViewById(R.id.et_fach);
        et_prof = (EditText)view.findViewById(R.id.et_prof);
        et_raum = (EditText)view.findViewById(R.id.et_raum);
        bottomSheet = view.findViewById(R.id.bottom_sheet1);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);

        //Montags
        final TextView m1 = (TextView)view.findViewById(R.id.schedule_tv_h1_monday);
        m1.setOnClickListener(ocl);
        final TextView m2 = (TextView)view.findViewById(R.id.schedule_tv_h3_monday);
        m2.setOnClickListener(ocl);
        final TextView m3 = (TextView)view.findViewById(R.id.schedule_tv_h2_monday);
        m3.setOnClickListener(ocl);
        final TextView m4 = (TextView)view.findViewById(R.id.schedule_tv_h4_monday);
        m4.setOnClickListener(ocl);
        final TextView m5 = (TextView)view.findViewById(R.id.schedule_tv_h5_monday);
        m5.setOnClickListener(ocl);
        final TextView m6 = (TextView)view.findViewById(R.id.schedule_tv_h6_monday);
        m6.setOnClickListener(ocl);

        //Dienstags
        final TextView d1 = (TextView)view.findViewById(R.id.schedule_tv_h1_tuesday);
        d1.setOnClickListener(ocl);
        final TextView d2 = (TextView)view.findViewById(R.id.schedule_tv_h3_tuesday);
        d2.setOnClickListener(ocl);
        final TextView d3 = (TextView)view.findViewById(R.id.schedule_tv_h2_tuesday);
        d3.setOnClickListener(ocl);
        final TextView d4 = (TextView)view.findViewById(R.id.schedule_tv_h4_tuesday);
        d4.setOnClickListener(ocl);
        final TextView d5 = (TextView)view.findViewById(R.id.schedule_tv_h5_tuesday);
        d5.setOnClickListener(ocl);
        final TextView d6 = (TextView)view.findViewById(R.id.schedule_tv_h6_tuesday);
        d6.setOnClickListener(ocl);

        //Mittwochs
        final TextView w1 = (TextView)view.findViewById(R.id.schedule_tv_h1_wednesday);
        w1.setOnClickListener(ocl);
        final TextView w2 = (TextView)view.findViewById(R.id.schedule_tv_h3_wednesday);
        w2.setOnClickListener(ocl);
        final TextView w3 = (TextView)view.findViewById(R.id.schedule_tv_h2_wednesday);
        w3.setOnClickListener(ocl);
        final TextView w4 = (TextView)view.findViewById(R.id.schedule_tv_h4_wednesday);
        w4.setOnClickListener(ocl);
        final TextView w5 = (TextView)view.findViewById(R.id.schedule_tv_h5_wednesday);
        w5.setOnClickListener(ocl);
        final TextView w6 = (TextView)view.findViewById(R.id.schedule_tv_h6_wednesday);
        w6.setOnClickListener(ocl);

        //Donnerstags
        final TextView do1 = (TextView)view.findViewById(R.id.schedule_tv_h1_thursday);
        do1.setOnClickListener(ocl);
        final TextView do2 = (TextView)view.findViewById(R.id.schedule_tv_h3_thursday);
        do2.setOnClickListener(ocl);
        final TextView do3 = (TextView)view.findViewById(R.id.schedule_tv_h2_thursday);
        do3.setOnClickListener(ocl);
        final TextView do4 = (TextView)view.findViewById(R.id.schedule_tv_h4_thursday);
        do4.setOnClickListener(ocl);
        final TextView do5 = (TextView)view.findViewById(R.id.schedule_tv_h5_thursday);
        do5.setOnClickListener(ocl);
        final TextView do6 = (TextView)view.findViewById(R.id.schedule_tv_h6_thursday);
        do6.setOnClickListener(ocl);

        //Freitags
        final TextView f1 = (TextView)view.findViewById(R.id.schedule_tv_h1_friday);
        f1.setOnClickListener(ocl);
        final TextView f2 = (TextView)view.findViewById(R.id.schedule_tv_h3_friday);
        f2.setOnClickListener(ocl);
        final TextView f3 = (TextView)view.findViewById(R.id.schedule_tv_h2_friday);
        f3.setOnClickListener(ocl);
        final TextView f4 = (TextView)view.findViewById(R.id.schedule_tv_h4_friday);
        f4.setOnClickListener(ocl);
        final TextView f5 = (TextView)view.findViewById(R.id.schedule_tv_h5_friday);
        f5.setOnClickListener(ocl);
        final TextView f6 = (TextView)view.findViewById(R.id.schedule_tv_h6_monday);
        f6.setOnClickListener(ocl);



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    cancel.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                    et_fach.setEnabled(true);
                    et_prof.setEnabled(true);
                    et_raum.setEnabled(true);


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    cancel.setVisibility(View.INVISIBLE);
                    save.setVisibility(View.GONE);
                    edit.setVisibility(View.VISIBLE);
                    et_fach.setEnabled(false);
                    et_prof.setEnabled(false);
                    et_raum.setEnabled(false);
                    currentTV.setText(et_fach.getText());
                    ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_fach.setText("");
                et_fach.setEnabled(false);
                et_prof.setText("");
                et_prof.setEnabled(false);
                et_raum.setText("");
                et_raum.setEnabled(false);
                save.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.INVISIBLE);
                ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
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

    private class BeginnInsertion extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            ScheduleDao scheduleDao = MainActivity.getHawDatabase().scheduleDao();
            if(!preference.getBoolean("Profs inserted",false)) {
                scheduleDao.insertAlleProfs(new ProfData("Johannes", "Busse"), new ProfData("Matthias", "Dorfner"), new ProfData("Ludwig", "Griebl"), new ProfData("Peter", "Hartmann"), new ProfData("Wolfgang", "Jürgensen"), new ProfData("Abdelmajid", "Khelil"), new ProfData("Monika", "Messerer"), new ProfData("Markus", "Mock"), new ProfData("Dieter", "Nazareth"), new ProfData("Martin", "Pellkofer"), new ProfData("Gudrun", "Schiedermeier"), new ProfData("Peter", "Scholz"), new ProfData("Christian", "See"), new ProfData("Andreas", "Siebert"), new ProfData("Johann", "Uhrmann"), new ProfData("Jürgen", "Wunderlich"), new ProfData("Thomas", "Franzke"), new ProfData("Michael", "Bürker"), new ProfData("Patrick", "Dieses"), new ProfData("Marcus", "Fischer"), new ProfData("Dieter", "Greipl"), new ProfData("Sandra", "Gronover"), new ProfData("Michael", "Gumbsheimer"), new ProfData("Burkhard", "Jaeger"), new ProfData("Alexander", "Kumpf"), new ProfData("Michael", "Leckebusch"), new ProfData("Maren", "Martens"), new ProfData("Bernd", "Mühlfriedel"), new ProfData("Martin", "Prasch"), new ProfData("Heinz-Werner", "Schuster"), new ProfData("Hanns", "Robby"), new ProfData("Valentina", "Speidel"), new ProfData("Thomas", "Stauffert"), new ProfData("Karl", "Stoffel"), new ProfData("Manuel", "Strunz"), new ProfData("Thomas", "Zinser"), new ProfData("Stefan-Alexander", "Arlt"), new ProfData("Andrea", "Badura"), new ProfData("Andreas", "Breidenassel"), new ProfData("Petra", "Denk"), new ProfData("Andreas", "Dieterle"), new ProfData("Guido", "Dietl"), new ProfData("Armin", "Englmaier"), new ProfData("Christian", "Faber"), new ProfData("Thomas", "Faldum"), new ProfData("Jürgen", "Gebert"), new ProfData("Jürgen", "Giersch"), new ProfData("Michaela", "Gruber"), new ProfData("Artem", "Ivanov"), new ProfData("Johann", "Jaud"), new ProfData("Benedict", "Kemmerer"), new ProfData("Alexander", "Kleimaier"), new ProfData("Carl-Gustaf", "Kligge"), new ProfData("Dieter", "Koller"), new ProfData("Raimund", "Kreis"), new ProfData("Jörg", "Mareczek"), new ProfData("Sebastian", "Meißner"), new ProfData("Fritz", "Pörnbacher"), new ProfData("Mathias", "Rausch"), new ProfData("Stefanie", "Remmele"), new ProfData("Goetz", "Roderer"), new ProfData("Carsten", "Röh"), new ProfData("Magda", "Schiegl"), new ProfData("Markus", "Schmitt"), new ProfData("Markus", "Schneider"), new ProfData("Martin", "Soika"), new ProfData("Peter", "Spindler"), new ProfData("Reimer", "Studt"), new ProfData("Holger", "Timinger"), new ProfData("Klaus", "Timmer"), new ProfData("Petra", "Tippmann-Krayer"), new ProfData("Hubertus", "C."), new ProfData("Jürgen", "Welter"), new ProfData("Thomas", "Wolf"), new ProfData("Norbert", "Babel"), new ProfData("Walter", "Fischer"), new ProfData("Martin", "Förg"), new ProfData("Bernhard", "Gubanka"), new ProfData("Diana", "Hehenberger-Risse"), new ProfData("Josef", "Hofmann"), new ProfData("Peter", "Holbein"), new ProfData("Barbara", "Höling"), new ProfData("Otto", "Huber"), new ProfData("Marcus", "Jautze"), new ProfData("Hubert", "Klaus"), new ProfData("Jan", "Köll"), new ProfData("Detlev", "Maurer"), new ProfData("Karl-Heinz", "Pettinger"), new ProfData("Franz", "Prexler"), new ProfData("Ralph", "Pütz"), new ProfData("Karl", "Reiling"), new ProfData("Wolfgang", "Reimann"), new ProfData("Tim", "Rödiger"), new ProfData("Sven", "Roeren"), new ProfData("Holger", "Saage"), new ProfData("Manfred", "Strohe"), new ProfData("Volker", "Weinbrenner"), new ProfData("Sigrid", "A."), new ProfData("Hubert", "Beste"), new ProfData("Stefan", "Borrmann"), new ProfData("Clemens", "Dannenbeck"), new ProfData("Christoph", "Fedke"), new ProfData("Bettina", "Kühbeck"), new ProfData("Katrin", "Liel"), new ProfData("Johannes", "Lohner"), new ProfData("Dominique", "Moisl"), new ProfData("Karin", "E."), new ProfData("Maria", "Ohling"), new ProfData("Mihri", "Özdoğan"), new ProfData("Andreas", "Panitz"), new ProfData("Barbara", "Thiessen"), new ProfData("Ralph", "Viehhauser"), new ProfData("Mechthild", "Wolff"), new ProfData("Eva", "Wunderer"));
                preference.edit().putBoolean("Profs inserted",true).apply();
            }
            if(!preference.getBoolean("Subjekts inserted",false)){
                scheduleDao.insertAlleFaecher(new FaecherData("Betriebswirtschaft","Wirtschaftsmathematik"), new FaecherData("Betriebswirtschaft","Statistik"), new FaecherData("Betriebswirtschaft","Volkswirtschaftslehre I Mikroökonomie"), new FaecherData("Betriebswirtschaft","Volkswirtschaftslehre II Makroökonomie"), new FaecherData("Betriebswirtschaft","Einführung in die Betriebswirtschaftslehre"), new FaecherData("Betriebswirtschaft","Externes Rechnungswesen"), new FaecherData("Betriebswirtschaft","Kosten- und Leistungsrechnung"), new FaecherData("Betriebswirtschaft","Informationstechnologie"), new FaecherData("Betriebswirtschaft","Wirtschaftsenglisch"), new FaecherData("Betriebswirtschaft","Foreign Business Language II"), new FaecherData("Betriebswirtschaft","Principles of Marketing and Sales"), new FaecherData("Betriebswirtschaft","Principles of Human Resource Management"), new FaecherData("Betriebswirtschaft","Principles of Operations and Logistics Management"), new FaecherData("Betriebswirtschaft","Principles of Finance and Investment"), new FaecherData("Betriebswirtschaft","Principles of International Management"), new FaecherData("Betriebswirtschaft","Principles of Organisation"), new FaecherData("Betriebswirtschaft","European Law"), new FaecherData("Betriebswirtschaft","Foreign Business Language II (Teil 2)"), new FaecherData("Betriebswirtschaft","Business Administration Seminar"), new FaecherData("Betriebswirtschaft","Digital Business Models"), new FaecherData("Betriebswirtschaft","Fundamentals of International Economics"), new FaecherData("Betriebswirtschaft","Specialised Compulsory Elective Module"), new FaecherData("Betriebswirtschaft","Arbeitsrecht"), new FaecherData("Betriebswirtschaft","Designing The Global Marketing Programme"), new FaecherData("Betriebswirtschaft","Doing Business in Russia"), new FaecherData("Betriebswirtschaft","Management Accounting and Control"), new FaecherData("Betriebswirtschaft","Optimization in Logistics"), new FaecherData("Betriebswirtschaft","Private Wealth Management"), new FaecherData("Betriebswirtschaft","Wirtschaftsprivatrecht/Gesellschaftsrecht"), new FaecherData("Betriebswirtschaft","Ingenieurmathematik I"), new FaecherData("Betriebswirtschaft","Grundlagen der Elektrotechnik"), new FaecherData("Betriebswirtschaft","Informatik I"), new FaecherData("Betriebswirtschaft","Technische Mechanik"), new FaecherData("Betriebswirtschaft","Grundlagen der Betriebs- und Volkswirtschaftslehre"), new FaecherData("Betriebswirtschaft","Ingenieurmathematik II"), new FaecherData("Betriebswirtschaft","Elektronik und Messtechnik"), new FaecherData("Betriebswirtschaft","Informatik II"), new FaecherData("Betriebswirtschaft","Angewandte Physik"), new FaecherData("Betriebswirtschaft","Konstruktion und Entwicklung"), new FaecherData("Betriebswirtschaft","Regelungstechnik"), new FaecherData("Betriebswirtschaft","Mikrocomputertechnik"), new FaecherData("Betriebswirtschaft","Buchführung und Bilanzierung"), new FaecherData("Betriebswirtschaft","Grundlagen der Automobilwirtschaft"), new FaecherData("Betriebswirtschaft","Marketing und Vertrieb"), new FaecherData("Betriebswirtschaft","Kosten- und Leistungsrechnung"), new FaecherData("Betriebswirtschaft","Beschaffung, Produktion und Logistik"), new FaecherData("Betriebswirtschaft","Finanz- und Investitionswirtschaft"), new FaecherData("Betriebswirtschaft","Projektmanagement"), new FaecherData("Betriebswirtschaft","Grundlagen der Produktionstechnik"), new FaecherData("Betriebswirtschaft","Unternehmensplanspiel"), new FaecherData("Betriebswirtschaft","Wirtschaftsprivatrecht"), new FaecherData("Betriebswirtschaft","Produktions- und Prozessplanung"), new FaecherData("Betriebswirtschaft","Logistik- und Fabrikplanung"), new FaecherData("Betriebswirtschaft","Qualitätsmanagement"), new FaecherData("Betriebswirtschaft","Technischer Einkauf"), new FaecherData("Betriebswirtschaft","Wirtschaftsmathematik"), new FaecherData("Betriebswirtschaft","Statistik"), new FaecherData("Betriebswirtschaft","Volkswirtschaftslehre I Mikroökonomie"), new FaecherData("Betriebswirtschaft","Volkswirtschaftslehre II Makroökonomie"), new FaecherData("Betriebswirtschaft","Einführung in die Betriebswirtschaftslehre"), new FaecherData("Betriebswirtschaft","Externes Rechnungswesen"), new FaecherData("Betriebswirtschaft","Kosten- und Leistungsrechnung"), new FaecherData("Betriebswirtschaft","Informationstechnologie"), new FaecherData("Betriebswirtschaft","Wirtschaftsenglisch"), new FaecherData("Betriebswirtschaft","Grundlagen der Organisation"), new FaecherData("Betriebswirtschaft","Grundlagen der Material- und Fertigungswirtschaft"), new FaecherData("Betriebswirtschaft","Grundlagen des Personalmanagement"), new FaecherData("Betriebswirtschaft","Grundlagen Marketing und Vertrieb"), new FaecherData("Betriebswirtschaft","Wirtschaftsprivatrecht / Gesellschaftsrecht"), new FaecherData("Betriebswirtschaft","Arbeitsrecht"), new FaecherData("Betriebswirtschaft","Finanz- und Investitionswirtschaft"), new FaecherData("Betriebswirtschaft","Steuern"), new FaecherData("Betriebswirtschaft","Betriebswirtschaftliches Seminar"), new FaecherData("Betriebswirtschaft","Betriebswirtschaftliche Entscheidungstechniken"), new FaecherData("Betriebswirtschaft","Digital Business Models"), new FaecherData("Betriebswirtschaft","Controlling und Finanzierung"), new FaecherData("Betriebswirtschaft","Fundamentals of International Economics"), new FaecherData("Betriebswirtschaft","Produkt- und Designmanagement"), new FaecherData("Betriebswirtschaft","Datev I"), new FaecherData("Betriebswirtschaft","Designing The Global Marketing Programme"), new FaecherData("Betriebswirtschaft","Doing Business in Russia"), new FaecherData("Betriebswirtschaft","Grundlagen des Facility-Managements / Principles of Facility Management"), new FaecherData("Betriebswirtschaft","Management Accounting and Control"), new FaecherData("Betriebswirtschaft","Optimization in Logistics"), new FaecherData("Betriebswirtschaft","Private Wealth Management"), new FaecherData("Betriebswirtschaft","Risikomanagement "), new FaecherData("Betriebswirtschaft","Unternehmensführung"), new FaecherData("Betriebswirtschaft","Politische ökonomie"), new FaecherData("Betriebswirtschaft","Spezielle Steuerrechtsgebiete / Special taxes"), new FaecherData("Betriebswirtschaft","Finanzmanagementkonzepte I"), new FaecherData("Betriebswirtschaft","Finanzmanagementkonzepte II"), new FaecherData("Betriebswirtschaft","Marketing- und Vertriebsmanagement I"), new FaecherData("Betriebswirtschaft","Marketing- und Vertriebsmanagement II"), new FaecherData("Betriebswirtschaft","Personalmanagement"), new FaecherData("Betriebswirtschaft","Steuern I "), new FaecherData("Betriebswirtschaft","Steuern II"), new FaecherData("Betriebswirtschaft","Wirtschaftsinformatik I "), new FaecherData("Betriebswirtschaft","Wirtschaftsinformatik II "), new FaecherData("Betriebswirtschaft","Beschaffung"), new FaecherData("Betriebswirtschaft","Logistik"), new FaecherData("Betriebswirtschaft","Rechnungslegung und Wirtschaftsprüfung I "), new FaecherData("Betriebswirtschaft","Rechnungslegung und Wirtschaftsprüfung II"), new FaecherData("Elekrotechnik","Ingenieurmathematik I"), new FaecherData("Elekrotechnik","Grundlagen der Elektrotechnik"), new FaecherData("Elekrotechnik","Informatik I"), new FaecherData("Elekrotechnik","Physik I"), new FaecherData("Elekrotechnik","Biomedizinische Grundlagen I"), new FaecherData("Elekrotechnik","Ingenieurmathematik II"), new FaecherData("Elekrotechnik","Elektronik und Messtechnik"), new FaecherData("Elekrotechnik","Informatik II"), new FaecherData("Elekrotechnik","Physik II"), new FaecherData("Elekrotechnik","Biomedizinische Grundlagen II"), new FaecherData("Elekrotechnik","Konstruktion und Entwicklung"), new FaecherData("Elekrotechnik","Mikrocomputertechnik"), new FaecherData("Elekrotechnik","Werkstoffe und Design in der Medizintechnik"), new FaecherData("Elekrotechnik","Grundlagen der Betriebswirtschaftslehre"), new FaecherData("Elekrotechnik","Sensorik in der Medizintechnik"), new FaecherData("Elekrotechnik","Marketing und Vertrieb"), new FaecherData("Elekrotechnik","Medizinische Bildverarbeitung"), new FaecherData("Elekrotechnik","Qualitätsmanagement in der Medizintechnik"), new FaecherData("Elekrotechnik","Grundlagen der medizinischen Bildgebung"), new FaecherData("Elekrotechnik","Projektmanagement"), new FaecherData("Elekrotechnik","Regelungstechnik"), new FaecherData("Elekrotechnik","Praktische Zeit im Betrieb"), new FaecherData("Elekrotechnik","Softwareentwicklung in der Medizintechnik"), new FaecherData("Elekrotechnik","Biosignalverarbeitung"), new FaecherData("Elekrotechnik","Minimalinvasive Verfahren"), new FaecherData("Elekrotechnik","Medizinische Optik und Lasertechnologie"), new FaecherData("Elekrotechnik","Grundlagen der medizinischen Gerätetechnik"), new FaecherData("Elekrotechnik","Krankenhausorganisation"), new FaecherData("Elekrotechnik","Systems Engineering in der Medizintechnik"), new FaecherData("Elekrotechnik","Beschaffung, Produktion und Logistik"), new FaecherData("Elekrotechnik","Technischer Einkauf"), new FaecherData("Elekrotechnik","Rechnergestützte Messtechnik"), new FaecherData("Elekrotechnik","Konstruktionsarbeit in der Medizintechnik"), new FaecherData("Elekrotechnik","Produktmanagement und Technischer Vertrieb"), new FaecherData("Elekrotechnik","Ingenieurmathematik I"), new FaecherData("Elekrotechnik","Elektrotechnik I"), new FaecherData("Elekrotechnik","Informatik I"), new FaecherData("Elekrotechnik","Technische Mechanik"), new FaecherData("Elekrotechnik","Ingenieurmathematik II"), new FaecherData("Elekrotechnik","Elektrotechnik II"), new FaecherData("Elekrotechnik","Informatik II "), new FaecherData("Elekrotechnik","Angewandte Physik"), new FaecherData("Elekrotechnik","Elektrotechnik III"), new FaecherData("Elekrotechnik","Elektrische Messtechnik"), new FaecherData("Elekrotechnik","Elektronische Bauelemente"), new FaecherData("Elekrotechnik","Digitaltechnik"), new FaecherData("Elekrotechnik","Informatik III "), new FaecherData("Elekrotechnik","Mikrocomputertechnik"), new FaecherData("Elekrotechnik","Schaltungstechnik"), new FaecherData("Elekrotechnik","Regelungstechnik I"), new FaecherData("Elekrotechnik","Grundlagen der Energietechnik"), new FaecherData("Elekrotechnik","Informatik IV"), new FaecherData("Elekrotechnik","Kommunikationstechnik"), new FaecherData("Elekrotechnik","Mikrocontroller mit Echtzeitbetriebssystemen"), new FaecherData("Elekrotechnik","Grundlagen elektrische Antriebe"), new FaecherData("Elekrotechnik","Regelungstechnik II"), new FaecherData("Elekrotechnik","Bussysteme"), new FaecherData("Elekrotechnik","Leistungselektronik"), new FaecherData("Elekrotechnik","Automatisierungstechnik"), new FaecherData("Elekrotechnik","Energieversorgung in der Gebäudetechnik"), new FaecherData("Elekrotechnik","Product Engineering in der Elektronikindustrie "), new FaecherData("Elekrotechnik","Sensorik"), new FaecherData("Elekrotechnik","Kommunikationssysteme"), new FaecherData("Elekrotechnik","Marketing und Vertrieb"), new FaecherData("Elekrotechnik","Projektarbeit in der Praxis"), new FaecherData("Elekrotechnik","Robotik"), new FaecherData("Elekrotechnik","Ingenieurmathematik I"), new FaecherData("Elekrotechnik","Grundlagen der Elektrotechnik"), new FaecherData("Elekrotechnik","Informatik I"), new FaecherData("Elekrotechnik","Technische Mechanik"), new FaecherData("Elekrotechnik","Grundlagen der Betriebs- und Volkswirtschaftslehre"), new FaecherData("Elekrotechnik","Ingenieurmathematik II"), new FaecherData("Elekrotechnik","Elektronik und Messtechnik"), new FaecherData("Elekrotechnik","Informatik II"), new FaecherData("Elekrotechnik","Angewandte Physik"), new FaecherData("Elekrotechnik","Regelungstechnik"), new FaecherData("Elekrotechnik","Grundlagen der Energiewirtschaft"), new FaecherData("Elekrotechnik","Grundlagen der Energietechnik"), new FaecherData("Elekrotechnik","Buchführung und Bilanzierung"), new FaecherData("Elekrotechnik","Excel und VBA-Anwendungen"), new FaecherData("Elekrotechnik","Marketing und Vertrieb"), new FaecherData("Elekrotechnik","Energierecht und Regulierung"), new FaecherData("Elekrotechnik","Kosten- und Leistungsrechnung"), new FaecherData("Elekrotechnik","Netztechnik und -führung"), new FaecherData("Elekrotechnik","Finanz- und Investitionswirtschaft"), new FaecherData("Elekrotechnik","Projektmanagement"), new FaecherData("Elekrotechnik","Grundlagen der Produktionstechnik"), new FaecherData("Elekrotechnik","Stromerzeugungstechnologien"), new FaecherData("Elekrotechnik","Gas- und Kommunalwirtschaft"), new FaecherData("Elekrotechnik","Energieeffizienz in Wohngebäuden"), new FaecherData("Elekrotechnik","Aktuelle Managementthemen der Energiewirtschaft und -technik"), new FaecherData("Elekrotechnik","Energie, Umwelt, Gesellschaft und Ethik"), new FaecherData("Elekrotechnik","Energiehandel und Marktmechanismen"), new FaecherData("Elekrotechnik","Energieeffizienz in Industrie und Gewerbe"), new FaecherData("Elekrotechnik","Energieberatung für Wohngebäude"), new FaecherData("Elekrotechnik","Technischer Einkauf"), new FaecherData("Elekrotechnik","Sensorik"), new FaecherData("Elekrotechnik","Automatisierungstechnik"), new FaecherData("Elekrotechnik","Rechnergestützte Messtechnik"), new FaecherData("Elekrotechnik","ERP-Systeme"), new FaecherData("Elekrotechnik","Controlling"), new FaecherData("Elekrotechnik","Geschäftsprozessmanagement"), new FaecherData("Elekrotechnik","Wirtschaftsprivatrecht"), new FaecherData("Elekrotechnik","Personalmanagement"), new FaecherData("Elekrotechnik","Produktions- und Prozessplanung"), new FaecherData("Elekrotechnik","Logistik- und Fabrikplanung"), new FaecherData("Elekrotechnik","Datenbanksysteme und -anwendungen"), new FaecherData("Elekrotechnik","Sensorik"), new FaecherData("Elekrotechnik","Ingenieurmathematik I"), new FaecherData("Elekrotechnik","Grundlagen der Elektrotechnik"), new FaecherData("Elekrotechnik","Informatik I"), new FaecherData("Elekrotechnik","Technische Mechanik"), new FaecherData("Elekrotechnik","Principles of Business Administration and Economics"), new FaecherData("Elekrotechnik","Ingenieurmathematik II"), new FaecherData("Elekrotechnik","Elektronik und Messtechnik "), new FaecherData("Elekrotechnik","Informatik II"), new FaecherData("Elekrotechnik","Applied Physics"), new FaecherData("Elekrotechnik","Software-Tools"), new FaecherData("Elekrotechnik","Buchführung und Bilanzierung"), new FaecherData("Elekrotechnik","Prozessoptimierung und statistische Qualitätssicherung"), new FaecherData("Elekrotechnik","Marketing and Sales"), new FaecherData("Elekrotechnik","Konstruktion und Entwicklung"), new FaecherData("Elekrotechnik","Kosten- und Leistungsrechnung"), new FaecherData("Elekrotechnik","Procurement, Manufacturing and Logistics"), new FaecherData("Elekrotechnik","Grundlagen der Produktionstechnik"), new FaecherData("Elekrotechnik","Finanz- und Investitionswirtschaft"), new FaecherData("Elekrotechnik","Project Management"), new FaecherData("Elekrotechnik","International Business and Cross-Cultural Communication"), new FaecherData("Elekrotechnik","Energieversorgung in der Gebäudetechnik"), new FaecherData("Elekrotechnik","Internettechnologien"), new FaecherData("Elekrotechnik","Automatisierungstechnik "), new FaecherData("Elekrotechnik","Telekommunikation"), new FaecherData("Elekrotechnik","Rechnergestützte Messtechnik"), new FaecherData("Elekrotechnik","Mikrocomputertechnik"), new FaecherData("Elekrotechnik","Batteriespeicher"), new FaecherData("Elekrotechnik","Unternehmensplanspiel"), new FaecherData("Elekrotechnik","ERP-Systeme"), new FaecherData("Elekrotechnik","Controlling "), new FaecherData("Elekrotechnik","Geschäftsprozessmanagement "), new FaecherData("Elekrotechnik","Wirtschaftsprivatrecht "), new FaecherData("Elekrotechnik","Personalmanagement"), new FaecherData("Elekrotechnik","Product Engineering in der Elektroindustrie"), new FaecherData("Elekrotechnik","Produktions- und Prozessplanung"), new FaecherData("Elekrotechnik","Logistik- und Fabrikplanung"), new FaecherData("Elekrotechnik","Datenbanksysteme und -anwendungen"), new FaecherData("Elekrotechnik","Qualitätsmanagement "), new FaecherData("Elekrotechnik","Technischer Einkauf"), new FaecherData("Elekrotechnik","Produktmanagement und Technischer Vertrieb"), new FaecherData("Elekrotechnik","Internationale Beschaffung"), new FaecherData("Elekrotechnik","International Production Networks and Logistik"), new FaecherData("Elekrotechnik","Ingenieurmathematik I"), new FaecherData("Elekrotechnik","Grundlagen der Elektrotechnik"), new FaecherData("Elekrotechnik","Informatik I"), new FaecherData("Elekrotechnik","Technische Mechanik"), new FaecherData("Elekrotechnik","Grundlagen der Betriebs- und Volkswirtschaftslehre"), new FaecherData("Elekrotechnik","Ingenieurmathematik II"), new FaecherData("Elekrotechnik","Elektronik und Messtechnik "), new FaecherData("Elekrotechnik","Informatik II"), new FaecherData("Elekrotechnik","Angewandte Physik"), new FaecherData("Elekrotechnik","Energiewirtschaft"), new FaecherData("Elekrotechnik","Regelungstechnik"), new FaecherData("Elekrotechnik","Buchführung und Bilanzierung"), new FaecherData("Elekrotechnik","Marketing und Vertrieb"), new FaecherData("Elekrotechnik","Grundlagen der Produktionstechnik"), new FaecherData("Elekrotechnik","Konstruktion und Entwicklung"), new FaecherData("Elekrotechnik","Kosten- und Leistungsrechnung"), new FaecherData("Elekrotechnik","Beschaffung, Produktion und Logistik"), new FaecherData("Elekrotechnik","Finanz- und Investitionswirtschaft"), new FaecherData("Elekrotechnik","Energieversorgung in der Gebäudetechnik"), new FaecherData("Elekrotechnik","Sensorik"), new FaecherData("Elekrotechnik","Internettechnologien"), new FaecherData("Elekrotechnik","Automatisierungstechnik "), new FaecherData("Elekrotechnik","Telekommunikation "), new FaecherData("Elekrotechnik","Rechnergestützte Messtechnik"), new FaecherData("Elekrotechnik","Batteriespeicher"), new FaecherData("Elekrotechnik","Mikrocomputertechnik"), new FaecherData("Elekrotechnik","Geschäftsprozessmanagement "), new FaecherData("Elekrotechnik","Wirtschaftsprivatrecht"), new FaecherData("Elekrotechnik","Personalmanagement"), new FaecherData("Elekrotechnik","Produktions- und Prozessplanung"), new FaecherData("Elekrotechnik","Logistik- und Fabrikplanung"), new FaecherData("Elekrotechnik","Datenbanksysteme und -anwendungen"), new FaecherData("Elekrotechnik","Qualitätsmanagement "), new FaecherData("Elekrotechnik","Technischer Einkauf"), new FaecherData("Elekrotechnik","Produktmanagement und Technischer Vertrieb"), new FaecherData("Soziale Arbeit","Soziale Arbeit"), new FaecherData("Soziale Arbeit","Menschliches Verhalten, Entwicklung, Erziehung und Bildung"), new FaecherData("Soziale Arbeit","Theorien und Organisationen der Sozialen Arbeit"), new FaecherData("Soziale Arbeit","Gesellschaft und Politik"), new FaecherData("Soziale Arbeit","Strukturen des Rechts"), new FaecherData("Soziale Arbeit","Propädeutikum"), new FaecherData("Soziale Arbeit","Handlungskompetenz - Basisstrategien"), new FaecherData("Soziale Arbeit","Wissenschaft und Praxis Sozialer Arbeit"), new FaecherData("Soziale Arbeit","Sozialwissenschaftliche Forschung: Methoden und Projekte"), new FaecherData("Soziale Arbeit","Sozialleistungsrecht und Formen des Zusammenlebens"), new FaecherData("Soziale Arbeit","Handlungskompetenz Differenzielle Methoden"), new FaecherData("Soziale Arbeit","Soziale Arbeit und Kultur Kulturelle Differenzen"), new FaecherData("Soziale Arbeit","Soziale Arbeit und Gesundheit Theoretische Zugänge"), new FaecherData("Soziale Arbeit","Soziale Arbeit und sozialer Raum Theoretische Zugänge"), new FaecherData("Soziale Arbeit","Soziale Arbeit und soziale Ungleichheit Theoretische Zugänge"), new FaecherData("Soziale Arbeit","Soziale Arbeit und Kultur Interkulturelle Kompetenzen"), new FaecherData("Soziale Arbeit","Soziale Arbeit und Gesundheit Methoden Klinischer Sozialarbeit"), new FaecherData("Soziale Arbeit","Soziale Arbeit und sozialer Raum Methoden sozialräumlichen Arbeitens"), new FaecherData("Soziale Arbeit","Soziale Arbeit und soziale Ungleichheit Methoden im Umgang mit sozialer"), new FaecherData("Soziale Arbeit","Ungleichheit"), new FaecherData("Soziale Arbeit","Forschungs- und Entwicklungswerkstätten"), new FaecherData("Soziale Arbeit","Soziale Arbeit und Kultur Interkulturelle und gendersensible Praxis"), new FaecherData("Soziale Arbeit","Soziale Arbeit und Gesundheit Anwendungsfelder Klinischer Sozialarbeit"), new FaecherData("Soziale Arbeit","Soziale Arbeit und sozialer Raum Anwendungsfelder"), new FaecherData("Soziale Arbeit","Soziale Arbeit und soziale Ungleichheit Anwendungsfelder"), new FaecherData("Soziale Arbeit","Handlungskompetenz - Diagnostik und Fallarbeit"), new FaecherData("Informatik","Grundlagen der Informatik"), new FaecherData("Informatik","Grundlagen der theoretischen Informatik"), new FaecherData("Informatik","Digitaltechnik"), new FaecherData("Informatik","Mathematik 1"), new FaecherData("Informatik","Mathematik 2"), new FaecherData("Informatik","Software Engineering 1"), new FaecherData("Informatik","Englisch"), new FaecherData("Informatik","Programmieren 1"), new FaecherData("Informatik","Programmieren 2"), new FaecherData("Informatik","Programmieren 3"), new FaecherData("Informatik","Software Engineering 2"), new FaecherData("Informatik","Datenbanken"), new FaecherData("Informatik","Rechnerarchitektur"), new FaecherData("Informatik","Algorithmen und Datenstrukturen"), new FaecherData("Informatik","Grundlagen der Betriebswirtschaftslehre"), new FaecherData("Informatik","IT Sicherheit"), new FaecherData("Informatik","Betriebssysteme"), new FaecherData("Informatik","Datenkommunikation"), new FaecherData("Informatik","Statistik"), new FaecherData("Informatik","Präsentation und Kommunikation"), new FaecherData("Informatik","Praktikum"), new FaecherData("Informatik","Praxisseminar"), new FaecherData("Informatik","IT-Recht"), new FaecherData("Informatik","Grundlagen Projektmanagement/Projektcontrolling"), new FaecherData("Informatik","Numerik"), new FaecherData("Informatik","Compiler"), new FaecherData("Informatik","Verteilte System"), new FaecherData("Informatik","Internettechnologien"), new FaecherData("Informatik","Prozessrechentechnik"), new FaecherData("Informatik","Grundlagen der Wirtschaftsinformatik"), new FaecherData("Informatik","BWL Basismodul (Teil: Einführung BW"), new FaecherData("Informatik","BWL Basismodul (Teil: Buchführung"), new FaecherData("Informatik","Software Engineering 1"), new FaecherData("Informatik","Finanzen und Investition"), new FaecherData("Informatik","Englisch"), new FaecherData("Informatik","Software Engineering 2"), new FaecherData("Informatik","Datenbanken"), new FaecherData("Informatik","Statistik"), new FaecherData("Informatik","Geschäftsprozesse und Organisation"), new FaecherData("Informatik","Algorithmen und Datenstrukturen"), new FaecherData("Informatik","IT-Infrastrukturen"), new FaecherData("Informatik","Kosten- und Leistungsrechnung"), new FaecherData("Informatik","Material und Fertigungswirtschaft/Logistik"), new FaecherData("Informatik","Operations Research"), new FaecherData("Informatik","IT-Compliance und IT-Recht"), new FaecherData("Informatik","Praxisorientiertes Studienprojekt"), new FaecherData("Informatik","IT-Projektmanagement"), new FaecherData("Informatik","Praktische Zeit im Betrieb"), new FaecherData("Informatik","Praxisseminar"), new FaecherData("Informatik","Internettechnologien"), new FaecherData("Informatik","Software Engineering 3"), new FaecherData("Informatik","IT-Sicherheit"), new FaecherData("Informatik","IT-Management und -Controlling"), new FaecherData("Informatik","Unternehmenssoftware (ERP-System"), new FaecherData("Informatik","Controlling-Praxis im Unternehmen"), new FaecherData("Informatik","Doing Business in Russia"), new FaecherData("Informatik","Revenue Management"), new FaecherData("Informatik","Informations- und Metamodellierung"), new FaecherData("Informatik","Enterprise Computing"), new FaecherData("Informatik","Sicherheit mobiler System"), new FaecherData("Informatik","Internet of Things (IoT"), new FaecherData("Informatik","Innovationslaber (IoT-Projekt"), new FaecherData("Informatik","Mobile Business"), new FaecherData("Informatik","Wahlpflichtmodul BW"), new FaecherData("Informatik","Wahlpflichtmodul IF"), new FaecherData("Informatik","Dirigieren"), new FaecherData("Informatik","Digitaltechnik"), new FaecherData("Informatik","Grundlagen der Elektrotechnik"), new FaecherData("Informatik","Technische Mechanik"), new FaecherData("Informatik","Software Engineering"), new FaecherData("Informatik","Ingenieurmathematik 1"), new FaecherData("Informatik","Ingenieurmathematik 2"), new FaecherData("Informatik","Elektronik und Messtechnik"), new FaecherData("Informatik","Angewandte Physik"), new FaecherData("Informatik","Datenbanken"), new FaecherData("Informatik","Regelungstechnik"), new FaecherData("Informatik","Konstruktion und Entwicklung"), new FaecherData("Informatik","Rechnerarchitektur"), new FaecherData("Informatik","Algorithmen und Datenstrukturen"), new FaecherData("Informatik","Grundlagen der Automobiltechnik"), new FaecherData("Informatik","Fahrwerktechnik"), new FaecherData("Informatik","Antriebstechnik"), new FaecherData("Informatik","Prozessrechentechnik"), new FaecherData("Informatik","Karosserietechnik"), new FaecherData("Maschinenbau","Naturwissenschaftliche Grundlagen "), new FaecherData("Maschinenbau","Maschinenkonstruktion I "), new FaecherData("Maschinenbau","Wirtschaftliche und soziale Kompetenzen "), new FaecherData("Maschinenbau","Ingenieurmathematik"), new FaecherData("Maschinenbau","Werkstoffkunde"), new FaecherData("Maschinenbau","Technische Mechanik "), new FaecherData("Maschinenbau","Grundlagen Ingenieurinformatik"), new FaecherData("Maschinenbau","Festigkeitslehre"), new FaecherData("Maschinenbau","Ingenieurmathematik"), new FaecherData("Maschinenbau","Grundlagen, Elektrotechnik und Elektronik"), new FaecherData("Maschinenbau","Grundlagen Fertigungstechnik"), new FaecherData("Maschinenbau","Versuchstechnik "), new FaecherData("Maschinenbau","Strömungsmechanik "), new FaecherData("Maschinenbau","Technische Thermodynamik"), new FaecherData("Maschinenbau","Grundlagen CAD/FEM"), new FaecherData("Maschinenbau","Steuerungs- und Regelungstechnik"), new FaecherData("Maschinenbau","Maschinenkonstruktion II"), new FaecherData("Maschinenbau","Elektrische Antriebe und Getriebetechnik"), new FaecherData("Maschinenbau","Grundlagen Leichtbau"), new FaecherData("Maschinenbau","Produktionsmanagement"), new FaecherData("Maschinenbau","Umwelttechnik "), new FaecherData("Maschinenbau","Werkstoffe und Betriebsfestigkeit "), new FaecherData("Maschinenbau","Werkzeugmaschinen und Automatisierungstechnik"), new FaecherData("Maschinenbau","Wärme- und Fluidtechnik "), new FaecherData("Maschinenbau","Gießereitechnik und Schweißtechnik"), new FaecherData("Maschinenbau","Entwicklung dynamischer Systeme "), new FaecherData("Maschinenbau","Energietechnik 1 "), new FaecherData("Maschinenbau","Energietechnik 2 "), new FaecherData("Maschinenbau","Energietechnik 3 "), new FaecherData("Maschinenbau","Energie-/Umweltmanagement"), new FaecherData("Maschinenbau","Energiewirtschaft/Energieeffizienz"), new FaecherData("Maschinenbau","Vertiefende Fertigungstechnik 1"), new FaecherData("Maschinenbau","Werkzeugmaschinen und Automatisierungstechnik"), new FaecherData("Maschinenbau","Qualitätsmanagement und Unternehmensführung "), new FaecherData("Maschinenbau","Vertiefende Fertigungstechnik 2 "), new FaecherData("Maschinenbau","Produktionslogistik und Investitionsmanagement"), new FaecherData("Maschinenbau","Konstruktionswerkstoffe für den Leichtbau"), new FaecherData("Maschinenbau","Leichtbaustrukturen"), new FaecherData("Maschinenbau","Wärme- und Fluidtechnik"), new FaecherData("Maschinenbau","Fertigungstechnologien für den Leichtbau"), new FaecherData("Maschinenbau","Entwicklung dynamischer Systeme"), new FaecherData("Maschinenbau","Faserverbundwerkstoffe"), new FaecherData("Maschinenbau","Prozesseffizienz und Ressourcenmanagement in der Fertigung "), new FaecherData("Maschinenbau","Stoffstrommanagement und Abfallwirtschaft"), new FaecherData("Maschinenbau","Industriemarketing und technische Betriebsführung"), new FaecherData("Maschinenbau","Vertiefung CAD "), new FaecherData("Maschinenbau","Strömungsmaschinen"), new FaecherData("Maschinenbau","Ingenieurmathematik"), new FaecherData("Maschinenbau","Ingenieurinformatik"), new FaecherData("Maschinenbau","Naturwissenschaftliche Grundlagen"), new FaecherData("Maschinenbau","Materialkunde"), new FaecherData("Maschinenbau","Technische Mechanik I "), new FaecherData("Maschinenbau","Technische Mechanik II "), new FaecherData("Maschinenbau","Maschinenkonstruktion I "), new FaecherData("Maschinenbau","Maschinenkonstruktion II"), new FaecherData("Maschinenbau","Elektro- und Messtechnik "), new FaecherData("Maschinenbau","Grundlagen der Fertigungstechnik"), new FaecherData("Maschinenbau","Interdisziplinäre Fächer"), new FaecherData("Maschinenbau","Grundlagen der Energietechnik "), new FaecherData("Maschinenbau","Konstruktion und CAD"), new FaecherData("Maschinenbau","Finite Elemente"), new FaecherData("Maschinenbau","Automatisierungs- und Versuchstechnik"), new FaecherData("Maschinenbau","Werkstoffe und Leichtbau I "), new FaecherData("Maschinenbau","Konstruktionsarbeit"), new FaecherData("Maschinenbau","Erweiterte Energietechnik"), new FaecherData("Maschinenbau","Werkstoffe und Leichtbau II"), new FaecherData("Maschinenbau","Ingenieurwissenschaftliche Praktika"), new FaecherData("Maschinenbau","Leichtbaumechanik"), new FaecherData("Maschinenbau","Prozesstechnologie im Strukturleichtbau"), new FaecherData("Maschinenbau","Naturwissenschaftliche Grundlagen"), new FaecherData("Maschinenbau","Maschinenkonstruktion I"), new FaecherData("Maschinenbau","Wirtschaftliche und soziale Kompetenzen "), new FaecherData("Maschinenbau","Ingenieurmathematik"), new FaecherData("Maschinenbau","Werkstoffkunde"), new FaecherData("Maschinenbau","Technische Mechanik"), new FaecherData("Maschinenbau","Grundlagen Ingenieurinformatik"), new FaecherData("Maschinenbau","Festigkeitslehre"), new FaecherData("Maschinenbau","Maschinenelemente"), new FaecherData("Maschinenbau","Grundlagen  Elektrotechnik und Elektronik"), new FaecherData("Maschinenbau","Grundlagen Fertigungstechnik"), new FaecherData("Maschinenbau","Versuchstechnik"), new FaecherData("Maschinenbau","Strömungsmechanik"), new FaecherData("Maschinenbau","Technische Thermodynamik"), new FaecherData("Maschinenbau","Grundlagen CAD/FEM"), new FaecherData("Maschinenbau","Steuerungs- und Regelungstechnik"), new FaecherData("Maschinenbau","Maschinenkonstruktion II"), new FaecherData("Maschinenbau","Verbrennungsmotoren"), new FaecherData("Maschinenbau","Praktisches Studiensemester"), new FaecherData("Maschinenbau","Projektarbeit"), new FaecherData("Maschinenbau","Ingenieurtechnisches Praktikum"), new FaecherData("Maschinenbau","Fahrzeuginformatik"), new FaecherData("Maschinenbau","Automobiltechnik I"), new FaecherData("Maschinenbau","Grundlagen der Antriebstechnik"), new FaecherData("Maschinenbau","Grundlagen der Fahrzeugmechatronik"), new FaecherData("Maschinenbau","Automobiltechnik II"), new FaecherData("Maschinenbau","Fahrzeuginformatik"), new FaecherData("Maschinenbau","Grundlagen der Antriebstechnik"), new FaecherData("Maschinenbau","Grundlagen moderner NFZ"), new FaecherData("Maschinenbau","Moderne NFZ Technik I"), new FaecherData("Maschinenbau","Moderne NFZ Technik II"), new FaecherData("Maschinenbau","Fahrzeuginformatik"), new FaecherData("Maschinenbau","Grundlagen der Antriebstechnik"), new FaecherData("Maschinenbau","Grundlagen der Fahrzeugmechatronik"), new FaecherData("Maschinenbau","Alternative Antriebstechniken"), new FaecherData("Maschinenbau","Entwicklung dynamischer Systeme"), new FaecherData("Maschinenbau","Qualitätsmanagement und Unternehmensführung"), new FaecherData("Maschinenbau","Konstruktionswerkstoffe für den Leichtbau"), new FaecherData("Maschinenbau","Leichtbaustrukturen"));
                preference.edit().putBoolean("Profs inserted",true);
            }
            return null;
        }
    }
}
