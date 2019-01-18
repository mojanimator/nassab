package com.mmr.nassab.Util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.mmr.nassab.Adapter.CarsAdapter;
import com.mmr.nassab.Adapter.UsersAdapter;
import com.mmr.nassab.LoginActivity;
import com.mmr.nassab.MainActivity;
import com.mmr.nassab.Model.Car;
import com.mmr.nassab.Model.Finance;
import com.mmr.nassab.Model.Nassab;
import com.mmr.nassab.Model.Report;
import com.mmr.nassab.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mmr on 22/02/2018.
 */

public class NetUtils {
    private static final String TAG = "_tagg";
    private static final String DOMAIN = "https://moj-raj.ir/nassab/";
    private static final String IMAGE_BASE_URL = DOMAIN + "img/";
    private static final String DATABASE_URL = DOMAIN + "show.php";
    private static final String DATABASE_URL_EDIT = DOMAIN + "edit.php";
    private static final String DATABASE_URL_USER = DOMAIN + "user.php";
    private static final String DATABASE_URL_CAR = DOMAIN + "car.php";
    private static final String DATABASE_URL_UTIL = DOMAIN + "util.php";
    private static final String DATABASE_URL_REPORT = DOMAIN + "report.php";
    private SharedPreferences sharedpreferences;
    private UsersAdapter usersAdapter;
    private TextView tvPercent;
    private ProgressBar progressBar;
    private PresetValueButton tabInstalled, tabNotInstalled, tabInstalling, tabAll;
    private final AppCompatActivity activity;


    private CarsAdapter carsAdapter;
    private static ImageLoader imageLoader;
    private Context mCtx;
    private Map<String, String> params = new HashMap<String, String>();
    private ProgressBar progressBarCars, pbRegisterLogin;
    private Communicator communicator;
    private Car tmpCar = new Car();
    private Nassab tmpUser = new Nassab();
    private SwipeRefreshLayout refreshcars, refreshcarsEdit;
    public static int installedCount, installingCount, notInstalledCount, allCount = 0;
    private static boolean loadImageFinished = false;
    private Report tmpReport;
    private Finance tmpFinance;
    private ArrayList<Nassab> users;

    public NetUtils(Context context, CarsAdapter carsAdapter, UsersAdapter usersAdapter) {
        this.mCtx = context;
        activity = (AppCompatActivity) context;
        this.carsAdapter = carsAdapter;
        this.usersAdapter = usersAdapter;
        progressBarCars = activity.findViewById(R.id.progressCars);
        refreshcars = activity.findViewById(R.id.swipeRefreshCars);


        tabInstalled = activity.findViewById(R.id.tab_installed);
        tabNotInstalled = activity.findViewById(R.id.tab_not_Installed);
        tabInstalling = activity.findViewById(R.id.tab_installing);
        tabAll = activity.findViewById(R.id.tab_all);
        progressBar = activity.findViewById(R.id.progressBar_project);
        tvPercent = activity.findViewById(R.id.tvPercent);


        imageLoader = VolleySingleton.getInstance(mCtx).getImageLoader();

        sharedpreferences = this.mCtx.getSharedPreferences("settings", MODE_PRIVATE);
    }

    public NetUtils(Context context) {
        activity = (AppCompatActivity) context;
        mCtx = context;
        refreshcarsEdit = activity.findViewById(R.id.swipeRefreshCarsEdit);
        pbRegisterLogin = activity.findViewById(R.id.pbRegister_login);

        if (pbRegisterLogin != null)
            pbRegisterLogin.getIndeterminateDrawable()
                    .setColorFilter(ContextCompat.getColor(mCtx, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void getReports(final String requestType) {

        if (!isNetworkAvailable(activity)) {
            Utils.mToast(mCtx, "اتصال اینترنت برقرار نیست!", Toast.LENGTH_SHORT);

            return;
        }

        VolleySingleton.getInstance(mCtx).cancelPendingRequests(requestType);

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, DATABASE_URL_REPORT,
                new Response.Listener<String>() {
                    private JSONArray tmp;

                    @Override
                    public void onResponse(String response) {

                        try {
//                            Log.d(TAG, "resp " + response);

                            HashMap<String, String> param = new HashMap<>();
                            JSONArray rsp;
                            JSONObject jsonObject;


                            ArrayList<Report> reports;
                            ArrayList<Finance> finances;

                            reports = new ArrayList<>();
                            finances = new ArrayList<>();
                            users = new ArrayList<>();

                            if (response != null && !response.equals("")) {
//                                Log.d(TAG, response);
//                                rsp = new JSONArray(response);

                                tmp = new JSONArray();
                                jsonObject = new JSONObject(response);


                                tmp = jsonObject.getJSONArray("reports");
                                for (int i = 0; i < tmp.length(); i++) {
                                    tmpReport = new Report();

                                    tmpReport.setGPSId(tmp.getJSONObject(i).getString("gps-id"));
                                    tmpReport.setPerson_id(tmp.getJSONObject(i).getString("person-id"));
                                    tmpReport.setHardness(tmp.getJSONObject(i).getString("hardness"));
                                    tmpReport.setDate(tmp.getJSONObject(i).getString("date"));

                                    reports.add(tmpReport);
                                }

                                tmp = jsonObject.getJSONArray("finances");
                                for (int i = 0; i < tmp.length(); i++) {
                                    tmpFinance = new Finance();

                                    tmpFinance.setId(tmp.getJSONObject(i).getString("id"));
                                    tmpFinance.setPerson_id(tmp.getJSONObject(i).getString("person-id"));
                                    tmpFinance.setPrice(tmp.getJSONObject(i).getDouble("price"));
                                    tmpFinance.setDescription(tmp.getJSONObject(i).getString("description"));
                                    tmpFinance.setDate(tmp.getJSONObject(i).getString("date"));

                                    finances.add(tmpFinance);
                                }

                                tmp = jsonObject.getJSONArray("nassabs");
                                for (int i = 0; i < tmp.length(); i++) {
                                    tmpUser = new Nassab();
                                    tmpUser.setId(tmp.getJSONObject(i).getInt("id"));
                                    tmpUser.setName(tmp.getJSONObject(i).getString("name"));
                                    tmpUser.setPhone(tmp.getJSONObject(i).getString("phone"));
                                    tmpUser.setCard(tmp.getJSONObject(i).getString("card"));
                                    tmpUser.setStatus(tmp.getJSONObject(i).getString("status"));
                                    tmpUser.setImage(tmp.getJSONObject(i).getString("image"));

                                    users.add(tmpUser);
                                }

                                showHideRefresh(false);

                                communicator.response("reports200", reports, finances, users);
                            }
//
                        } catch (JSONException e) {
                            Log.d(TAG, "JSONException " + e.getMessage());
                            showHideRefresh(false);
                        }

                    }
                },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.mToast(mCtx, " خطا " + '\n' + error.getMessage(), Toast.LENGTH_SHORT);
                    }
                })

        {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                params.put("request_type", requestType);


                return params;
            }
        };
        jsonArrayRequest.setTag(requestType);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(2500, 4, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(mCtx).addToRequestQueue(jsonArrayRequest);

    }

    public void utils(final String requestType) {

        if (!isNetworkAvailable(activity)) {
            Utils.mToast(mCtx, "اتصال اینترنت برقرار نیست!", Toast.LENGTH_SHORT);

            return;
        }

        VolleySingleton.getInstance(mCtx).cancelPendingRequests(requestType);

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, DATABASE_URL_UTIL,
                new Response.Listener<String>() {
                    private JSONArray tmp;

                    @Override
                    public void onResponse(String response) {

                        try {
//                            Log.d(TAG, "resp " + response);

                            HashMap<String, String> param = new HashMap<>();
                            JSONArray rsp;
                            JSONObject jsonObject;

                            if (response != null && !response.equals("")) {
//                                Log.d(TAG, response);
//                                rsp = new JSONArray(response);

                                tmp = new JSONArray();
                                jsonObject = new JSONObject(response);
                                MainActivity.utils.clearAll();

                                tmp = jsonObject.getJSONArray("devices");
                                for (int i = 0; i < tmp.length(); i++) {
                                    MainActivity.utils.addDevices(tmp.getJSONObject(i).getString("id"), tmp.getJSONObject(i).getString("name"));
                                    MainActivity.utils.addDevicesReverse(tmp.getJSONObject(i).getString("name"), tmp.getJSONObject(i).getString("id"));
                                    MainActivity.utils.getDeviceNames().add(tmp.getJSONObject(i).getString("name"));
                                }
                                tmp = jsonObject.getJSONArray("clusters");
                                for (int i = 0; i < tmp.length(); i++) {
                                    MainActivity.utils.addClusters(tmp.getJSONObject(i).getString("id"), tmp.getJSONObject(i).getString("name"));
                                    MainActivity.utils.addClustersReverse(tmp.getJSONObject(i).getString("name"), tmp.getJSONObject(i).getString("id"));
                                    MainActivity.utils.getClusterNames().add(tmp.getJSONObject(i).getString("name"));
                                }
                                tmp = jsonObject.getJSONArray("projects");
                                for (int i = 0; i < tmp.length(); i++) {
                                    MainActivity.utils.addProjects(tmp.getJSONObject(i).getString("id"), tmp.getJSONObject(i).getString("name"));
                                    MainActivity.utils.addProjectsReverse(tmp.getJSONObject(i).getString("name"), tmp.getJSONObject(i).getString("id"));
                                    MainActivity.utils.getProjectNames().add(tmp.getJSONObject(i).getString("name"));
                                }
                                showHideRefresh(false);
                                communicator.response("utils200");
                            }
//
                        } catch (JSONException e) {
                            //Toast.makeText(mCtx, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "JSONException utils" + e.getMessage());
//                            Utils.mToast(mCtx, "خطایی رخ داد", Toast.LENGTH_SHORT);
                        }

                    }
                },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Utils.mToast(mCtx, " خطا " + '\n' + error.getMessage(), Toast.LENGTH_SHORT);
                    }
                })

        {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                params.put("request_type", requestType);
                params.put("where", "1");

                return params;
            }
        };
        jsonArrayRequest.setTag(requestType);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(2500, 4, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(mCtx).addToRequestQueue(jsonArrayRequest);

    }

    public void getCars(final String where, final String whereProject) {

        if (!isNetworkAvailable(activity)) {
            Utils.mToast(mCtx, "اتصال اینترنت برقرار نیست!", Toast.LENGTH_SHORT);

            return;
        }
        refreshcars.setRefreshing(true);
        installedCount = notInstalledCount = installingCount = allCount = 0;

        progressBarCars.setVisibility(View.VISIBLE);
        VolleySingleton.getInstance(mCtx).cancelPendingRequests("cars");

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, DATABASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

//                            Log.d(TAG, response);

                            JSONArray rsp;
                            JSONObject jsonObject;
                            long len;

                            if (response != null && !response.equals("")) {
//                                Log.d(TAG, response);
                                rsp = new JSONArray(response);
                                len = rsp.length();

                                notInstalledCount = rsp.getJSONObject(0).getInt("total");
                                installingCount = rsp.getJSONObject(1).getInt("total");
                                installedCount = rsp.getJSONObject(2).getInt("total");
                                allCount = notInstalledCount + installingCount + installedCount;
                                if (allCount != 0) {
                                    progressBar.setProgress(installedCount * 100 / allCount);
                                    tvPercent.setText(installedCount * 100 / allCount + " %");
                                } else {
                                    progressBar.setProgress(0);
                                    tvPercent.setText(0 + " %");
                                }

                                for (int i = 3; i < len; i++) {

                                    jsonObject = rsp.getJSONObject(i);
                                    tmpCar = new Car();
                                    tmpCar.setId(jsonObject.getString("id"));
                                    tmpCar.setName(jsonObject.getString("name"));
                                    tmpCar.setNumberplate(jsonObject.getString("numberplate"));
                                    tmpCar.setGps_imei(jsonObject.getString("gps-imei"));
                                    tmpCar.setGps_simcard(jsonObject.getString("gps-simcard"));
                                    tmpCar.setDriver_name(jsonObject.getString("driver-name"));
                                    tmpCar.setDriver_simcard(jsonObject.getString("driver-simcard"));
                                    tmpCar.setCluster(jsonObject.getString("cluster-id"));
                                    tmpCar.setStatus((short) jsonObject.getInt("status"));
                                    tmpCar.setNassab_id(jsonObject.getString("nassab-id"));
                                    tmpCar.setGps_pos(jsonObject.getString("gps-pos"));

//                                    Log.d(MainActivity.TAG, tmpCar.getStatus() + "");
                                    MainActivity.cars.add(tmpCar);
                                }
                                if (MainActivity.cars.size() == 0)
                                    Utils.mToast(mCtx, "نتیجه ای یافت نشد!", Toast.LENGTH_SHORT);

                                else
                                    Utils.mToast(mCtx, "با موفقیت بارگذاری شد!", Toast.LENGTH_SHORT);
                                setCountLabels();


                            } else {
                                MainActivity.cars.clear();
                                Utils.mToast(mCtx, "نتیجه ای یافت نشد", Toast.LENGTH_SHORT);
                            }


                            carsAdapter.notifyDataSetChanged();
//                            communicator.refreshMapFragment(FragmentBillboardsList.billboards);
                            //pages are every 10 item
                            refreshcars.setRefreshing(false);
                        } catch (JSONException e) {

                            //Toast.makeText(mCtx, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "JSONException " + e.getMessage());
                            Utils.mToast(mCtx, "خطایی رخ داد", Toast.LENGTH_SHORT);
                            carsAdapter.notifyDataSetChanged();
//                            communicator.refreshMapFragment(FragmentBillboardsList.billboards);
                            refreshcars.setRefreshing(false);

                        }

                        refreshcars.setRefreshing(false);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, "error response " + error.getMessage());
                        refreshcars.setRefreshing(false);
                        Utils.mToast(mCtx, " خطا " + '\n' + error.getMessage(), Toast.LENGTH_SHORT);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                params.put("request_type", "cars");
                params.put("where", where);
                params.put("where_project", whereProject);

                return params;
            }
        };
//        MainActivity.cars.clear();
        jsonArrayRequest.setTag("cars");
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 4, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(mCtx).addToRequestQueue(jsonArrayRequest);


    }

    public void addBatchCars(final JSONObject data) {


        if (!isNetworkAvailable(activity)) {
            Utils.mToast(mCtx, "اتصال اینترنت برقرار نیست!", Toast.LENGTH_SHORT);
            return;
        }
        showHideRefresh(true);
        // make a post request to the server
        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, DATABASE_URL_CAR, new Response.Listener<String>() {
            private JSONObject rsp;

            @Override
            public void onResponse(String response) {

//                showHideRefresh(false);
//                try {
//                Log.d(TAG, "res " + response);
                communicator.response(response);
//
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.mToast(mCtx, error.toString(), Toast.LENGTH_SHORT);
                        Log.d(TAG, "onErrorResponse: " + error);
                        showHideRefresh(false);
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("request_type", "add_batch");
                params.put("data", data.toString());

                return params;
            }
        };
        VolleySingleton.getInstance(mCtx).cancelPendingRequests("car_batch");

        stringPostRequest.setTag("car_batch");
        stringPostRequest.setRetryPolicy(new
                DefaultRetryPolicy(10000, 4, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(mCtx).addToRequestQueue(stringPostRequest);

        showHideRefresh(true);
    }

    public void registerEditLoginLogout(final String requestType, final String username, final String password, final String imageSrc,
                                        final String card, final String phone, final String device, final String pusheId) {

        if (!isNetworkAvailable(activity)) {
            Utils.mToast(mCtx, "اتصال اینترنت برقرار نیست!", Toast.LENGTH_SHORT);

            return;
        }
        // make a post request to the server
        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, DATABASE_URL_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                showHideRefresh(false);

                if (requestType.equals("register")) {

                    if (response.equals("200")) {
                        Utils.mToast(mCtx, "ثبت نام موفق", Toast.LENGTH_SHORT);

                        Intent intent = new Intent();
                        intent.setClass(mCtx, LoginActivity.class);
                        activity.startActivity(intent);
                    } else
                        Utils.mToast(mCtx, response, Toast.LENGTH_SHORT);

                } else if (requestType.equals("login")) {
                    if (response.equals("200")) {

                        Intent intent = new Intent();
                        intent.setClass(mCtx, MainActivity.class);

                        SharedPreferences.Editor editor = mCtx.getSharedPreferences("settings", MODE_PRIVATE).edit();
                        editor.putString("user_name", username);
                        editor.apply();
                        activity.startActivity(intent);
                    } else
                        Utils.mToast(mCtx, response, Toast.LENGTH_SHORT);
                } else if (requestType.equals("logout")) {

                    communicator.usersLogout(response);
                }

                Log.d(TAG, "res " + response);
//
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.mToast(mCtx, error.toString(), Toast.LENGTH_SHORT);
                        Log.d(TAG, "onErrorResponse: " + error);
                        showHideRefresh(false);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("request_type", requestType);
                params.put("image_src", imageSrc);
                params.put("username", username);
                params.put("password", password);
                params.put("phone", phone);
                params.put("card", card);
                params.put("device", device);
                params.put("pushe_id", pusheId);
                return params;
            }
        };
        VolleySingleton.getInstance(mCtx).cancelPendingRequests("user_register");

        stringPostRequest.setTag("user_register");
        stringPostRequest.setRetryPolicy(new
                DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(mCtx).addToRequestQueue(stringPostRequest);

        showHideRefresh(true);
    }


    public void editCar(Context ctx, final String requestType, final String set, final String car_id, final String user_id,
                        final String gps_imei, final String hardness
            , final String car_name, final String car_numberplate, final String gps_simcard, final String driver_name,
                        final String driver_simcard, final String cluster, final String device_id, final String project_id, final String gps_pos) {

        final Context tmpContext;
        if (ctx != null)
            tmpContext = ctx;
        else
            tmpContext = mCtx;


        if (!isNetworkAvailable(activity)) {
            Utils.mToast(tmpContext, "اتصال اینترنت برقرار نیست!", Toast.LENGTH_SHORT);
            return;
        }
        showHideRefresh(true);
        VolleySingleton.getInstance(tmpContext).cancelPendingRequests("cars_edit");

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, DATABASE_URL_CAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, "respon " + response);

                        showHideRefresh(false);
//                        if (response.equals("200"))
                        communicator.response(response);


////                            Utils.mToast(tmpContext, "با موفقیت بروزرسانی شد!", Toast.LENGTH_SHORT);
//
//                        } else {
//
////                            Utils.mToast(tmpContext, "خطایی رخ داد", Toast.LENGTH_SHORT);
//                        }
//                        communicator.response(response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: ");
                        Log.d(TAG, error.getMessage());
                        showHideRefresh(false);
                        communicator.response(error + "");
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                Log.d(TAG, set + ":" + car_id + ":" + user_id + ":" + gps_id + ":" + hardness);

                params.put("request_type", requestType);

                params.put("car_id", car_id);
                params.put("set", set);
                params.put("user_id", user_id);
                params.put("gps_imei", gps_imei);
                params.put("hardness", hardness);
                params.put("car_name", car_name);
                params.put("car_numberplate", car_numberplate);
                params.put("gps_simcard", gps_simcard);
                params.put("driver_name", driver_name);
                params.put("driver_simcard", driver_simcard);
                params.put("cluster", cluster);
                params.put("device", device_id);
                params.put("project", project_id);
                params.put("gps_pos", gps_pos);


                return params;
            }
        };
        jsonArrayRequest.setTag("cars_edit");
        jsonArrayRequest.setRetryPolicy(new
                DefaultRetryPolicy(2500, 4, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(tmpContext).addToRequestQueue(jsonArrayRequest);

    }

    public void getUsers(final String where, final String myUsername) {
        if (!isNetworkAvailable(activity)) {

            return;
        }
        MainActivity.users.clear();


        VolleySingleton.getInstance(mCtx).cancelPendingRequests("users");

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, DATABASE_URL_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Log.d(TAG, "users " + response);
                        try {

                            JSONArray rsp;
                            JSONObject jsonObject;
                            long len;

                            if (response != null && !response.equals("")) {

                                rsp = new JSONArray(response);
                                len = rsp.length();

                                for (int i = 0; i < len; i++) {

                                    jsonObject = rsp.getJSONObject(i);
                                    tmpUser = new Nassab();
                                    tmpUser.setId(jsonObject.getInt("id"));
                                    tmpUser.setName(jsonObject.getString("name"));
                                    tmpUser.setPhone(jsonObject.getString("phone"));
                                    tmpUser.setCard(jsonObject.getString("card"));
                                    tmpUser.setStatus(jsonObject.getString("status"));
                                    tmpUser.setImage(jsonObject.getString("image"));
                                    tmpUser.setPush_id(jsonObject.getString("push-id"));


                                    if (tmpUser.getName().equals(myUsername)) {
                                        SharedPreferences.Editor editor = mCtx.getSharedPreferences("settings", MODE_PRIVATE).edit();
                                        editor.putInt("user_id", tmpUser.getId());
                                        editor.putString("user_image", tmpUser.getImage());
                                        editor.apply();

                                    } else
                                        MainActivity.users.add(tmpUser);
                                }
                            }

                            usersAdapter.notifyDataSetChanged();

                            communicator.usersReceived();

                        } catch (JSONException e) {

                            //Toast.makeText(mCtx, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "JSONException " + e.getMessage());

                            usersAdapter.notifyDataSetChanged();
//                            communicator.refreshMapFragment(FragmentBillboardsList.billboards);

                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, "error response " + error.getMessage());
//                        refreshcars.setRefreshing(false);
//                        Utils.mToast(mCtx, " خطا " + '\n' + error.getMessage(), Toast.LENGTH_SHORT);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                params.put("request_type", "users");
                params.put("where", where);

                return params;
            }
        };
        jsonArrayRequest.setTag("users");
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(2500, 4, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(mCtx).addToRequestQueue(jsonArrayRequest);

    }

    private void setCountLabels() {
        tabInstalled.setCount(String.valueOf(installedCount));
        tabNotInstalled.setCount(String.valueOf(notInstalledCount));
        tabInstalling.setCount(String.valueOf(installingCount));
        tabAll.setCount(String.valueOf(allCount));
//        Log.d(MainActivity.TAG, NetUtils.allCount + "");
    }

    private void showHideRefresh(boolean show) {
        if (show) {
            if (refreshcars != null)
                refreshcars.setRefreshing(true);
            else if (refreshcarsEdit != null)
                refreshcarsEdit.setRefreshing(true);
            else if (pbRegisterLogin != null)
                pbRegisterLogin.setVisibility(View.VISIBLE);
        } else {

            if (refreshcars != null)
                refreshcars.setRefreshing(false);
            else if (refreshcarsEdit != null)
                refreshcarsEdit.setRefreshing(false);
            else if (pbRegisterLogin != null)
                pbRegisterLogin.setVisibility(View.GONE);
        }
    }

    public static void setImage(final short status, final ImageView imageView) {
        switch (status) {
            case 0:
                imageView.setImageResource(R.drawable.ic_notinstalled);
                break;
            case 1:
                imageView.setImageResource(R.drawable.ic_installing);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_installed);
                break;
            default:
                imageView.setImageResource(R.drawable.ic_notinstalled);
                break;

        }


    }

    public static void setImage(final String imageURL, final NetworkImageView imageView) {
        loadImageFinished = false;
        imageLoader.get(IMAGE_BASE_URL + imageURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    imageView.setImageBitmap(response.getBitmap());
                } else {
                    imageView.setImageResource(R.drawable.ic_user);
                }
                loadImageFinished = true;
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setImageResource(R.drawable.ic_user);
                loadImageFinished = true;
            }
        });

//        imageLoader.get(IMAGE_BASE_URL + imageURL, ImageLoader.getImageListener(imageView,
//                R.drawable.loading, R.drawable.no_image));
//
        imageView.setImageUrl(IMAGE_BASE_URL + imageURL, imageLoader);


    }


    public interface Communicator

    {
        void response(String response);

        void response(String response, ArrayList<Report> reports, ArrayList<Finance> finances, ArrayList<Nassab> users);

        void usersReceived();

        void usersLogout(String response);


    }

    public void setCommunicator(Communicator c) {
        this.communicator = c;
    }

    private static boolean isNetworkAvailable(AppCompatActivity c) {
        boolean state;
        ConnectivityManager cmg = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cmg != null ? cmg.getActiveNetworkInfo() : null;
        state = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        // internet
        return state;
    }

//    public List<Car> getCars() {
//        ArrayList<Car> cars = new ArrayList<Car>();
//        try {
//
//            URL url = new URL("https://moj-raj.ir/nassab/show.php");
//            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            String urlParameters = "page=0";
//            connection.setDoOutput(true);
//            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
//
//            dataOutputStream.writeBytes(urlParameters);
//            dataOutputStream.flush();
//            dataOutputStream.close();
//
//            int responseCode = connection.getResponseCode();
//            Log.d(MainActivity.TAG, connection.getResponseMessage());
//            String output = "";
//
//            BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//            String line;
//
//            JSONObject json = new JSONObject();
//            while ((line = bReader.readLine()) != null) {
////                responseOutput.append(line);
//                json.getJSONObject(line);
//                tmpCar.setId(json.getInt("id"));
//                tmpCar.setName(json.getString("name"));
//                tmpCar.setNumberplate(json.getString("numberplate"));
//                tmpCar.setGps_code(json.getString("gps-code"));
//                tmpCar.setCluster(json.getString("cluster"));
//                tmpCar.setStatus((short) json.getInt("status"));
//                cars.add(tmpCar);
//                Log.d(MainActivity.TAG, line);
//            }
//            bReader.close();
//            return cars;
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return cars;
//    }

}
