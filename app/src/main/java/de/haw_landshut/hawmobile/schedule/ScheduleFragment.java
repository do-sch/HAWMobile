package de.haw_landshut.hawmobile.schedule;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import de.haw_landshut.hawmobile.R;
import org.w3c.dom.Text;

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
