package de.haw_landshut.hawmobile.schedule;

//import android.support.v4.app.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.view.*;

import android.widget.*;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import de.haw_landshut.hawmobile.Credentials;
import de.haw_landshut.hawmobile.Fakultaet;
import de.haw_landshut.hawmobile.MainActivity;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.*;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

    //Constant numbers
    private final int ENTRYCOUNT = 60; //Number of hours per 2 weeks
    private final int BASICCOLOR = 16777215; //default background color

    //Textviews
    private UpOnlyAutoCompleteTextView et_fach;  //subject in bottomsheet
    private UpOnlyAutoCompleteTextView et_prof;  //subject in bottomsheet
    private UpOnlyAutoCompleteTextView et_raum;              //room in bottomsheet
    private TextView currentDate,currentWeek; //aktuelles Datum, aktuelle Wochenzahl
    private TextView currentTV;             //current Textview

    //Others
    private View.OnClickListener ocl;
    public static BottomSheetBehavior mBottomSheetBehavior1;
    private View bottomSheet;
    private List<CustomTimetable> timetable;
    protected ScheduleDao scheduleDao = MainActivity.getHawDatabase(getContext()).scheduleDao();
    private LocationMarker locationmarker;
    private TextView[][] elements;
    private boolean isEven;
    private  String[] subjects;
    private String[] profs;
    private String[] rooms;
    private int colormaker;
    private  Context context;
    private boolean checkDouble;
    //Buttons
    Button edit;
    Button save;
    Button cancel;
    Button show;
    Button color;
    Button clear;
    Button copy;
    CheckBox wöchentl;
    //Copy
    private String[] copyable = new String[3];
    private int copyablecolor;
    private boolean weeklyCopy;
    private boolean copyActive = false;

    //Getter and Setter


    public boolean isCheckDouble() {
        return checkDouble;
    }

    public void setCheckDouble(boolean checkDouble) {
        this.checkDouble = checkDouble;
    }

    public boolean isEven() {
        return isEven;
    }

    public void setTimetable(List<CustomTimetable> timetable) {
        this.timetable = timetable;
    }

    public List<CustomTimetable> getTimetable() {
        return timetable;
    }
    public void setCopyActive(boolean active){
        this.copyActive=active;
    }

    public void setCopyAction(boolean active, String fach, String prof, String raum, boolean week, int color){
        this.copyablecolor=color;
        this.copyActive=active;
        this.weeklyCopy=week;
        this.copyable[0]=fach;
        this.copyable[1]=prof;
        this.copyable[2]=raum;
    }

    public String[] getCopyable() {
        return copyable;
    }

    public int getCopyablecolor() {
        return copyablecolor;
    }

    public boolean isWeeklyCopy() {
        return weeklyCopy;
    }

    public boolean isCopyActive() {
        return copyActive;
    }

    public String getEt_fach_text() {
        return et_fach.getText().toString();
    }

    public void setEt_fach_text(String s) {
        this.et_fach.setText(s);
    }

    public String getEt_prof_text() {
        return et_prof.getText().toString();
    }

    public void setEt_prof_text(String s) {
        this.et_prof.setText(s);
    }

    public String getEt_raum_text() {
        return et_raum.getText().toString();
    }

    public void setEt_raum_text(String s) {
        this.et_raum.setText(s);
    }

    public TextView getCurrentTV() {
        return currentTV;
    }

    public void setCurrentTV(TextView currentTV) {
        this.currentTV = currentTV;
    }

    public int getColormaker() {
        return colormaker;
    }

    public void setColormaker(int colormaker) {
        this.colormaker = colormaker;
    }

    //HelpMethods
    public int getCurrentTvNumber(){
        return Integer.parseInt(currentTV.getTag().toString());
    }

    public void callOnClick(Button s){
        s.callOnClick();
    }
    public void setEnabledTextViews(boolean b){
        et_fach.setEnabled(b);
        et_prof.setEnabled(b);
        et_raum.setEnabled(b);
    }


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

        ocl = new OnClickLabel(this);
        context=this.getContext();
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
        clear = view.findViewById(R.id.clear);
        copy = view.findViewById(R.id.copy);
        show = view.findViewById(R.id.btn_show);
        wöchentl = view.findViewById(R.id.wöchentlCheckbox);
        et_fach = view.findViewById(R.id.et_fach);
        et_prof = view.findViewById(R.id.et_prof);
        et_raum = view.findViewById(R.id.et_raum);
        bottomSheet = view.findViewById(R.id.bottom_sheet1);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);


        timeInitializer(view);

        currentWeek.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isEven){
                    currentWeek.setText(R.string.odd);
                    isEven=false;
                    new BeginnInsertion().execute();
                }
                else{
                    currentWeek.setText(R.string.even);
                    isEven=true;
                    new BeginnInsertion().execute();
                }
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                locationmarker = ((MainActivity) getActivity()).changeFragment(R.id.action_map);
                locationmarker.showLocation(getEt_raum_text());
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    cancel.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.GONE);
                    copy.setVisibility(View.GONE);
                    clear.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                    color.setVisibility(View.VISIBLE);
                    wöchentl.setVisibility(View.VISIBLE);
                    show.setVisibility(View.GONE);
                    setEnabledTextViews(true);
                    ArrayAdapter<String> subjectAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,subjects);
                    et_fach.setAdapter(subjectAdapter);
                    et_fach.enableAutoHeight();

                    ArrayAdapter<String> profAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,profs);
                    et_prof.setAdapter(profAdapter);
                    et_prof.enableAutoHeight();

                    ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,rooms);
                    et_raum.setAdapter(roomAdapter);
                    et_raum.enableAutoHeight();


                }
        });

        clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int currentHour=getCurrentTvNumber();
                setEt_fach_text("");
                setEt_prof_text("");
                setEt_raum_text("");
                setColormaker(BASICCOLOR);
                if(wöchentl.isChecked()){
                    CustomTimetable table = new CustomTimetable(currentHour,"","","",BASICCOLOR);
                    new UpdateTimetable().execute(table);
                    currentHour = (currentHour+(ENTRYCOUNT/2))%ENTRYCOUNT;
                    table=new CustomTimetable(currentHour,"","","",BASICCOLOR);
                    new UpdateTimetable().execute(table);
                }else{
                    if(isEven){
                        CustomTimetable table = new CustomTimetable(currentHour,"","","",BASICCOLOR);
                        new UpdateTimetable().execute(table);
                    }else{
                        currentHour = (currentHour+(ENTRYCOUNT/2))%ENTRYCOUNT;
                        CustomTimetable table=new CustomTimetable(currentHour,"","","",BASICCOLOR);
                        new UpdateTimetable().execute(table);
                    }
                }
                currentTV.setText("");
                currentTV.setBackgroundColor(BASICCOLOR);
                ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });

        copy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setCopyAction(true,getEt_fach_text(),getEt_prof_text(),getEt_raum_text(),wöchentl.isChecked(),getColormaker());
                ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    cancel.setVisibility(View.GONE);
                    save.setVisibility(View.GONE);
                    edit.setVisibility(View.VISIBLE);
                    color.setVisibility(View.GONE);
                    wöchentl.setVisibility(View.GONE);
                    clear.setVisibility(View.VISIBLE);
                    copy.setVisibility(View.VISIBLE);
                    show.setVisibility(View.VISIBLE);
                    setEnabledTextViews(false);
                    et_fach.setAdapter(null);
                    et_prof.setAdapter(null);
                    et_raum.setAdapter(null);
                    currentTV.setText(testStringLength(getEt_fach_text()));
                    currentTV.setBackgroundColor(getColormaker());


                    int currentHour = getCurrentTvNumber();
                    CustomTimetable table;


                    if(wöchentl.isChecked()){
                        table = new CustomTimetable(currentHour,getEt_prof_text(),getEt_fach_text(),getEt_raum_text(),getColormaker());
                        new UpdateTimetable().execute(table);
                        currentHour = (currentHour+(ENTRYCOUNT/2))%ENTRYCOUNT;
                        table=new CustomTimetable(currentHour,getEt_prof_text(),getEt_fach_text(),getEt_raum_text(),getColormaker());
                        new UpdateTimetable().execute(table);
                    }
                    else {
                        if(isCheckDouble()){
                            if(isEven){
                                table=new CustomTimetable(currentHour,getEt_prof_text(),getEt_fach_text(),getEt_raum_text(),getColormaker());
                                new UpdateTimetable().execute(table);
                                currentHour = (currentHour+(ENTRYCOUNT/2))%ENTRYCOUNT;
                                table=new CustomTimetable(currentHour,"","","",BASICCOLOR);
                                new UpdateTimetable().execute(table);
                            }else{
                                table=new CustomTimetable(currentHour,"","","",BASICCOLOR);
                                new UpdateTimetable().execute(table);
                                currentHour = (currentHour+(ENTRYCOUNT/2))%ENTRYCOUNT;
                                table=new CustomTimetable(currentHour,getEt_prof_text(),getEt_fach_text(),getEt_raum_text(),getColormaker());
                                new UpdateTimetable().execute(table);
                            }

                        }
                        else{
                            if(isEven){
                                table=new CustomTimetable(currentHour,getEt_prof_text(),getEt_fach_text(),getEt_raum_text(),getColormaker());
                                new UpdateTimetable().execute(table);
                            }else{
                                currentHour = (currentHour+(ENTRYCOUNT/2))%ENTRYCOUNT;
                                table=new CustomTimetable(currentHour,getEt_prof_text(),getEt_fach_text(),getEt_raum_text(),getColormaker());
                                new UpdateTimetable().execute(table);
                            }

                        }
                    }
                currentTV.setText(testStringLength(getEt_fach_text()));
                setColormaker(BASICCOLOR);
                ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEt_raum_text("");
                setEt_prof_text("");
                setEt_fach_text("");
                setEnabledTextViews(false);
                save.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.GONE);
                color.setVisibility(View.GONE);
                wöchentl.setVisibility(View.GONE);
                clear.setVisibility(View.VISIBLE);
                copy.setVisibility(View.VISIBLE);
                show.setVisibility(View.VISIBLE);
                ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        color.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ColorPickerDialogBuilder
                        .with(view.getContext())
                        .setTitle(R.string.choose_color)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(5)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int i) {

                            }
                        })
                        .setPositiveButton(R.string.ok, new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, Integer[] integers) {
                                setColormaker(i);

                            }
                        })
                        .setNegativeButton(R.string.set_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .build()
                        .show();

            }

        });
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
            scheduleDao.updateTimetable(customTimetables[0]);
            setTimetable(scheduleDao.getTimetable());
            new BeginnInsertion();
            return null;
        }
    }

    private class DeleteWholeTable extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            scheduleDao.deleteWholeCustomTimetable();
            setTimetable(scheduleDao.getTimetable());
            return null;
        }
    }



    private class BeginnInsertion extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... views) {

            if(!preference.getBoolean("Emptytimetable inserted",false)){
                for(int i = 0;i<ENTRYCOUNT;i++){
                    scheduleDao.insertEmptyTimetable(new CustomTimetable(i,"","","",BASICCOLOR));
                }
                preference.edit().putBoolean("Emptytimetable inserted",true).apply();
            }
            setTimetable(scheduleDao.getTimetable());


            subjects=scheduleDao.getFaecherByStudiengang(Credentials.getFakultaet());
            profs=scheduleDao.getProflastName();
            rooms=scheduleDao.getRooms();




            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            View view = getView();
            if(view!=null){
                final TableLayout tl = getView().findViewById(R.id.table);
                elements = new TextView[tl.getChildCount()-1][((TableRow) tl.getChildAt(1)).getChildCount()];

                for(int y = 1; y < tl.getChildCount(); y++){
                    TableRow tr = ((TableRow) tl.getChildAt(y));
                    for (int x = 1; x < tr.getChildCount(); x++) {
                        TextView tv = ((TextView) tr.getChildAt(x));
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
                    final TextView current = elements[(j % (elements[0].length))][(j / (elements.length))];
                    if (current != null) {
                        current.setText(testStringLength(timetable.get(i).getFach()));
                        current.setBackgroundColor(timetable.get(i).getColor());

                    }

                }
            }
        }
    }

    /*
    Initializes the current date
    sets class variable isEven depending on the current number of the week
     */
    private void timeInitializer(View view){
        currentDate = view.findViewById(R.id.schedule_textView_currentDate);
        GregorianCalendar now = new GregorianCalendar();
        DateFormat df= DateFormat.getDateInstance(DateFormat.SHORT);
        currentDate.setText(df.format(now.getTime()));

        currentWeek=view.findViewById(R.id.calendar_week_switch);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int num_week = c.get(Calendar.WEEK_OF_YEAR);
        if(num_week%2==0){
            currentWeek.setText(R.string.even);
            isEven=true;
        }
        else{
            currentWeek.setText(R.string.odd);
            isEven=false;
        }

    }

    private String testStringLength(String s){
        String finalString="";
        if(s.length()>11){
            String[] pasteparts=s.split(" ");
            for (String p:pasteparts
                    ) {
                if(p.equals("Praktikum")){
                    finalString=finalString+"Prak.";
                }
                else{
                    if(p.length()>5){
                        finalString=finalString+p.substring(0,5)+".";
                }
                    else{
                        finalString=finalString+p+" ";
                    }


                }

            }

        }
        else{
            finalString=s;
        }
        return finalString;
    }


}
