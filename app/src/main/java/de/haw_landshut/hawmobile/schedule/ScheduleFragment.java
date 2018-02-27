package de.haw_landshut.hawmobile.schedule;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.*;

import android.widget.*;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import de.haw_landshut.hawmobile.Fakultaet;
import de.haw_landshut.hawmobile.MainActivity;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.CustomTimetable;
import de.haw_landshut.hawmobile.base.FaecherData;
import de.haw_landshut.hawmobile.base.ProfData;
import de.haw_landshut.hawmobile.base.ScheduleDao;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
    private static final int ENTRYCOUNT = 60;
    private static final int BASICCOLOR = 16777215;
    private View.OnClickListener ocl;
    public static BottomSheetBehavior mBottomSheetBehavior1;
    View bottomSheet;
    public static TextView currentDate,currentWeek;
    public static TextView currentTV;
    public static EditText et_fach;
    public static EditText et_prof;
    public static EditText et_raum;
    public static List<CustomTimetable> timetable;
    public static ScheduleDao scheduleDao = MainActivity.getHawDatabase().scheduleDao();
    private TextView[][] elements;
    protected static boolean isEven;
    private boolean checkDouble;
    private int colormaker;


    Button edit;
    Button save;
    Button cancel;
    Button color;
   protected static CheckBox wöchentl;


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

        ocl = new OnClickLabel();
        preference=getActivity().getPreferences(Context.MODE_PRIVATE);
        this.setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        edit = view.findViewById(R.id.btn_edit);
        save = view.findViewById(R.id.btn_save);
        cancel = view.findViewById(R.id.btn_cancel);
        color = view.findViewById(R.id.colorPicker);
        wöchentl = view.findViewById(R.id.wöchentlCheckbox);
        et_fach = view.findViewById(R.id.et_fach);
        et_prof = view.findViewById(R.id.et_prof);
        et_raum = view.findViewById(R.id.et_raum);
        bottomSheet = view.findViewById(R.id.bottom_sheet1);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);

        //sets the current date and week
        currentDate = view.findViewById(R.id.schedule_textView_currentDate);
        GregorianCalendar now = new GregorianCalendar();
        DateFormat df= DateFormat.getDateInstance(DateFormat.SHORT);
        currentDate.setText(df.format(now.getTime()));

        currentWeek=view.findViewById(R.id.schedule_textView_currentWeek);

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int num_week = c.get(Calendar.WEEK_OF_YEAR);
        Log.d("KW:", num_week+"");
        if(num_week%2==0){
            currentWeek.setText("gerade");
            isEven=true;
        }
        else{
            currentWeek.setText("ungerade");
            isEven=false;
        }

        currentWeek.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isEven){
                    currentWeek.setText("ungerade");
                    isEven=false;
                    new BeginnInsertion().execute();
                }
                else{
                    currentWeek.setText("gerade");
                    isEven=true;
                    new BeginnInsertion().execute();
                }
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    cancel.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                    color.setVisibility(View.VISIBLE);
                    wöchentl.setVisibility(View.VISIBLE);
                    et_fach.setEnabled(true);
                    et_prof.setEnabled(true);
                    et_raum.setEnabled(true);
                    checkDouble=wöchentl.isChecked();

                }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    cancel.setVisibility(View.INVISIBLE);
                    save.setVisibility(View.GONE);
                    edit.setVisibility(View.VISIBLE);
                    color.setVisibility(View.GONE);
                    wöchentl.setVisibility(View.INVISIBLE);
                    et_fach.setEnabled(false);
                    et_prof.setEnabled(false);
                    et_raum.setEnabled(false);
                    currentTV.setText(et_fach.getText());
                    currentTV.setBackgroundColor(colormaker);
                    Log.d("Test","COLOR:"+colormaker);

                    int currentHour = Integer.parseInt(currentTV.getTag().toString());
                    CustomTimetable table;
                    if(wöchentl.isChecked()){
                        table = new CustomTimetable(currentHour,et_prof.getText().toString(),et_fach.getText().toString(),et_raum.getText().toString(),colormaker);
                        new UpdateTimetable().execute(table);
                        currentHour = currentHour+(ENTRYCOUNT/2);
                        table=new CustomTimetable(currentHour,et_prof.getText().toString(),et_fach.getText().toString(),et_raum.getText().toString(),colormaker);
                        new UpdateTimetable().execute(table);
                    }
                    else {
                        if(checkDouble){
                            if(isEven){
                                currentHour = currentHour + (ENTRYCOUNT/2);
                                table=new CustomTimetable(currentHour,"","","",BASICCOLOR);
                                new UpdateTimetable().execute(table);
                            }
                            else{
                                table=new CustomTimetable(currentHour,"","","",BASICCOLOR);
                                new UpdateTimetable().execute(table);
                            }
                        }
                        else{
                            if (!isEven) {
                                currentHour = currentHour + (ENTRYCOUNT / 2);
                            }
                            table = new CustomTimetable(currentHour, et_prof.getText().toString(), et_fach.getText().toString(), et_raum.getText().toString(),colormaker);
                            new UpdateTimetable().execute(table);
                        }


                    }
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
                color.setVisibility(View.GONE);
                wöchentl.setVisibility(View.INVISIBLE);
                ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        color.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ColorPickerDialogBuilder
                        .with(view.getContext())
                        .setTitle("Farbe auswählen")
                        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                        .density(3)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int i) {

                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, Integer[] integers) {
                                colormaker=i;

                            }
                        })
                        .setNegativeButton("abbrechen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .build()
                        .show();

            }

        });

        //wöchentl.setOnClickListener();


            return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().setTitle(R.string.schedule);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        new BeginnInsertion().execute();
        super.onActivityCreated(savedInstanceState);
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
    private class UpdateTimetable extends AsyncTask<CustomTimetable,Void,Void>{
        @Override
        protected Void doInBackground(CustomTimetable... customTimetables){
            ScheduleFragment.scheduleDao.updateTimetable(customTimetables[0]);
            ScheduleFragment.timetable = ScheduleFragment.scheduleDao.getTimetable();
            return null;
        }
    }

    private class BeginnInsertion extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... views) {
            if(!preference.getBoolean("Profs inserted",false)) {
                ScheduleFragment.scheduleDao.insertAlleProfs(new ProfData("Johannes", "Busse"), new ProfData("Matthias", "Dorfner"), new ProfData("Ludwig", "Griebl"), new ProfData("Peter", "Hartmann"), new ProfData("Wolfgang", "Jürgensen"), new ProfData("Abdelmajid", "Khelil"), new ProfData("Monika", "Messerer"), new ProfData("Markus", "Mock"), new ProfData("Dieter", "Nazareth"), new ProfData("Martin", "Pellkofer"), new ProfData("Gudrun", "Schiedermeier"), new ProfData("Peter", "Scholz"), new ProfData("Christian", "See"), new ProfData("Andreas", "Siebert"), new ProfData("Johann", "Uhrmann"), new ProfData("Jürgen", "Wunderlich"), new ProfData("Thomas", "Franzke"), new ProfData("Michael", "Bürker"), new ProfData("Patrick", "Dieses"), new ProfData("Marcus", "Fischer"), new ProfData("Dieter", "Greipl"), new ProfData("Sandra", "Gronover"), new ProfData("Michael", "Gumbsheimer"), new ProfData("Burkhard", "Jaeger"), new ProfData("Alexander", "Kumpf"), new ProfData("Michael", "Leckebusch"), new ProfData("Maren", "Martens"), new ProfData("Bernd", "Mühlfriedel"), new ProfData("Martin", "Prasch"), new ProfData("Heinz-Werner", "Schuster"), new ProfData("Hanns", "Robby"), new ProfData("Valentina", "Speidel"), new ProfData("Thomas", "Stauffert"), new ProfData("Karl", "Stoffel"), new ProfData("Manuel", "Strunz"), new ProfData("Thomas", "Zinser"), new ProfData("Stefan-Alexander", "Arlt"), new ProfData("Andrea", "Badura"), new ProfData("Andreas", "Breidenassel"), new ProfData("Petra", "Denk"), new ProfData("Andreas", "Dieterle"), new ProfData("Guido", "Dietl"), new ProfData("Armin", "Englmaier"), new ProfData("Christian", "Faber"), new ProfData("Thomas", "Faldum"), new ProfData("Jürgen", "Gebert"), new ProfData("Jürgen", "Giersch"), new ProfData("Michaela", "Gruber"), new ProfData("Artem", "Ivanov"), new ProfData("Johann", "Jaud"), new ProfData("Benedict", "Kemmerer"), new ProfData("Alexander", "Kleimaier"), new ProfData("Carl-Gustaf", "Kligge"), new ProfData("Dieter", "Koller"), new ProfData("Raimund", "Kreis"), new ProfData("Jörg", "Mareczek"), new ProfData("Sebastian", "Meißner"), new ProfData("Fritz", "Pörnbacher"), new ProfData("Mathias", "Rausch"), new ProfData("Stefanie", "Remmele"), new ProfData("Goetz", "Roderer"), new ProfData("Carsten", "Röh"), new ProfData("Magda", "Schiegl"), new ProfData("Markus", "Schmitt"), new ProfData("Markus", "Schneider"), new ProfData("Martin", "Soika"), new ProfData("Peter", "Spindler"), new ProfData("Reimer", "Studt"), new ProfData("Holger", "Timinger"), new ProfData("Klaus", "Timmer"), new ProfData("Petra", "Tippmann-Krayer"), new ProfData("Hubertus", "C."), new ProfData("Jürgen", "Welter"), new ProfData("Thomas", "Wolf"), new ProfData("Norbert", "Babel"), new ProfData("Walter", "Fischer"), new ProfData("Martin", "Förg"), new ProfData("Bernhard", "Gubanka"), new ProfData("Diana", "Hehenberger-Risse"), new ProfData("Josef", "Hofmann"), new ProfData("Peter", "Holbein"), new ProfData("Barbara", "Höling"), new ProfData("Otto", "Huber"), new ProfData("Marcus", "Jautze"), new ProfData("Hubert", "Klaus"), new ProfData("Jan", "Köll"), new ProfData("Detlev", "Maurer"), new ProfData("Karl-Heinz", "Pettinger"), new ProfData("Franz", "Prexler"), new ProfData("Ralph", "Pütz"), new ProfData("Karl", "Reiling"), new ProfData("Wolfgang", "Reimann"), new ProfData("Tim", "Rödiger"), new ProfData("Sven", "Roeren"), new ProfData("Holger", "Saage"), new ProfData("Manfred", "Strohe"), new ProfData("Volker", "Weinbrenner"), new ProfData("Sigrid", "A."), new ProfData("Hubert", "Beste"), new ProfData("Stefan", "Borrmann"), new ProfData("Clemens", "Dannenbeck"), new ProfData("Christoph", "Fedke"), new ProfData("Bettina", "Kühbeck"), new ProfData("Katrin", "Liel"), new ProfData("Johannes", "Lohner"), new ProfData("Dominique", "Moisl"), new ProfData("Karin", "E."), new ProfData("Maria", "Ohling"), new ProfData("Mihri", "Özdoğan"), new ProfData("Andreas", "Panitz"), new ProfData("Barbara", "Thiessen"), new ProfData("Ralph", "Viehhauser"), new ProfData("Mechthild", "Wolff"), new ProfData("Eva", "Wunderer"));
                preference.edit().putBoolean("Profs inserted",true).apply();
            }
           if(!preference.getBoolean("Subjekts inserted",false)){
                ScheduleFragment.scheduleDao.insertAlleFaecher(new FaecherData(Fakultaet.BW,"Wirtschaftsmathematik"), new FaecherData(Fakultaet.BW,"Statistik"), new FaecherData(Fakultaet.BW,"Volkswirtschaftslehre I Mikroökonomie"), new FaecherData(Fakultaet.BW,"Volkswirtschaftslehre II Makroökonomie"), new FaecherData(Fakultaet.BW,"Einführung in die Betriebswirtschaftslehre"), new FaecherData(Fakultaet.BW,"Externes Rechnungswesen"), new FaecherData(Fakultaet.BW,"Kosten- und Leistungsrechnung"), new FaecherData(Fakultaet.BW,"Informationstechnologie"), new FaecherData(Fakultaet.BW,"Wirtschaftsenglisch"), new FaecherData(Fakultaet.BW,"Foreign Business Language II"), new FaecherData(Fakultaet.BW,"Principles of Marketing and Sales"), new FaecherData(Fakultaet.BW,"Principles of Human Resource Management"), new FaecherData(Fakultaet.BW,"Principles of Operations and Logistics Management"), new FaecherData(Fakultaet.BW,"Principles of Finance and Investment"), new FaecherData(Fakultaet.BW,"Principles of International Management"), new FaecherData(Fakultaet.BW,"Principles of Organisation"), new FaecherData(Fakultaet.BW,"European Law"), new FaecherData(Fakultaet.BW,"Foreign Business Language II (Teil 2)"), new FaecherData(Fakultaet.BW,"Business Administration Seminar"), new FaecherData(Fakultaet.BW,"Digital Business Models"), new FaecherData(Fakultaet.BW,"Fundamentals of International Economics"), new FaecherData(Fakultaet.BW,"Specialised Compulsory Elective Module"), new FaecherData(Fakultaet.BW,"Arbeitsrecht"), new FaecherData(Fakultaet.BW,"Designing The Global Marketing Programme"), new FaecherData(Fakultaet.BW,"Doing Business in Russia"), new FaecherData(Fakultaet.BW,"Management Accounting and Control"), new FaecherData(Fakultaet.BW,"Optimization in Logistics"), new FaecherData(Fakultaet.BW,"Private Wealth Management"), new FaecherData(Fakultaet.BW,"Wirtschaftsprivatrecht/Gesellschaftsrecht"), new FaecherData(Fakultaet.BW,"Ingenieurmathematik I"), new FaecherData(Fakultaet.BW,"Grundlagen der Elektrotechnik"), new FaecherData(Fakultaet.BW,"Informatik I"), new FaecherData(Fakultaet.BW,"Technische Mechanik"), new FaecherData(Fakultaet.BW,"Grundlagen der Betriebs- und Volkswirtschaftslehre"), new FaecherData(Fakultaet.BW,"Ingenieurmathematik II"), new FaecherData(Fakultaet.BW,"Elektronik und Messtechnik"), new FaecherData(Fakultaet.BW,"Informatik II"), new FaecherData(Fakultaet.BW,"Angewandte Physik"), new FaecherData(Fakultaet.BW,"Konstruktion und Entwicklung"), new FaecherData(Fakultaet.BW,"Regelungstechnik"), new FaecherData(Fakultaet.BW,"Mikrocomputertechnik"), new FaecherData(Fakultaet.BW,"Buchführung und Bilanzierung"), new FaecherData(Fakultaet.BW,"Grundlagen der Automobilwirtschaft"), new FaecherData(Fakultaet.BW,"Marketing und Vertrieb"), new FaecherData(Fakultaet.BW,"Kosten- und Leistungsrechnung"), new FaecherData(Fakultaet.BW,"Beschaffung, Produktion und Logistik"), new FaecherData(Fakultaet.BW,"Finanz- und Investitionswirtschaft"), new FaecherData(Fakultaet.BW,"Projektmanagement"), new FaecherData(Fakultaet.BW,"Grundlagen der Produktionstechnik"), new FaecherData(Fakultaet.BW,"Unternehmensplanspiel"), new FaecherData(Fakultaet.BW,"Wirtschaftsprivatrecht"), new FaecherData(Fakultaet.BW,"Produktions- und Prozessplanung"), new FaecherData(Fakultaet.BW,"Logistik- und Fabrikplanung"), new FaecherData(Fakultaet.BW,"Qualitätsmanagement"), new FaecherData(Fakultaet.BW,"Technischer Einkauf"), new FaecherData(Fakultaet.BW,"Wirtschaftsmathematik"), new FaecherData(Fakultaet.BW,"Statistik"), new FaecherData(Fakultaet.BW,"Volkswirtschaftslehre I Mikroökonomie"), new FaecherData(Fakultaet.BW,"Volkswirtschaftslehre II Makroökonomie"), new FaecherData(Fakultaet.BW,"Einführung in die Betriebswirtschaftslehre"), new FaecherData(Fakultaet.BW,"Externes Rechnungswesen"), new FaecherData(Fakultaet.BW,"Kosten- und Leistungsrechnung"), new FaecherData(Fakultaet.BW,"Informationstechnologie"), new FaecherData(Fakultaet.BW,"Wirtschaftsenglisch"), new FaecherData(Fakultaet.BW,"Grundlagen der Organisation"), new FaecherData(Fakultaet.BW,"Grundlagen der Material- und Fertigungswirtschaft"), new FaecherData(Fakultaet.BW,"Grundlagen des Personalmanagement"), new FaecherData(Fakultaet.BW,"Grundlagen Marketing und Vertrieb"), new FaecherData(Fakultaet.BW,"Wirtschaftsprivatrecht / Gesellschaftsrecht"), new FaecherData(Fakultaet.BW,"Arbeitsrecht"), new FaecherData(Fakultaet.BW,"Finanz- und Investitionswirtschaft"), new FaecherData(Fakultaet.BW,"Steuern"), new FaecherData(Fakultaet.BW,"Betriebswirtschaftliches Seminar"), new FaecherData(Fakultaet.BW,"Betriebswirtschaftliche Entscheidungstechniken"), new FaecherData(Fakultaet.BW,"Digital Business Models"), new FaecherData(Fakultaet.BW,"Controlling und Finanzierung"), new FaecherData(Fakultaet.BW,"Fundamentals of International Economics"), new FaecherData(Fakultaet.BW,"Produkt- und Designmanagement"), new FaecherData(Fakultaet.BW,"Datev I"), new FaecherData(Fakultaet.BW,"Designing The Global Marketing Programme"), new FaecherData(Fakultaet.BW,"Doing Business in Russia"), new FaecherData(Fakultaet.BW,"Grundlagen des Facility-Managements / Principles of Facility Management"), new FaecherData(Fakultaet.BW,"Management Accounting and Control"), new FaecherData(Fakultaet.BW,"Optimization in Logistics"), new FaecherData(Fakultaet.BW,"Private Wealth Management"), new FaecherData(Fakultaet.BW,"Risikomanagement "), new FaecherData(Fakultaet.BW,"Unternehmensführung"), new FaecherData(Fakultaet.BW,"Politische ökonomie"), new FaecherData(Fakultaet.BW,"Spezielle Steuerrechtsgebiete / Special taxes"), new FaecherData(Fakultaet.BW,"Finanzmanagementkonzepte I"), new FaecherData(Fakultaet.BW,"Finanzmanagementkonzepte II"), new FaecherData(Fakultaet.BW,"Marketing- und Vertriebsmanagement I"), new FaecherData(Fakultaet.BW,"Marketing- und Vertriebsmanagement II"), new FaecherData(Fakultaet.BW,"Personalmanagement"), new FaecherData(Fakultaet.BW,"Steuern I "), new FaecherData(Fakultaet.BW,"Steuern II"), new FaecherData(Fakultaet.BW,"Wirtschaftsinformatik I "), new FaecherData(Fakultaet.BW,"Wirtschaftsinformatik II "), new FaecherData(Fakultaet.BW,"Beschaffung"), new FaecherData(Fakultaet.BW,"Logistik"), new FaecherData(Fakultaet.BW,"Rechnungslegung und Wirtschaftsprüfung I "), new FaecherData(Fakultaet.BW,"Rechnungslegung und Wirtschaftsprüfung II"), new FaecherData(Fakultaet.EW,"Ingenieurmathematik I"), new FaecherData(Fakultaet.EW,"Grundlagen der Elektrotechnik"), new FaecherData(Fakultaet.EW,"Informatik I"), new FaecherData(Fakultaet.EW,"Physik I"), new FaecherData(Fakultaet.EW,"Biomedizinische Grundlagen I"), new FaecherData(Fakultaet.EW,"Ingenieurmathematik II"), new FaecherData(Fakultaet.EW,"Elektronik und Messtechnik"), new FaecherData(Fakultaet.EW,"Informatik II"), new FaecherData(Fakultaet.EW,"Physik II"), new FaecherData(Fakultaet.EW,"Biomedizinische Grundlagen II"), new FaecherData(Fakultaet.EW,"Konstruktion und Entwicklung"), new FaecherData(Fakultaet.EW,"Mikrocomputertechnik"), new FaecherData(Fakultaet.EW,"Werkstoffe und Design in der Medizintechnik"), new FaecherData(Fakultaet.EW,"Grundlagen der Betriebswirtschaftslehre"), new FaecherData(Fakultaet.EW,"Sensorik in der Medizintechnik"), new FaecherData(Fakultaet.EW,"Marketing und Vertrieb"), new FaecherData(Fakultaet.EW,"Medizinische Bildverarbeitung"), new FaecherData(Fakultaet.EW,"Qualitätsmanagement in der Medizintechnik"), new FaecherData(Fakultaet.EW,"Grundlagen der medizinischen Bildgebung"), new FaecherData(Fakultaet.EW,"Projektmanagement"), new FaecherData(Fakultaet.EW,"Regelungstechnik"), new FaecherData(Fakultaet.EW,"Praktische Zeit im Betrieb"), new FaecherData(Fakultaet.EW,"Softwareentwicklung in der Medizintechnik"), new FaecherData(Fakultaet.EW,"Biosignalverarbeitung"), new FaecherData(Fakultaet.EW,"Minimalinvasive Verfahren"), new FaecherData(Fakultaet.EW,"Medizinische Optik und Lasertechnologie"), new FaecherData(Fakultaet.EW,"Grundlagen der medizinischen Gerätetechnik"), new FaecherData(Fakultaet.EW,"Krankenhausorganisation"), new FaecherData(Fakultaet.EW,"Systems Engineering in der Medizintechnik"), new FaecherData(Fakultaet.EW,"Beschaffung, Produktion und Logistik"), new FaecherData(Fakultaet.EW,"Technischer Einkauf"), new FaecherData(Fakultaet.EW,"Rechnergestützte Messtechnik"), new FaecherData(Fakultaet.EW,"Konstruktionsarbeit in der Medizintechnik"), new FaecherData(Fakultaet.EW,"Produktmanagement und Technischer Vertrieb"), new FaecherData(Fakultaet.EW,"Ingenieurmathematik I"), new FaecherData(Fakultaet.EW,"Elektrotechnik I"), new FaecherData(Fakultaet.EW,"Informatik I"), new FaecherData(Fakultaet.EW,"Technische Mechanik"), new FaecherData(Fakultaet.EW,"Ingenieurmathematik II"), new FaecherData(Fakultaet.EW,"Elektrotechnik II"), new FaecherData(Fakultaet.EW,"Informatik II "), new FaecherData(Fakultaet.EW,"Angewandte Physik"), new FaecherData(Fakultaet.EW,"Elektrotechnik III"), new FaecherData(Fakultaet.EW,"Elektrische Messtechnik"), new FaecherData(Fakultaet.EW,"Elektronische Bauelemente"), new FaecherData(Fakultaet.EW,"Digitaltechnik"), new FaecherData(Fakultaet.EW,"Informatik III "), new FaecherData(Fakultaet.EW,"Mikrocomputertechnik"), new FaecherData(Fakultaet.EW,"Schaltungstechnik"), new FaecherData(Fakultaet.EW,"Regelungstechnik I"), new FaecherData(Fakultaet.EW,"Grundlagen der Energietechnik"), new FaecherData(Fakultaet.EW,"Informatik IV"), new FaecherData(Fakultaet.EW,"Kommunikationstechnik"), new FaecherData(Fakultaet.EW,"Mikrocontroller mit Echtzeitbetriebssystemen"), new FaecherData(Fakultaet.EW,"Grundlagen elektrische Antriebe"), new FaecherData(Fakultaet.EW,"Regelungstechnik II"), new FaecherData(Fakultaet.EW,"Bussysteme"), new FaecherData(Fakultaet.EW,"Leistungselektronik"), new FaecherData(Fakultaet.EW,"Automatisierungstechnik"), new FaecherData(Fakultaet.EW,"Energieversorgung in der Gebäudetechnik"), new FaecherData(Fakultaet.EW,"Product Engineering in der Elektronikindustrie "), new FaecherData(Fakultaet.EW,"Sensorik"), new FaecherData(Fakultaet.EW,"Kommunikationssysteme"), new FaecherData(Fakultaet.EW,"Marketing und Vertrieb"), new FaecherData(Fakultaet.EW,"Projektarbeit in der Praxis"), new FaecherData(Fakultaet.EW,"Robotik"), new FaecherData(Fakultaet.EW,"Ingenieurmathematik I"), new FaecherData(Fakultaet.EW,"Grundlagen der Elektrotechnik"), new FaecherData(Fakultaet.EW,"Informatik I"), new FaecherData(Fakultaet.EW,"Technische Mechanik"), new FaecherData(Fakultaet.EW,"Grundlagen der Betriebs- und Volkswirtschaftslehre"), new FaecherData(Fakultaet.EW,"Ingenieurmathematik II"), new FaecherData(Fakultaet.EW,"Elektronik und Messtechnik"), new FaecherData(Fakultaet.EW,"Informatik II"), new FaecherData(Fakultaet.EW,"Angewandte Physik"), new FaecherData(Fakultaet.EW,"Regelungstechnik"), new FaecherData(Fakultaet.EW,"Grundlagen der Energiewirtschaft"), new FaecherData(Fakultaet.EW,"Grundlagen der Energietechnik"), new FaecherData(Fakultaet.EW,"Buchführung und Bilanzierung"), new FaecherData(Fakultaet.EW,"Excel und VBA-Anwendungen"), new FaecherData(Fakultaet.EW,"Marketing und Vertrieb"), new FaecherData(Fakultaet.EW,"Energierecht und Regulierung"), new FaecherData(Fakultaet.EW,"Kosten- und Leistungsrechnung"), new FaecherData(Fakultaet.EW,"Netztechnik und -führung"), new FaecherData(Fakultaet.EW,"Finanz- und Investitionswirtschaft"), new FaecherData(Fakultaet.EW,"Projektmanagement"), new FaecherData(Fakultaet.EW,"Grundlagen der Produktionstechnik"), new FaecherData(Fakultaet.EW,"Stromerzeugungstechnologien"), new FaecherData(Fakultaet.EW,"Gas- und Kommunalwirtschaft"), new FaecherData(Fakultaet.EW,"Energieeffizienz in Wohngebäuden"), new FaecherData(Fakultaet.EW,"Aktuelle Managementthemen der Energiewirtschaft und -technik"), new FaecherData(Fakultaet.EW,"Energie, Umwelt, Gesellschaft und Ethik"), new FaecherData(Fakultaet.EW,"Energiehandel und Marktmechanismen"), new FaecherData(Fakultaet.EW,"Energieeffizienz in Industrie und Gewerbe"), new FaecherData(Fakultaet.EW,"Energieberatung für Wohngebäude"), new FaecherData(Fakultaet.EW,"Technischer Einkauf"), new FaecherData(Fakultaet.EW,"Sensorik"), new FaecherData(Fakultaet.EW,"Automatisierungstechnik"), new FaecherData(Fakultaet.EW,"Rechnergestützte Messtechnik"), new FaecherData(Fakultaet.EW,"ERP-Systeme"), new FaecherData(Fakultaet.EW,"Controlling"), new FaecherData(Fakultaet.EW,"Geschäftsprozessmanagement"), new FaecherData(Fakultaet.EW,"Wirtschaftsprivatrecht"), new FaecherData(Fakultaet.EW,"Personalmanagement"), new FaecherData(Fakultaet.EW,"Produktions- und Prozessplanung"), new FaecherData(Fakultaet.EW,"Logistik- und Fabrikplanung"), new FaecherData(Fakultaet.EW,"Datenbanksysteme und -anwendungen"), new FaecherData(Fakultaet.EW,"Sensorik"), new FaecherData(Fakultaet.EW,"Ingenieurmathematik I"), new FaecherData(Fakultaet.EW,"Grundlagen der Elektrotechnik"), new FaecherData(Fakultaet.EW,"Informatik I"), new FaecherData(Fakultaet.EW,"Technische Mechanik"), new FaecherData(Fakultaet.EW,"Principles of Business Administration and Economics"), new FaecherData(Fakultaet.EW,"Ingenieurmathematik II"), new FaecherData(Fakultaet.EW,"Elektronik und Messtechnik "), new FaecherData(Fakultaet.EW,"Informatik II"), new FaecherData(Fakultaet.EW,"Applied Physics"), new FaecherData(Fakultaet.EW,"Software-Tools"), new FaecherData(Fakultaet.EW,"Buchführung und Bilanzierung"), new FaecherData(Fakultaet.EW,"Prozessoptimierung und statistische Qualitätssicherung"), new FaecherData(Fakultaet.EW,"Marketing and Sales"), new FaecherData(Fakultaet.EW,"Konstruktion und Entwicklung"), new FaecherData(Fakultaet.EW,"Kosten- und Leistungsrechnung"), new FaecherData(Fakultaet.EW,"Procurement, Manufacturing and Logistics"), new FaecherData(Fakultaet.EW,"Grundlagen der Produktionstechnik"), new FaecherData(Fakultaet.EW,"Finanz- und Investitionswirtschaft"), new FaecherData(Fakultaet.EW,"Project Management"), new FaecherData(Fakultaet.EW,"International Business and Cross-Cultural Communication"), new FaecherData(Fakultaet.EW,"Energieversorgung in der Gebäudetechnik"), new FaecherData(Fakultaet.EW,"Internettechnologien"), new FaecherData(Fakultaet.EW,"Automatisierungstechnik "), new FaecherData(Fakultaet.EW,"Telekommunikation"), new FaecherData(Fakultaet.EW,"Rechnergestützte Messtechnik"), new FaecherData(Fakultaet.EW,"Mikrocomputertechnik"), new FaecherData(Fakultaet.EW,"Batteriespeicher"), new FaecherData(Fakultaet.EW,"Unternehmensplanspiel"), new FaecherData(Fakultaet.EW,"ERP-Systeme"), new FaecherData(Fakultaet.EW,"Controlling "), new FaecherData(Fakultaet.EW,"Geschäftsprozessmanagement "), new FaecherData(Fakultaet.EW,"Wirtschaftsprivatrecht "), new FaecherData(Fakultaet.EW,"Personalmanagement"), new FaecherData(Fakultaet.EW,"Product Engineering in der Elektroindustrie"), new FaecherData(Fakultaet.EW,"Produktions- und Prozessplanung"), new FaecherData(Fakultaet.EW,"Logistik- und Fabrikplanung"), new FaecherData(Fakultaet.EW,"Datenbanksysteme und -anwendungen"), new FaecherData(Fakultaet.EW,"Qualitätsmanagement "), new FaecherData(Fakultaet.EW,"Technischer Einkauf"), new FaecherData(Fakultaet.EW,"Produktmanagement und Technischer Vertrieb"), new FaecherData(Fakultaet.EW,"Internationale Beschaffung"), new FaecherData(Fakultaet.EW,"International Production Networks and Logistik"), new FaecherData(Fakultaet.EW,"Ingenieurmathematik I"), new FaecherData(Fakultaet.EW,"Grundlagen der Elektrotechnik"), new FaecherData(Fakultaet.EW,"Informatik I"), new FaecherData(Fakultaet.EW,"Technische Mechanik"), new FaecherData(Fakultaet.EW,"Grundlagen der Betriebs- und Volkswirtschaftslehre"), new FaecherData(Fakultaet.EW,"Ingenieurmathematik II"), new FaecherData(Fakultaet.EW,"Elektronik und Messtechnik "), new FaecherData(Fakultaet.EW,"Informatik II"), new FaecherData(Fakultaet.EW,"Angewandte Physik"), new FaecherData(Fakultaet.EW,"Energiewirtschaft"), new FaecherData(Fakultaet.EW,"Regelungstechnik"), new FaecherData(Fakultaet.EW,"Buchführung und Bilanzierung"), new FaecherData(Fakultaet.EW,"Marketing und Vertrieb"), new FaecherData(Fakultaet.EW,"Grundlagen der Produktionstechnik"), new FaecherData(Fakultaet.EW,"Konstruktion und Entwicklung"), new FaecherData(Fakultaet.EW,"Kosten- und Leistungsrechnung"), new FaecherData(Fakultaet.EW,"Beschaffung, Produktion und Logistik"), new FaecherData(Fakultaet.EW,"Finanz- und Investitionswirtschaft"), new FaecherData(Fakultaet.EW,"Energieversorgung in der Gebäudetechnik"), new FaecherData(Fakultaet.EW,"Sensorik"), new FaecherData(Fakultaet.EW,"Internettechnologien"), new FaecherData(Fakultaet.EW,"Automatisierungstechnik "), new FaecherData(Fakultaet.EW,"Telekommunikation "), new FaecherData(Fakultaet.EW,"Rechnergestützte Messtechnik"), new FaecherData(Fakultaet.EW,"Batteriespeicher"), new FaecherData(Fakultaet.EW,"Mikrocomputertechnik"), new FaecherData(Fakultaet.EW,"Geschäftsprozessmanagement "), new FaecherData(Fakultaet.EW,"Wirtschaftsprivatrecht"), new FaecherData(Fakultaet.EW,"Personalmanagement"), new FaecherData(Fakultaet.EW,"Produktions- und Prozessplanung"), new FaecherData(Fakultaet.EW,"Logistik- und Fabrikplanung"), new FaecherData(Fakultaet.EW,"Datenbanksysteme und -anwendungen"), new FaecherData(Fakultaet.EW,"Qualitätsmanagement "), new FaecherData(Fakultaet.EW,"Technischer Einkauf"), new FaecherData(Fakultaet.EW,"Produktmanagement und Technischer Vertrieb"), new FaecherData(Fakultaet.SA,"Soziale Arbeit"), new FaecherData(Fakultaet.SA,"Menschliches Verhalten, Entwicklung, Erziehung und Bildung"), new FaecherData(Fakultaet.SA,"Theorien und Organisationen der Sozialen Arbeit"), new FaecherData(Fakultaet.SA,"Gesellschaft und Politik"), new FaecherData(Fakultaet.SA,"Strukturen des Rechts"), new FaecherData(Fakultaet.SA,"Propädeutikum"), new FaecherData(Fakultaet.SA,"Handlungskompetenz - Basisstrategien"), new FaecherData(Fakultaet.SA,"Wissenschaft und Praxis Sozialer Arbeit"), new FaecherData(Fakultaet.SA,"Sozialwissenschaftliche Forschung: Methoden und Projekte"), new FaecherData(Fakultaet.SA,"Sozialleistungsrecht und Formen des Zusammenlebens"), new FaecherData(Fakultaet.SA,"Handlungskompetenz Differenzielle Methoden"), new FaecherData(Fakultaet.SA,"Soziale Arbeit und Kultur Kulturelle Differenzen"), new FaecherData(Fakultaet.SA,"Soziale Arbeit und Gesundheit Theoretische Zugänge"), new FaecherData(Fakultaet.SA,"Soziale Arbeit und sozialer Raum Theoretische Zugänge"), new FaecherData(Fakultaet.SA,"Soziale Arbeit und soziale Ungleichheit Theoretische Zugänge"), new FaecherData(Fakultaet.SA,"Soziale Arbeit und Kultur Interkulturelle Kompetenzen"), new FaecherData(Fakultaet.SA,"Soziale Arbeit und Gesundheit Methoden Klinischer Sozialarbeit"), new FaecherData(Fakultaet.SA,"Soziale Arbeit und sozialer Raum Methoden sozialräumlichen Arbeitens"), new FaecherData(Fakultaet.SA,"Soziale Arbeit und soziale Ungleichheit Methoden im Umgang mit sozialer"), new FaecherData(Fakultaet.SA,"Ungleichheit"), new FaecherData(Fakultaet.SA,"Forschungs- und Entwicklungswerkstätten"), new FaecherData(Fakultaet.SA,"Soziale Arbeit und Kultur Interkulturelle und gendersensible Praxis"), new FaecherData(Fakultaet.SA,"Soziale Arbeit und Gesundheit Anwendungsfelder Klinischer Sozialarbeit"), new FaecherData(Fakultaet.SA,"Soziale Arbeit und sozialer Raum Anwendungsfelder"), new FaecherData(Fakultaet.SA,"Soziale Arbeit und soziale Ungleichheit Anwendungsfelder"), new FaecherData(Fakultaet.SA,"Handlungskompetenz - Diagnostik und Fallarbeit"), new FaecherData(Fakultaet.IF,"Grundlagen der Informatik"), new FaecherData(Fakultaet.IF,"Grundlagen der theoretischen Informatik"), new FaecherData(Fakultaet.IF,"Digitaltechnik"), new FaecherData(Fakultaet.IF,"Mathematik 1"), new FaecherData(Fakultaet.IF,"Mathematik 2"), new FaecherData(Fakultaet.IF,"Software Engineering 1"), new FaecherData(Fakultaet.IF,"Englisch"), new FaecherData(Fakultaet.IF,"Programmieren 1"), new FaecherData(Fakultaet.IF,"Programmieren 2"), new FaecherData(Fakultaet.IF,"Programmieren 3"), new FaecherData(Fakultaet.IF,"Software Engineering 2"), new FaecherData(Fakultaet.IF,"Datenbanken"), new FaecherData(Fakultaet.IF,"Rechnerarchitektur"), new FaecherData(Fakultaet.IF,"Algorithmen und Datenstrukturen"), new FaecherData(Fakultaet.IF,"Grundlagen der Betriebswirtschaftslehre"), new FaecherData(Fakultaet.IF,"IT Sicherheit"), new FaecherData(Fakultaet.IF,"Betriebssysteme"), new FaecherData(Fakultaet.IF,"Datenkommunikation"), new FaecherData(Fakultaet.IF,"Statistik"), new FaecherData(Fakultaet.IF,"Präsentation und Kommunikation"), new FaecherData(Fakultaet.IF,"Praktikum"), new FaecherData(Fakultaet.IF,"Praxisseminar"), new FaecherData(Fakultaet.IF,"IT-Recht"), new FaecherData(Fakultaet.IF,"Grundlagen Projektmanagement/Projektcontrolling"), new FaecherData(Fakultaet.IF,"Numerik"), new FaecherData(Fakultaet.IF,"Compiler"), new FaecherData(Fakultaet.IF,"Verteilte System"), new FaecherData(Fakultaet.IF,"Internettechnologien"), new FaecherData(Fakultaet.IF,"Prozessrechentechnik"), new FaecherData(Fakultaet.IF,"Grundlagen der Wirtschaftsinformatik"), new FaecherData(Fakultaet.IF,"BWL Basismodul (Teil: Einführung BW"), new FaecherData(Fakultaet.IF,"BWL Basismodul (Teil: Buchführung"), new FaecherData(Fakultaet.IF,"Software Engineering 1"), new FaecherData(Fakultaet.IF,"Finanzen und Investition"), new FaecherData(Fakultaet.IF,"Englisch"), new FaecherData(Fakultaet.IF,"Software Engineering 2"), new FaecherData(Fakultaet.IF,"Datenbanken"), new FaecherData(Fakultaet.IF,"Statistik"), new FaecherData(Fakultaet.IF,"Geschäftsprozesse und Organisation"), new FaecherData(Fakultaet.IF,"Algorithmen und Datenstrukturen"), new FaecherData(Fakultaet.IF,"IT-Infrastrukturen"), new FaecherData(Fakultaet.IF,"Kosten- und Leistungsrechnung"), new FaecherData(Fakultaet.IF,"Material und Fertigungswirtschaft/Logistik"), new FaecherData(Fakultaet.IF,"Operations Research"), new FaecherData(Fakultaet.IF,"IT-Compliance und IT-Recht"), new FaecherData(Fakultaet.IF,"Praxisorientiertes Studienprojekt"), new FaecherData(Fakultaet.IF,"IT-Projektmanagement"), new FaecherData(Fakultaet.IF,"Praktische Zeit im Betrieb"), new FaecherData(Fakultaet.IF,"Praxisseminar"), new FaecherData(Fakultaet.IF,"Internettechnologien"), new FaecherData(Fakultaet.IF,"Software Engineering 3"), new FaecherData(Fakultaet.IF,"IT-Sicherheit"), new FaecherData(Fakultaet.IF,"IT-Management und -Controlling"), new FaecherData(Fakultaet.IF,"Unternehmenssoftware (ERP-System"), new FaecherData(Fakultaet.IF,"Controlling-Praxis im Unternehmen"), new FaecherData(Fakultaet.IF,"Doing Business in Russia"), new FaecherData(Fakultaet.IF,"Revenue Management"), new FaecherData(Fakultaet.IF,"Informations- und Metamodellierung"), new FaecherData(Fakultaet.IF,"Enterprise Computing"), new FaecherData(Fakultaet.IF,"Sicherheit mobiler System"), new FaecherData(Fakultaet.IF,"Internet of Things (IoT"), new FaecherData(Fakultaet.IF,"Innovationslaber (IoT-Projekt"), new FaecherData(Fakultaet.IF,"Mobile Business"), new FaecherData(Fakultaet.IF,"Wahlpflichtmodul BW"), new FaecherData(Fakultaet.IF,"Wahlpflichtmodul IF"), new FaecherData(Fakultaet.IF,"Dirigieren"), new FaecherData(Fakultaet.IF,"Digitaltechnik"), new FaecherData(Fakultaet.IF,"Grundlagen der Elektrotechnik"), new FaecherData(Fakultaet.IF,"Technische Mechanik"), new FaecherData(Fakultaet.IF,"Software Engineering"), new FaecherData(Fakultaet.IF,"Ingenieurmathematik 1"), new FaecherData(Fakultaet.IF,"Ingenieurmathematik 2"), new FaecherData(Fakultaet.IF,"Elektronik und Messtechnik"), new FaecherData(Fakultaet.IF,"Angewandte Physik"), new FaecherData(Fakultaet.IF,"Datenbanken"), new FaecherData(Fakultaet.IF,"Regelungstechnik"), new FaecherData(Fakultaet.IF,"Konstruktion und Entwicklung"), new FaecherData(Fakultaet.IF,"Rechnerarchitektur"), new FaecherData(Fakultaet.IF,"Algorithmen und Datenstrukturen"), new FaecherData(Fakultaet.IF,"Grundlagen der Automobiltechnik"), new FaecherData(Fakultaet.IF,"Fahrwerktechnik"), new FaecherData(Fakultaet.IF,"Antriebstechnik"), new FaecherData(Fakultaet.IF,"Prozessrechentechnik"), new FaecherData(Fakultaet.IF,"Karosserietechnik"), new FaecherData(Fakultaet.MA,"Naturwissenschaftliche Grundlagen "), new FaecherData(Fakultaet.MA,"Maschinenkonstruktion I "), new FaecherData(Fakultaet.MA,"Wirtschaftliche und soziale Kompetenzen "), new FaecherData(Fakultaet.MA,"Ingenieurmathematik"), new FaecherData(Fakultaet.MA,"Werkstoffkunde"), new FaecherData(Fakultaet.MA,"Technische Mechanik "), new FaecherData(Fakultaet.MA,"Grundlagen Ingenieurinformatik"), new FaecherData(Fakultaet.MA,"Festigkeitslehre"), new FaecherData(Fakultaet.MA,"Ingenieurmathematik"), new FaecherData(Fakultaet.MA,"Grundlagen, Elektrotechnik und Elektronik"), new FaecherData(Fakultaet.MA,"Grundlagen Fertigungstechnik"), new FaecherData(Fakultaet.MA,"Versuchstechnik "), new FaecherData(Fakultaet.MA,"Strömungsmechanik "), new FaecherData(Fakultaet.MA,"Technische Thermodynamik"), new FaecherData(Fakultaet.MA,"Grundlagen CAD/FEM"), new FaecherData(Fakultaet.MA,"Steuerungs- und Regelungstechnik"), new FaecherData(Fakultaet.MA,"Maschinenkonstruktion II"), new FaecherData(Fakultaet.MA,"Elektrische Antriebe und Getriebetechnik"), new FaecherData(Fakultaet.MA,"Grundlagen Leichtbau"), new FaecherData(Fakultaet.MA,"Produktionsmanagement"), new FaecherData(Fakultaet.MA,"Umwelttechnik "), new FaecherData(Fakultaet.MA,"Werkstoffe und Betriebsfestigkeit "), new FaecherData(Fakultaet.MA,"Werkzeugmaschinen und Automatisierungstechnik"), new FaecherData(Fakultaet.MA,"Wärme- und Fluidtechnik "), new FaecherData(Fakultaet.MA,"Gießereitechnik und Schweißtechnik"), new FaecherData(Fakultaet.MA,"Entwicklung dynamischer Systeme "), new FaecherData(Fakultaet.MA,"Energietechnik 1 "), new FaecherData(Fakultaet.MA,"Energietechnik 2 "), new FaecherData(Fakultaet.MA,"Energietechnik 3 "), new FaecherData(Fakultaet.MA,"Energie-/Umweltmanagement"), new FaecherData(Fakultaet.MA,"Energiewirtschaft/Energieeffizienz"), new FaecherData(Fakultaet.MA,"Vertiefende Fertigungstechnik 1"), new FaecherData(Fakultaet.MA,"Werkzeugmaschinen und Automatisierungstechnik"), new FaecherData(Fakultaet.MA,"Qualitätsmanagement und Unternehmensführung "), new FaecherData(Fakultaet.MA,"Vertiefende Fertigungstechnik 2 "), new FaecherData(Fakultaet.MA,"Produktionslogistik und Investitionsmanagement"), new FaecherData(Fakultaet.MA,"Konstruktionswerkstoffe für den Leichtbau"), new FaecherData(Fakultaet.MA,"Leichtbaustrukturen"), new FaecherData(Fakultaet.MA,"Wärme- und Fluidtechnik"), new FaecherData(Fakultaet.MA,"Fertigungstechnologien für den Leichtbau"), new FaecherData(Fakultaet.MA,"Entwicklung dynamischer Systeme"), new FaecherData(Fakultaet.MA,"Faserverbundwerkstoffe"), new FaecherData(Fakultaet.MA,"Prozesseffizienz und Ressourcenmanagement in der Fertigung "), new FaecherData(Fakultaet.MA,"Stoffstrommanagement und Abfallwirtschaft"), new FaecherData(Fakultaet.MA,"Industriemarketing und technische Betriebsführung"), new FaecherData(Fakultaet.MA,"Vertiefung CAD "), new FaecherData(Fakultaet.MA,"Strömungsmaschinen"), new FaecherData(Fakultaet.MA,"Ingenieurmathematik"), new FaecherData(Fakultaet.MA,"Ingenieurinformatik"), new FaecherData(Fakultaet.MA,"Naturwissenschaftliche Grundlagen"), new FaecherData(Fakultaet.MA,"Materialkunde"), new FaecherData(Fakultaet.MA,"Technische Mechanik I "), new FaecherData(Fakultaet.MA,"Technische Mechanik II "), new FaecherData(Fakultaet.MA,"Maschinenkonstruktion I "), new FaecherData(Fakultaet.MA,"Maschinenkonstruktion II"), new FaecherData(Fakultaet.MA,"Elektro- und Messtechnik "), new FaecherData(Fakultaet.MA,"Grundlagen der Fertigungstechnik"), new FaecherData(Fakultaet.MA,"Interdisziplinäre Fächer"), new FaecherData(Fakultaet.MA,"Grundlagen der Energietechnik "), new FaecherData(Fakultaet.MA,"Konstruktion und CAD"), new FaecherData(Fakultaet.MA,"Finite Elemente"), new FaecherData(Fakultaet.MA,"Automatisierungs- und Versuchstechnik"), new FaecherData(Fakultaet.MA,"Werkstoffe und Leichtbau I "), new FaecherData(Fakultaet.MA,"Konstruktionsarbeit"), new FaecherData(Fakultaet.MA,"Erweiterte Energietechnik"), new FaecherData(Fakultaet.MA,"Werkstoffe und Leichtbau II"), new FaecherData(Fakultaet.MA,"Ingenieurwissenschaftliche Praktika"), new FaecherData(Fakultaet.MA,"Leichtbaumechanik"), new FaecherData(Fakultaet.MA,"Prozesstechnologie im Strukturleichtbau"), new FaecherData(Fakultaet.MA,"Naturwissenschaftliche Grundlagen"), new FaecherData(Fakultaet.MA,"Maschinenkonstruktion I"), new FaecherData(Fakultaet.MA,"Wirtschaftliche und soziale Kompetenzen "), new FaecherData(Fakultaet.MA,"Ingenieurmathematik"), new FaecherData(Fakultaet.MA,"Werkstoffkunde"), new FaecherData(Fakultaet.MA,"Technische Mechanik"), new FaecherData(Fakultaet.MA,"Grundlagen Ingenieurinformatik"), new FaecherData(Fakultaet.MA,"Festigkeitslehre"), new FaecherData(Fakultaet.MA,"Maschinenelemente"), new FaecherData(Fakultaet.MA,"Grundlagen  Elektrotechnik und Elektronik"), new FaecherData(Fakultaet.MA,"Grundlagen Fertigungstechnik"), new FaecherData(Fakultaet.MA,"Versuchstechnik"), new FaecherData(Fakultaet.MA,"Strömungsmechanik"), new FaecherData(Fakultaet.MA,"Technische Thermodynamik"), new FaecherData(Fakultaet.MA,"Grundlagen CAD/FEM"), new FaecherData(Fakultaet.MA,"Steuerungs- und Regelungstechnik"), new FaecherData(Fakultaet.MA,"Maschinenkonstruktion II"), new FaecherData(Fakultaet.MA,"Verbrennungsmotoren"), new FaecherData(Fakultaet.MA,"Praktisches Studiensemester"), new FaecherData(Fakultaet.MA,"Projektarbeit"), new FaecherData(Fakultaet.MA,"Ingenieurtechnisches Praktikum"), new FaecherData(Fakultaet.MA,"Fahrzeuginformatik"), new FaecherData(Fakultaet.MA,"Automobiltechnik I"), new FaecherData(Fakultaet.MA,"Grundlagen der Antriebstechnik"), new FaecherData(Fakultaet.MA,"Grundlagen der Fahrzeugmechatronik"), new FaecherData(Fakultaet.MA,"Automobiltechnik II"), new FaecherData(Fakultaet.MA,"Fahrzeuginformatik"), new FaecherData(Fakultaet.MA,"Grundlagen der Antriebstechnik"), new FaecherData(Fakultaet.MA,"Grundlagen moderner NFZ"), new FaecherData(Fakultaet.MA,"Moderne NFZ Technik I"), new FaecherData(Fakultaet.MA,"Moderne NFZ Technik II"), new FaecherData(Fakultaet.MA,"Fahrzeuginformatik"), new FaecherData(Fakultaet.MA,"Grundlagen der Antriebstechnik"), new FaecherData(Fakultaet.MA,"Grundlagen der Fahrzeugmechatronik"), new FaecherData(Fakultaet.MA,"Alternative Antriebstechniken"), new FaecherData(Fakultaet.MA,"Entwicklung dynamischer Systeme"), new FaecherData(Fakultaet.MA,"Qualitätsmanagement und Unternehmensführung"), new FaecherData(Fakultaet.MA,"Konstruktionswerkstoffe für den Leichtbau"), new FaecherData(Fakultaet.MA,"Leichtbaustrukturen"));
                preference.edit().putBoolean("Subjekts inserted",true).apply();
            }
            if(!preference.getBoolean("Emptytimetable inserted",false)){
                for(int i = 0;i<ENTRYCOUNT;i++){
                    ScheduleFragment.scheduleDao.insertEmptyTimetable(new CustomTimetable(i,"","","",BASICCOLOR));
                }
                preference.edit().putBoolean("Emptytimetable inserted",true).apply();
            }
            ScheduleFragment.timetable = ScheduleFragment.scheduleDao.getTimetable();
            View view = getView();
            if(view!=null){
                final TableLayout tl = getView().findViewById(R.id.table);
                elements = new TextView[tl.getChildCount()-1][((TableRow) tl.getChildAt(1)).getChildCount()];

                for(int y = 1; y < tl.getChildCount(); y++){
                    TableRow tr = ((TableRow) tl.getChildAt(y));
                    for (int x = 1; x < tr.getChildCount(); x++) {
                        TextView tv = ((TextView) tr.getChildAt(x));
//                        tv.setText("");
                        tv.setOnClickListener(ocl);
                        elements[y-1][x-1] = tv;

                    }
                }

            for(int j = 0;j<(timetable.size()/2);j++){
                final int i;
                if(isEven){
                    i=j;
                }
                else{
                    i=j+(ENTRYCOUNT/2);
                }
     //          Log.d("SchFr", elements.length+"");
                final TextView current = elements[(j % (elements[0].length))][(j / (elements.length))];
                if (current != null) {
                    Log.d("SchFr", j+"");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            current.setText(timetable.get(i).getFach());
                            current.setBackgroundColor(timetable.get(i).getColor());
                        }
                    });
                }

            }}

            return null;
        }
    }


}
