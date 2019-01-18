package com.mmr.nassab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.mmr.nassab.Adapter.CarsAdapter;
import com.mmr.nassab.Adapter.SpinnersAdapter;
import com.mmr.nassab.Adapter.UsersAdapter;
import com.mmr.nassab.Model.Car;
import com.mmr.nassab.Model.Finance;
import com.mmr.nassab.Model.Nassab;
import com.mmr.nassab.Model.Report;
import com.mmr.nassab.Model.Util;
import com.mmr.nassab.Util.CircleNetworkImageView;
import com.mmr.nassab.Util.NetUtils;
import com.mmr.nassab.Util.PresetRadioGroup;
import com.mmr.nassab.Util.PresetValueButton;
import com.mmr.nassab.Util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.SSLContext;

import co.ronash.pushe.Pushe;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NetUtils.Communicator, AdapterView.OnItemSelectedListener {
    public static final String TAG = "_tagg";
    Context context;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String mActivityTitle = "تاژان نصب";
    public static ArrayList<Nassab> users;
    public static ArrayList<Car> cars;
    public static Util utils;
    SwipeRefreshLayout refreshcars;
    private CarsAdapter carsAdapter;
    private RecyclerView rvCars;
    private NetUtils netUtils;
    String where, whereSearch = "";
    String whereStatus = "0";
    String searchFilter = "numberplate";
    private GridLayoutManager manager;
    private ProgressBar progressBar;
    private View view;
    private CardView cvCarRow;
    PresetRadioGroup statusRadioGroup;
    private PresetValueButton tabInstalled, tabNotInstalled, tabInstalling, tabAll;
    private AppCompatAutoCompleteTextView etSearch;
    private Spinner spinnerFilterSearch;
    private ImageView etDelSearch;
    private UsersAdapter usersAdapter;
    private ListView gvUsers;
    private SharedPreferences sharedpreferences;
    private String myUsername;
    CircleNetworkImageView civ_me;
    boolean doubleBackToExitPressedOnce = false;
    private Handler mHandler = new Handler();
    FloatingActionButton fabAdd, fabReport;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };
    final Intent intent = new Intent();
    private SpinnerAdapter spinnerFilterSearchAdapter;
    Toolbar toolbatMain;
    private Spinner spinnerProjects;
    private SpinnersAdapter spinnerAdapterProjects;
    private String project;
    private String whereProject;
    private ImageButton iBSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);

        Pushe.initialize(this, true);

        toolbatMain = findViewById(R.id.toolbar_main);

        iBSettings = findViewById(R.id.imageButtonSettings);
        iBSettings.setOnClickListener(this);

        myUsername = sharedpreferences.getString("user_name", "");


        civ_me = findViewById(R.id.civ_me);

        civ_me.setOnClickListener(this);

        view = this.getWindow().getDecorView().findViewById(R.id.layout_main);
        context = this;


        cars = new ArrayList<Car>();
        users = new ArrayList<Nassab>();
        utils = new Util();

        usersAdapter = new UsersAdapter(this, users, false);
        gvUsers = findViewById(R.id.gv_users);
        gvUsers.setAdapter(usersAdapter);


        carsAdapter = new CarsAdapter(cars, context);
        carsAdapter.setOnItemClickListener(new CarsAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                try {


                    intent.setClass(getBaseContext(), CarActivity.class);

                    intent.putExtra("id", cars.get(position).getId());
                    intent.putExtra("name", cars.get(position).getName());
                    intent.putExtra("numberplate", cars.get(position).getNumberplate());
                    intent.putExtra("gps_imei", cars.get(position).getGps_imei());
                    intent.putExtra("gps_simcard", cars.get(position).getGps_simcard());
                    intent.putExtra("driver_name", cars.get(position).getDriver_name());
                    intent.putExtra("driver_simcard", cars.get(position).getDriver_simcard());
                    intent.putExtra("cluster", utils.getClusters().get(cars.get(position).getCluster()));
                    intent.putExtra("status", cars.get(position).getStatus());
                    intent.putExtra("gps_pos", cars.get(position).getGps_pos());

                    startActivity(intent);
                } catch (IndexOutOfBoundsException e) {

                }
            }

            @Override
            public void onItemLongClick(int position, View v) {
                Log.d(TAG, "onItemLongClick pos = " + position);
            }
        });

        netUtils = new NetUtils(this, carsAdapter, usersAdapter);
        netUtils.utils("get"); //get all utils
        netUtils.setCommunicator(this);
        netUtils.getUsers("1", myUsername); //get all users


        manager = new GridLayoutManager(context, 1);
        cvCarRow = findViewById(R.id.row_car);
        rvCars = findViewById(R.id.rv_cars);
        rvCars.setLayoutManager(manager);
        rvCars.setAdapter(carsAdapter);


        toolbar = findViewById(R.id.toolbar_cars);
        progressBar = findViewById(R.id.progressBar_project);
        progressBar.setProgress(10);

        refreshcars = findViewById(R.id.swipeRefreshCars);
        refreshcars.setColorSchemeResources(
                R.color.colorPrimaryDark,
                R.color.colorAccent,
                R.color.colorPrimaryDark);

        refreshcars.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                netUtils.getUsers("1", myUsername); //get all users
                netUtils.utils("get"); //get all utils
                search();
                refreshcars.setRefreshing(false);
            }
        });
        //tabs (filters)
        statusRadioGroup = findViewById(R.id.rg_status);

        statusRadioGroup.check(R.id.tab_installed);
        statusRadioGroup.check(R.id.tab_installing);
        statusRadioGroup.check(R.id.tab_all);
        statusRadioGroup.check(R.id.tab_not_Installed);

        statusRadioGroup.setOnCheckedChangeListener(new PresetRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View radioGroup, View radioButton, boolean isChecked, int checkedId) {
                switch (checkedId) {
                    case R.id.tab_installed:
                        whereStatus = "2";
                        break;
                    case R.id.tab_installing:
                        whereStatus = "1";
                        break;
                    case R.id.tab_not_Installed:
                        whereStatus = "0";
                        break;
                    case R.id.tab_all:
                        whereStatus = "";
                        break;
                }
                search();
            }

        });
//spinners
        spinnerProjects = findViewById(R.id.spinner_project);
        spinnerProjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                project = utils.getProjectNames().get(position);
//                Log.d(TAG, "p " + project);
                search();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerAdapterProjects = new SpinnersAdapter(this, utils.getProjectNames());
        spinnerProjects.setAdapter(spinnerAdapterProjects);


        spinnerFilterSearch = (Spinner) findViewById(R.id.spinner_filter_search);
        spinnerFilterSearchAdapter = new SpinnersAdapter(this, Arrays.asList(getResources().getStringArray(R.array.spinner_filter_items)));
        spinnerFilterSearch.setAdapter(spinnerFilterSearchAdapter);
        //        spinnerFilterSearch.setSelection(0);
        spinnerFilterSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        searchFilter = "numberplate";
                        break;
                    case 1:
                        searchFilter = "name";
                        break;
                    case 2:
                        searchFilter = "cluster-id";
                        break;
                    case 3:
                        searchFilter = "driver-name";
                        break;
                    case 4:
                        searchFilter = "driver-simcard";
                        break;
                    case 5:
                        searchFilter = "gps-imei";
                        break;
                    case 6:
                        searchFilter = "gps-simcard";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
// search
        etDelSearch = findViewById(R.id.iBDeleteSearch);
        etDelSearch.setOnClickListener(this);
        etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                whereSearch = s.toString();
                search();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH
//                        || actionId == EditorInfo.IME_ACTION_DONE
//                        || actionId == EditorInfo.IME_ACTION_GO
//                        || actionId == EditorInfo.IME_ACTION_NEXT
//                        || (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN)
//                        || (keyEvent != null && keyEvent.getAction() == KeyEvent.KEYCODE_ENTER)

                        ) {

                    search();
                }

                return false;
            }
        });

        fabAdd = findViewById(R.id.fab_add_car);
        fabReport = findViewById(R.id.fab_reports);

        fabAdd.setOnClickListener(this);
        fabReport.setOnClickListener(this);
//        Utils.changeAppbarFont(toolbar, this);
        Utils.overrideFonts(this, view);
        Utils.hideSoftKeyboard(this);


        try {
            SSLContext.getInstance("TLSv1.2");
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            Log.d(MainActivity.TAG, e.getMessage());
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), this, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(MainActivity.TAG, "Google Play Services not available.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void search() {

        where = whereMaker();
        cars.clear();
        carsAdapter.notifyDataSetChanged();
        netUtils.getCars(where, whereProject);


    }


    private String whereMaker() {
        whereProject = " `project-id` IN (SELECT `id` FROM `projects` WHERE `name` = '" + project + "' ) ";
        where = whereProject;
        if (whereStatus != "") {
            where += " and `status` = '" + whereStatus + "'";
            if (whereSearch != "" && whereSearch != null)
                if (searchFilter.equals("cluster-id")) //need search with id
                    where += " and `" + searchFilter + "` IN ( SELECT `id` FROM `clusters` WHERE `name`  LIKE '%" + whereSearch + "%' )";
                else
                    where += " and `" + searchFilter + "`  LIKE '%" + whereSearch + "%'";
        } else if (whereSearch != "" && whereSearch != null)
            if (searchFilter.equals("cluster-id")) //need search with id
                where += " and `" + searchFilter + "` IN ( SELECT `id` FROM `clusters` WHERE `name`  LIKE '%" + whereSearch + "%' )";
            else
                where += " and `" + searchFilter + "` LIKE '%" + whereSearch + "%'";
//        Log.d(MainActivity.TAG, where);
        return where;
    }

    @Override
    public void onResume() {
        super.onResume();

        search();
//        Log.d(MainActivity.TAG, "on resume");
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        // Associate searchable configuration with the SearchView
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_status:
                Toast.makeText(this, "Home Status Click", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Home Settings Click", Toast.LENGTH_SHORT).show();
                return true;
//            case R.id.action_with_icon:
//                Intent withicon = new Intent(this, TabWithIconActivity.class);
//                startActivity(withicon);
//                finish();
//                return true;
//            case R.id.action_without_icon:
//                Intent withouticon = new Intent(this, TabWOIconActivity.class);
//                startActivity(withouticon);
//                finish();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        callsFragment = new CarsFragment();
//        chatFragment = new ChatFragment();
//        contactsFragment = new ContactsFragment();
//        adapter.addFragment(callsFragment, "CALLS");
//        adapter.addFragment(chatFragment, "CHAT");
//        adapter.addFragment(contactsFragment, "CONTACTS");
//        viewPager.setAdapter(adapter);
//    }
//
//    private View prepareTabView(int pos) {
//        View view = getLayoutInflater().inflate(R.layout.custom_tab, null);
//        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
//        TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
//        tv_title.setText(tabTitle[pos]);
//        if (unreadCount[pos] > 0) {
//            tv_count.setVisibility(View.VISIBLE);
//            tv_count.setText("" + unreadCount[pos]);
//        } else
//            tv_count.setVisibility(View.GONE);
//
//
//        return view;
//    }
//
//    private void setupTabIcons() {
//
//        for (int i = 0; i < tabTitle.length; i++) {
//            /*TabLayout.Tab tabitem = tabLayout.newTab();
//            tabitem.setCustomView(prepareTabView(i));
//            tabLayout.addTab(tabitem);*/
//
//            tabLayout.getTabAt(i).setCustomView(prepareTabView(i));
//        }
//
//
//    }
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.fab_add_car:
                intent.setClass(context, AddEditCarActivity.class);
                startActivity(intent);
                break;
            case R.id.fab_reports:
//                if (myUsername.equals("admin")) {
                intent.setClass(context, ReportsActivity.class);
                startActivity(intent);
//                } else
//                    Utils.mToast(this, "مجاز نمی باشید", Toast.LENGTH_SHORT);
                break;
            case R.id.civ_me:
                Log.d(TAG, "onClick: me");
                break;
            case R.id.iBDeleteSearch:
                etSearch.setText("");
                whereSearch = "";
                search();
                break;
            case R.id.imageButtonSettings:
                if (myUsername.equals("admin")) {
                    intent.setClass(context, SettingsActivity.class);
                    startActivity(intent);
                } else
                    Utils.mToast(this, "مجاز نمی باشید", Toast.LENGTH_SHORT);

        }
    }

    @Override
    public void response(String response) {
        if (response.equals("utils200")) {
            spinnerAdapterProjects.notifyDataSetChanged();
//            Log.d(TAG, "notifyDataSetChanged: ");
        }

    }

    @Override
    public void response(String response, ArrayList<Report> reports, ArrayList<Finance> finances, ArrayList<Nassab> users) {

    }

    @Override
    public void usersReceived() {
        NetUtils.setImage(sharedpreferences.getString("user_image", ""), civ_me);

    }

    @Override
    public void usersLogout(String response) {
        if (response.equals("200")) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } else
            Utils.mToast(this, response, Toast.LENGTH_SHORT);

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
            netUtils.registerEditLoginLogout("logout", myUsername, "", "", "", "", "", ""); //change status to 0

            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Utils.mToast(this, "برای خروج دوباره کلیک کنید", Toast.LENGTH_SHORT);
        mHandler.postDelayed(mRunnable, 2000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

