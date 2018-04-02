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
        int currentNumber = Integer.parseInt(ScheduleFragment.currentTV.getTag().toString());
        if(!ScheduleFragment.isEven){
            currentNumber+=30;
        }
        if(ScheduleFragment.mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
            CustomTimetable current = ScheduleFragment.timetable.get(currentNumber);
            ScheduleFragment.et_fach.setEnabled(false);
            ScheduleFragment.et_prof.setEnabled(false);
            ScheduleFragment.et_raum.setEnabled(false);

            if(ScheduleFragment.copyActive==true){
                ScheduleFragment.et_raum.setText(ScheduleFragment.copyable[2]);
                ScheduleFragment.et_prof.setText(ScheduleFragment.copyable[1]);
                ScheduleFragment.et_fach.setText(ScheduleFragment.copyable[0]);
                ScheduleFragment.colormaker=ScheduleFragment.copyablecolor;
                ScheduleFragment.wöchentl.setChecked(ScheduleFragment.weeklyCopy);
                if(ScheduleFragment.weeklyCopy==false){
                    ScheduleFragment.checkDouble=true;
                }
                ScheduleFragment.copyActive=false;
                ScheduleFragment.edit.callOnClick();
                ScheduleFragment.save.callOnClick();

                ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
            else {
                ScheduleFragment.et_fach.setText(current.getFach());
                ScheduleFragment.et_prof.setText(current.getProf());
                ScheduleFragment.et_raum.setText(current.getRaum());
                ScheduleFragment.colormaker = current.getColor();
                if (current.getFach().equals(ScheduleFragment.timetable.get((currentNumber + 30) % 60).getFach())) {
                    ScheduleFragment.wöchentl.setChecked(true);
                } else {
                    ScheduleFragment.wöchentl.setChecked(false);
                }

            }


        }
        else {
            ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }
    }
}
