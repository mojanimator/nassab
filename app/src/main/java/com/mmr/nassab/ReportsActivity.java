package com.mmr.nassab;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.View;

import com.mmr.nassab.Adapter.ReportsAdapter;
import com.mmr.nassab.Model.Finance;
import com.mmr.nassab.Model.Nassab;
import com.mmr.nassab.Model.NassabFinancial;
import com.mmr.nassab.Model.Report;
import com.mmr.nassab.Util.NetUtils;

import java.util.ArrayList;

public class ReportsActivity extends AppCompatActivity implements NetUtils.Communicator, ReportsAdapter.ClickListener {

    private NetUtils netUtils;
    private SharedPreferences sharedpreferences;
    private ArrayList<NassabFinancial> nassabFinancials = new ArrayList<>();
    private String[] nassabIds;
    private ReportsAdapter nassabFinancialsAdapter;
    private GridLayoutManager manager;
    private RecyclerView rvReports;
    private SwipeRefreshLayout refreshReports;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        netUtils = new NetUtils(this);
        netUtils.setCommunicator(this);

        nassabFinancialsAdapter = new ReportsAdapter(nassabFinancials, this);
        manager = new GridLayoutManager(this, 1);
        rvReports = findViewById(R.id.rv_reports);
        rvReports.setLayoutManager(manager);

        rvReports.setAdapter(nassabFinancialsAdapter);
        nassabFinancialsAdapter.setOnItemClickListener(new ReportsAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });


        refreshReports = findViewById(R.id.swipeRefreshCarsEdit);
        refreshReports.setColorSchemeResources(
                R.color.colorPrimaryDark,
                R.color.colorAccent,
                R.color.colorPrimaryDark);
        refreshReports.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                netUtils.getReports("nassabha");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.d(MainActivity.TAG, "onResume: ");
        netUtils.getReports("nassabha");
    }

    @Override
    public void response(String response) {

    }

    @Override
    public void response(String response, ArrayList<Report> reports, ArrayList<Finance> finances, ArrayList<Nassab> users) {

        NassabFinancial tmp;
        SparseIntArray idToIndex = new SparseIntArray();
        int i = 0;
        int idx;
        if (response.equals("reports200")) {
            nassabFinancials.clear();

            for (Nassab n : users) {
                tmp = new NassabFinancial();
                tmp.setNassabId(String.valueOf(n.getId()));
                tmp.setNassabName(n.getName());
                tmp.setNassabImage(n.getImage());
                for (Finance f : finances)
                    if (f.getPerson_id().equals(tmp.getNassabId()))
                        tmp.setCredit(tmp.getCredit() - f.getPrice());
                nassabFinancials.add(tmp);

                idToIndex.put(n.getId(), i++);
            }
            for (Report r : reports) {
                nassabIds = r.getPerson_id().split("@");
                for (String id : nassabIds) {
                    idx = idToIndex.get(Integer.valueOf(id));
                    nassabFinancials.get(idx).plusNasb(r.getHardness(), nassabIds.length);

                }
            }

            nassabFinancialsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void usersReceived() {

    }

    @Override
    public void usersLogout(String response) {

    }


    @Override
    public void onItemClick(int position, View v) {

    }

    @Override
    public void onItemLongClick(int position, View v) {

    }
}
