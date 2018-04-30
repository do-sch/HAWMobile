package de.haw_landshut.hawmobile.schedule;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

public class UpOnlyAutoCompleteTextView extends AppCompatAutoCompleteTextView implements TextWatcher {

    private static final int HEIGHT = 700;

    public UpOnlyAutoCompleteTextView(Context context) {
        super(context);
    }

    public UpOnlyAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UpOnlyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void enableAutoHeight(){
        this.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


//    @Override
//    public void showDropDown() {
//        int pos[] = new int[2];
//        getLocationOnScreen(pos);
//
//        Rect displayFrame = new Rect();
//        getWindowVisibleDisplayFrame(displayFrame);
//
//        int spaceBelow = displayFrame.bottom - pos[1];
//        System.out.println("spaceBelow = " + spaceBelow);
//        System.out.println("getDropDownHeight() = " + getDropDownHeight());
//        System.out.println("spaceBelow > getDropDownHeight() = " + (spaceBelow > getDropDownHeight()));
//        if (spaceBelow > getDropDownHeight())
//            setDropDownHeight(HEIGHT);
//
//
//        super.showDropDown();
//    }

    @Override
    public void afterTextChanged(Editable s) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int[] pos = new int[2];
            getLocationOnScreen(pos);
            setDropDownVerticalOffset(-getHeight() * 2 - getDropDownHeight() * 2);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}


