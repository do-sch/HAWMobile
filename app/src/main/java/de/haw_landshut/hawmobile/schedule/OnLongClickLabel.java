package de.haw_landshut.hawmobile.schedule;

import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import de.haw_landshut.hawmobile.R;
import de.haw_landshut.hawmobile.base.CustomTimetable;

public class OnLongClickLabel implements View.OnLongClickListener {
    @Override
    public boolean onLongClick(View view) {
        ScheduleFragment.currentTV = (TextView)view;
        int currentNumber = Integer.parseInt(ScheduleFragment.currentTV.getTag().toString());
        return false;
    }

}
