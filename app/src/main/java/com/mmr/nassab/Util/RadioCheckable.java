package com.mmr.nassab.Util;

import android.view.View;
import android.widget.Checkable;

/**
 * Created by Mojtaba Rajabi on 18/12/2018.
 */

public interface RadioCheckable extends Checkable {
    void addOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener);

    void removeOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener);

    public static interface OnCheckedChangeListener {
        void onCheckedChanged(View radioGroup, boolean isChecked);
    }
}
