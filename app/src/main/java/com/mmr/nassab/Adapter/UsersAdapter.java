package com.mmr.nassab.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mmr.nassab.Model.Nassab;
import com.mmr.nassab.R;
import com.mmr.nassab.Util.CircleNetworkImageView;
import com.mmr.nassab.Util.NetUtils;
import com.mmr.nassab.Util.Utils;

import java.util.ArrayList;

/**
 * Created by Mojtaba Rajabi on 23/12/2018.
 */

public class UsersAdapter extends ArrayAdapter {

    private final LayoutInflater mInflater;
    private final Context context;
    private final boolean showName;
    private ArrayList<Nassab> users = new ArrayList<>();

    public UsersAdapter(Context context, ArrayList<Nassab> objects, boolean showName) {
        super(context, R.layout.row_users, 0, objects);
        this.context = context;
        users = objects;
        mInflater = LayoutInflater.from(context);
        this.showName = showName;
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
            v = mInflater.inflate(R.layout.row_users, parent, false);
        TextView textView = v.findViewById(R.id.tv_user_name);

        CircleNetworkImageView imageView = v.findViewById(R.id.civ_user);
        ConstraintLayout backStatus = v.findViewById(R.id.status_user);
        NetUtils.setImage(users.get(position).getImage(), imageView);

        if (users.get(position).getStatus().equals("1"))
            backStatus.setBackgroundResource(R.drawable.secondary_background);
        else
            backStatus.setBackgroundResource(R.drawable.uninstalled_background);

        if (showName)
            textView.setText(users.get(position).getName());
//        else {
//            textView.setVisibility(View.GONE);
//            ConstraintSet constraintSet = new ConstraintSet();
//            constraintSet.clone(backStatus);
//            constraintSet.connect(R.id.civ_user, ConstraintSet.RIGHT, R.id.status_user, ConstraintSet.RIGHT);
////            constraintSet.connect(R.id.imageView, ConstraintSet.TOP, R.id.status_user, ConstraintSet.TOP);
//            constraintSet.applyTo(backStatus);
//        }

        imageView.setTranslationY(+30);
        imageView.animate().translationYBy(-30).start();
        imageView.setAlpha(0.5f);
        imageView.animate().alpha(1).start();

        Utils.overrideFonts(context, v);

        return v;
    }


}
