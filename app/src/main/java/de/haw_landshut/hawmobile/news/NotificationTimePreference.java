package de.haw_landshut.hawmobile.news;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import de.haw_landshut.hawmobile.R;

/**
 * Created by X on 13.03.2018.
 */

public class NotificationTimePreference extends DialogPreference {
    NumberPicker n1, n2;
    private int value;

    public NotificationTimePreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        n1 = (NumberPicker) v.findViewById(R.id.numberPicker);
        n1.setMinValue(0);
        n1.setMaxValue(23);
        n1.setValue(getValue() / 100);

        n2 = (NumberPicker) v.findViewById(R.id.numberPicker2);
        n2.setMinValue(0);
        n2.setMaxValue(59);
        n2.setValue(getValue() % 100);
    }


    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(600) : (Integer) defaultValue);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            n1.clearFocus();
            n2.clearFocus();
            int newValue = n1.getValue() * 100 + n2.getValue();
            if (callChangeListener(newValue)) {
                setValue(newValue);
                if(n2.getValue() < 10)
                    setSummary("Täglich um " + n1.getValue() +":0"+ n2.getValue() + " Uhr");
                else
                    setSummary("Täglich um " + n1.getValue() +":"+ n2.getValue() + " Uhr");
            }
        }
    }

    public void setValue(int value) {
        this.value = value;
        persistInt(this.value);
    }

    public int getValue() {
        return this.value;
    }
}
