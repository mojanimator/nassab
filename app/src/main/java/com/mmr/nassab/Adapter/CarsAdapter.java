package com.mmr.nassab.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.mmr.nassab.MainActivity;
import com.mmr.nassab.Model.Car;
import com.mmr.nassab.Model.Nassab;
import com.mmr.nassab.R;
import com.mmr.nassab.Util.CircleNetworkImageView;
import com.mmr.nassab.Util.NetUtils;
import com.mmr.nassab.Util.Utils;

import java.util.ArrayList;

/**
 * Created by Mojtaba Rajabi on 21/02/2018.
 */

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.CarViewHolder> implements Filterable {


    private final CarsFilter mFilter;
    private final SharedPreferences sharedpreferences;
    private final String myUserId, myUserImage;
    private ArrayList<Car> data;
    private ArrayList<Car> filteredList;
    private Context context;
    private NetUtils netUtils;
    private Car tmpData;
    private View view;
    private ClickListener clickListener;
    String[] nassabIds;

    public CarsAdapter(ArrayList<Car> data, Context context) {
        this.data = data;
        this.context = context;
        mFilter = new CarsFilter(CarsAdapter.this);
//        cars = NetUtils.getCars();
        sharedpreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);

        myUserId = String.valueOf(sharedpreferences.getInt("user_id", -1));
        myUserImage = sharedpreferences.getString("user_image", "");
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.row_cars, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {

        tmpData = data.get(position);

        holder.tvCode.setText(tmpData.getId());
        holder.tvName.setText(tmpData.getName());
        holder.tvNumberPlate.setText(tmpData.getNumberplate());
        holder.tvGps_imei.setText(tmpData.getGps_imei());
        holder.tvGps_simcard.setText(tmpData.getGps_simcard());
        holder.tvDriver_name.setText(tmpData.getDriver_name());
        holder.tvDriver_simcard.setText(tmpData.getDriver_simcard());
        holder.tvCluster.setText(MainActivity.utils.getClusters().get(tmpData.getCluster()));

        NetUtils.setImage(tmpData.getStatus(), holder.ivStatus);

//        Log.d(MainActivity.TAG, "setNassabImages: " + tmpData.getStatus() + "," + tmpData.getNassab_id());
        if (tmpData.getStatus() != 0)
            setNassabImages(tmpData.getNassab_id(), holder.ivNassab1, holder.ivNassab2);

        holder.itemView.setTranslationY(+30);
        holder.itemView.animate().translationYBy(-30).start();
        holder.itemView.setAlpha(0.5f);
        holder.itemView.animate().alpha(1).start();


        holder.setIsRecyclable(false);
        Utils.overrideFonts(context, view);

    }

    private void setNassabImages(String nassab_id, NetworkImageView ivNassab1, NetworkImageView ivNassab2) {

        nassabIds = nassab_id.split("@");
//        Log.d(MainActivity.TAG, "nassab id " + nassabIds[0]);

        if (nassabIds[0].equals(myUserId)) {
            NetUtils.setImage(myUserImage, ivNassab1);
        } else
            for (Nassab n : MainActivity.users) {

                if (nassabIds[0].equals(String.valueOf(n.getId()))) {

                    NetUtils.setImage(n.getImage(), ivNassab1);
                    break;
                }
            }
        if (nassabIds.length > 1) {
            if (nassabIds[1].equals(myUserId)) {
                NetUtils.setImage(myUserImage, ivNassab2);
            } else
                for (Nassab n : MainActivity.users) {
                    if (nassabIds[1].equals(String.valueOf(n.getId()))) {

                        NetUtils.setImage(n.getImage(), ivNassab2);
                        break;
                    }
                }
        }


    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public ArrayList<Car> getFilteredList() {
        return filteredList;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public void onViewRecycled(CarViewHolder holder) {
        if (holder.ivStatus.getDrawable() == null)
            holder.ivStatus.setImageResource(R.drawable.logo);
        super.onViewRecycled(holder);
    }


    class CarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tvCode;
        TextView tvName;
        TextView tvNumberPlate;
        TextView tvGps_imei;
        TextView tvGps_simcard;
        TextView tvDriver_name;
        TextView tvDriver_simcard;
        TextView tvCluster;

        ImageView ivStatus;
        CircleNetworkImageView ivNassab1;
        CircleNetworkImageView ivNassab2;

        CarViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            tvCode = itemView.findViewById(R.id.tv_code);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumberPlate = itemView.findViewById(R.id.tv_numberplate);
            tvGps_imei = itemView.findViewById(R.id.tv_imei);
            tvGps_simcard = itemView.findViewById(R.id.tv_gps_simcard);
            tvDriver_name = itemView.findViewById(R.id.tv_driver_name);
            tvDriver_simcard = itemView.findViewById(R.id.tv_driver_simcard);
            tvCluster = itemView.findViewById(R.id.tv_cluster);
            ivStatus = itemView.findViewById(R.id.iv_status);
            ivNassab1 = itemView.findViewById(R.id.iv_nassab1);
            ivNassab2 = itemView.findViewById(R.id.iv_nassab2);


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
        private CarsAdapter mAdapter;

        private CarsFilter(CarsAdapter mAdapter) {
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
                for (final Car car : data) {
                    if (car.getName().contains(filterPattern)) {
                        filteredList.add(car);
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
