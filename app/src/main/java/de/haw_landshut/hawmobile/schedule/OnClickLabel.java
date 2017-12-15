package de.haw_landshut.hawmobile.schedule;

import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import de.haw_landshut.hawmobile.base.CustomTimetable;

public class OnClickLabel implements View.OnClickListener {
    @Override
    public void onClick(View view) {

        ScheduleFragment.currentTV = (TextView)view;
        if(ScheduleFragment.mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
            CustomTimetable current = ScheduleFragment.timetable.get(Integer.parseInt(ScheduleFragment.currentTV.getTag().toString()));
            ScheduleFragment.et_fach.setText(current.getFach());
            ScheduleFragment.et_prof.setText(current.getProf());
            ScheduleFragment.et_raum.setText(current.getRaum());


        }
        else {
            ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }
    }
}
