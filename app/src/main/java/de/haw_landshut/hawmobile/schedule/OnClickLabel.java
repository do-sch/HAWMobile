package de.haw_landshut.hawmobile.schedule;

import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import de.haw_landshut.hawmobile.base.CustomTimetable;

public class OnClickLabel implements View.OnClickListener {
    private final ScheduleFragment fragment;
    OnClickLabel(ScheduleFragment fragment){
        this.fragment=fragment;
    }
    @Override
    public void onClick(View view) {

        fragment.setCurrentTV(((TextView) view));
        int currentNumber = fragment.getCurrentTvNumber();
        if(!fragment.isEven()){
            currentNumber+=30;
        }

        if(ScheduleFragment.mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
            CustomTimetable current = fragment.getTimetable().get(currentNumber);
            fragment.setCheckDouble(current.getFach().equals(fragment.getTimetable().get((currentNumber + 30) % 60).getFach()));
            fragment.setEnabledTextViews(false);

            if(fragment.isCopyActive()){
                String[] copy = fragment.getCopyable();
                fragment.setEt_fach_text(copy[0]);
                fragment.setEt_prof_text(copy[1]);
                fragment.setEt_raum_text(copy[2]);
                fragment.setColormaker(fragment.getCopyablecolor());
                fragment.wöchentl.setChecked(fragment.isWeeklyCopy());
                fragment.setCopyActive(false);
                fragment.callOnClick(fragment.edit);
                fragment.callOnClick(fragment.save);
               // ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
            else {
                fragment.setEt_raum_text(current.getRaum());
                fragment.setEt_prof_text(current.getProf());
                fragment.setEt_fach_text(current.getFach());
                fragment.setColormaker(current.getColor());

                if (fragment.isCheckDouble()) {
                    fragment.wöchentl.setChecked(true);
                } else {
                    fragment.wöchentl.setChecked(false);
                }

            }


        }
        else {
            ScheduleFragment.mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
            fragment.callOnClick(fragment.cancel);
        }
    }
}
