package de.haw_landshut.hawmobile.schedule;

import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class OnClickLabel implements View.OnClickListener {
    @Override
    public void onClick(View view) {

        ScheduleFragment.currentTV = (TextView)view;
        if(ScheduleFragment.mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

        }
        else {
            ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }
    }
}
