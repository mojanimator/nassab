package com.mmr.nassab.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mmr.nassab.R;
import com.mmr.nassab.Util.Utils;

import java.util.List;

/**
 * Created by Mojtaba Rajabi on 23/12/2018.
 */

public class SpinnersAdapter extends ArrayAdapter {

    private final LayoutInflater mInflater;
    private final Context context;
    private List<String> items;

    public SpinnersAdapter(Context context, List<String> objects) {
        super(context, R.layout.row_users, 0, objects);
        this.context = context;
        items = objects;
        mInflater = LayoutInflater.from(context);


    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (v == null)
            v = mInflater.inflate(R.layout.row_spinner, parent, false);

        TextView textView = v.findViewById(R.id.tv_name);
        textView.setText(items.get(position));
//        Log.d(MainActivity.TAG, "createItemView: " + items.get(position));
//        textView.setBackgroundResource(R.drawable.primary_background);
//        textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

        textView.setTranslationY(+30);
        textView.animate().translationYBy(-30).start();
        textView.setAlpha(0.5f);
        textView.animate().alpha(1).start();

        Utils.overrideFonts(context, v);

        return v;
    }


}
