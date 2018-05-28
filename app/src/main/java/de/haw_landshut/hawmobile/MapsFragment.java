package de.haw_landshut.hawmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import android.app.Fragment;

//import android.support.v4.app.Fragment;

public class MapsFragment extends Fragment {


    private final Handler handler = new Handler();
    private Runnable runPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getContext( fragment
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runPager);
    }

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
        runPager = new Runnable() {

            @Override
            public void run() {
                getFragmentManager().beginTransaction().add(R.id.linearContainer, ChildFragment.newInstance()).commit();
            }
        };
        handler.post(runPager);
    }

}
