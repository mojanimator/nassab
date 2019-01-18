package com.mmr.nassab.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mmr.nassab.Model.NassabFinancial;
import com.mmr.nassab.R;
import com.mmr.nassab.Util.CircleNetworkImageView;
import com.mmr.nassab.Util.NetUtils;
import com.mmr.nassab.Util.Utils;

import java.util.ArrayList;

/**
 * Created by Mojtaba Rajabi on 21/02/2018.
 */

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportViewHolder> implements Filterable, AdapterView.OnItemSelectedListener {


    private final CarsFilter mFilter;
    private final SharedPreferences sharedpreferences;

    private ArrayList<NassabFinancial> data;
    private ArrayList<NassabFinancial> filteredList;

    private Context context;
    private NetUtils netUtils;
    private NassabFinancial tmpData;
    private View view;
    private ClickListener clickListener;
    private String[] nassabIds;
    private int easyPrice, hardPrice;

    public ReportsAdapter(ArrayList<NassabFinancial> data, Context context) {
        this.data = data;

        this.context = context;
        mFilter = new CarsFilter(ReportsAdapter.this);
//        cars = NetUtils.getCars();
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        easyPrice = Integer.valueOf(sharedpreferences.getString("regular_price", "0"));
        hardPrice = Integer.valueOf(sharedpreferences.getString("hard_price", "0"));

    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.row_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportViewHolder holder, int position) {

        tmpData = data.get(position);

        holder.tvCode.setText(tmpData.getNassabId());
        holder.tvName.setText(tmpData.getNassabName());
        holder.tvEasyNum.setText(String.valueOf(tmpData.getEasyNasb()));
        holder.tvHardNum.setText(String.valueOf(tmpData.getHardNasb()));
        tmpData.setCredit(tmpData.getEasyNasb() * easyPrice + tmpData.getHardNasb() * hardPrice);
        holder.tvCredit.setText(String.valueOf(tmpData.getCredit()));


        NetUtils.setImage(tmpData.getNassabImage(), holder.ivImage);

//        Log.d(MainActivity.TAG, "setNassabImages: " + tmpData.getStatus() + "," + tmpData.getNassab_id());
//        if (tmpData.getStatus() == 2) {
//            setNassabImages(tmpData.getNassab_id(), holder.ivNassab1, holder.ivNassab2);
//            holder.tvGpsPos.setText(" محل نصب:" + tmpData.getGps_pos());
//
//        } else {
//            holder.tvGpsPos.setVisibility(View.GONE);
//        }

        holder.itemView.setTranslationY(+30);
        holder.itemView.animate().translationYBy(-30).start();
        holder.itemView.setAlpha(0.5f);
        holder.itemView.animate().alpha(1).start();


        holder.setIsRecyclable(false);
        Utils.overrideFonts(context, view);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public ArrayList<NassabFinancial> getFilteredList() {
        return filteredList;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public void onViewRecycled(ReportViewHolder holder) {
        if (holder.ivImage.getDrawable() == null)
            holder.ivImage.setImageResource(R.drawable.logo);
        super.onViewRecycled(holder);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    class ReportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tvCode;
        TextView tvName;
        TextView tvEasyNum;
        TextView tvHardNum;
        TextView tvCredit;

        CircleNetworkImageView ivImage;

        ReportViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            tvCode = itemView.findViewById(R.id.tv_code);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEasyNum = itemView.findViewById(R.id.tv_easy_num);
            tvHardNum = itemView.findViewById(R.id.tv_hard_num);
            tvCredit = itemView.findViewById(R.id.tv_credit);

            ivImage = itemView.findViewById(R.id.iv_image);


        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return true;
        }
    }

    public class CarsFilter extends Filter {
        private ReportsAdapter mAdapter;

        private CarsFilter(ReportsAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filteredList.addAll(data);
            } else {
                final String filterPattern = constraint.toString().trim();
                for (final NassabFinancial n : data) {
                    if (n.getNassabName().contains(filterPattern)) {
                        filteredList.add(n);
                    }
                }
            }
            System.out.println("Count Number " + filteredList.size());
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
//            System.out.println("Count Number 2 " + ((List<Car>) results.values).size());
            this.mAdapter.notifyDataSetChanged();
        }

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }
}
