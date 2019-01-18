package com.mmr.nassab;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mmr.nassab.Adapter.UsersAdapter;
import com.mmr.nassab.Model.Finance;
import com.mmr.nassab.Model.Nassab;
import com.mmr.nassab.Model.Report;
import com.mmr.nassab.Util.HardnessRadioButton;
import com.mmr.nassab.Util.NetUtils;
import com.mmr.nassab.Util.PresetRadioGroup;
import com.mmr.nassab.Util.Utils;

import java.util.ArrayList;

import co.ronash.pushe.Pushe;

/**
 * Created by Mojtaba Rajabi on 11/12/2018.
 */

public class CarActivity extends AppCompatActivity implements View.OnClickListener, NetUtils.Communicator {


    private TextView tvId, tvName, tvNumberPlate, tvGps_imei, tvGps_simcard, tvDriver_name, tvDriver_simcard, tvCluster;
    private ImageView ivStatus;
    private View view;
    private PresetRadioGroup statusRadioGroup;
    short status, changeStatus;
    private NetUtils netUtils;
    private Context context;
    private String id, name, numberplate, gps_imei, gps_simcard, driver_name, driver_simcard, cluster;
    private SharedPreferences sharedpreferences;

    private String user_id;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialogInstallFinished;
    LayoutInflater viewInflater;
    private View installfinishedView;
    private Button btnInstallFinished;
    private String response;
    String hardness = "1";
    HardnessRadioButton rb_hardness_1, rb_hardness_2;
    private Spinner spinnerNassab2;
    String nassab;
    private UsersAdapter usersAdapter;
    private PresetRadioGroup rg_hardness;
    private android.support.v7.widget.Toolbar toolbarStatus;
    private String username;
    private String message;
    private Button btn_asd_reset, btn_asd_imei, btn_asd_gsminfo, btn_asd_getapn;
    private SwipeRefreshLayout swipeRefreshCarStatus;
    private String gps_pos;
    private EditText etGpsPos;
    private String gpsPos;
    private TextView tvGpsPos;
    private String pushMessage;
    private Nassab nassab2 = new Nassab();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.logo);
        }
        viewInflater = getLayoutInflater();
        installfinishedView = viewInflater.inflate(R.layout.install_finished_layout, (ViewGroup) view, false);

        sharedpreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        user_id = String.valueOf(sharedpreferences.getInt("user_id", -1));
        username = String.valueOf(sharedpreferences.getString("user_name", ""));

        if (!user_id.equals("-1"))
            nassab = String.valueOf(user_id);

        view = findViewById(android.R.id.content);
        context = this;
        Bundle extra = getIntent().getExtras();


        rb_hardness_1 = installfinishedView.findViewById(R.id.rb_hardness_1);
        rb_hardness_2 = installfinishedView.findViewById(R.id.rb_hardness_2);
        rb_hardness_1.setOnClickListener(this);
        rb_hardness_2.setOnClickListener(this);

        rg_hardness = installfinishedView.findViewById(R.id.rg_hardness);
        rg_hardness.check(rb_hardness_1.getId());

        spinnerNassab2 = installfinishedView.findViewById(R.id.spinner_nassab2);

        Nassab nothing = new Nassab();
        nothing.setName("هیچکدام");
        nothing.setImage("");
        nothing.setStatus("");

        ArrayList<Nassab> tmp = new ArrayList<>();
        tmp.add(nothing);
        tmp.addAll(MainActivity.users);
        usersAdapter = new UsersAdapter(this, tmp, true);
//        usersAdapter.setDropDownViewResource(R.layout.row_users);
        spinnerNassab2.setAdapter(usersAdapter);
        spinnerNassab2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    nassab2 = MainActivity.users.get(position - 1);
                    nassab = user_id + "@" + nassab2.getId();
                    pushMessage = " یک نصب مشترک انجام دادند! " + username + " و " + nassab2.getName();
                } else {
                    nassab = user_id;
                    nassab2.setId(Integer.valueOf(user_id));
                    pushMessage = " یک نصب راانجام داد ! " + username;
                }

                Log.d(MainActivity.TAG, "onItemSelected: " + nassab);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(installfinishedView);
        dialogBuilder.setCancelable(true);
        dialogInstallFinished = dialogBuilder.create();
        dialogInstallFinished.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                changeStatusRadio(status);
            }
        });
        tvId = findViewById(R.id.tv_code);
        tvName = findViewById(R.id.tv_name);
        tvNumberPlate = findViewById(R.id.tv_nasb_number);
        tvGps_imei = findViewById(R.id.tv_imei);
        tvGps_simcard = findViewById(R.id.tv_gps_simcard);
        tvDriver_name = findViewById(R.id.tv_driver_name);
        tvDriver_simcard = findViewById(R.id.tv_driver_simcard);
        tvCluster = findViewById(R.id.tv_cluster);
        tvGpsPos = findViewById(R.id.tv_gps_pos);
        ivStatus = findViewById(R.id.iv_status3);

        id = extra.getString("id");
        name = extra.getString("name");
        numberplate = extra.getString("numberplate");
        gps_imei = extra.getString("gps_imei");
        gps_simcard = extra.getString("gps_simcard");
        driver_name = extra.getString("driver_name");
        driver_simcard = extra.getString("driver_simcard");
        cluster = extra.getString("cluster");
        status = extra.getShort("status");
        changeStatus = status;
        gpsPos = extra.getString("gps_pos");

        toolbarStatus = findViewById(R.id.toolbar_cars_status);
        if (status == 2 && !username.equals("admin"))
            toolbarStatus.setVisibility(View.GONE);
        tvId.setText(id);
        tvName.setText(name);
        tvNumberPlate.setText(numberplate);
        tvGps_imei.setText(gps_imei);
        tvGps_simcard.setText(gps_simcard);
        tvDriver_name.setText(driver_name);
        tvDriver_simcard.setText(driver_simcard);
        tvCluster.setText(cluster);
        if (status == 2)
            tvGpsPos.setText("محل نصب : " + gps_pos);

        NetUtils.setImage(status, ivStatus);
        netUtils = new NetUtils(context);
        netUtils.setCommunicator(this);
        statusRadioGroup = findViewById(R.id.rg_status);

        changeStatusRadio(status);
        statusRadioGroup.getChildAt(0).setOnClickListener(this);
        statusRadioGroup.getChildAt(1).setOnClickListener(this);
        statusRadioGroup.getChildAt(2).setOnClickListener(this);

        btn_asd_reset = findViewById(R.id.btn_asd_reset);
        btn_asd_imei = findViewById(R.id.btn_asd_imei);
        btn_asd_gsminfo = findViewById(R.id.btn_asd_gsminfo);
        btn_asd_getapn = findViewById(R.id.btn_asd_getapn);

        btn_asd_reset.setOnClickListener(this);
        btn_asd_imei.setOnClickListener(this);
        btn_asd_gsminfo.setOnClickListener(this);
        btn_asd_getapn.setOnClickListener(this);

        etGpsPos = installfinishedView.findViewById(R.id.et_gps_pos);

        btnInstallFinished = installfinishedView.findViewById(R.id.btn_install_finished);
        btnInstallFinished.setOnClickListener(this);

        swipeRefreshCarStatus = findViewById(R.id.swipeRefreshCarsEdit);
        swipeRefreshCarStatus.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshCarStatus.setRefreshing(false);
            }
        });

        Utils.overrideFonts(this, view);
        Utils.overrideFonts(this, installfinishedView);
    }

    private void changeStatusRadio(short status) {

        switch (status) {
            case 0:
                statusRadioGroup.check(R.id.status_not_Installed);

                break;
            case 1:
                statusRadioGroup.check(R.id.status_installing);
                break;
            case 2:
                statusRadioGroup.check(R.id.status_installed);
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_install_finished: {
                if (!etGpsPos.getText().toString().equals(""))
                    netUtils.editCar(installfinishedView.getContext(), "edit_status", "status='2'", id, nassab, gps_imei, hardness, "", "", "", "", "", "", "", "", etGpsPos.getText().toString());

                else
                    Utils.mToast(context, "محل نصب را وارد کنید", Toast.LENGTH_SHORT);
                break;
            }
            case R.id.status_installed: {
                changeStatus = 2;
                dialogInstallFinished.show();
                break;
            }
            case R.id.status_installing: {
                changeStatus = 1;
//                Log.d(MainActivity.TAG, id);
                netUtils.editCar(installfinishedView.getContext(), "edit_status", "status='1'", id, nassab, gps_imei, "", "", "", "", "", "", "", "", "", "");
                break;
            }
            case R.id.status_not_Installed: {
                changeStatus = 0;
                netUtils.editCar(installfinishedView.getContext(), "edit_status", "status='0'", id, nassab, gps_imei, "", "", "", "", "", "", "", "", "", "");
                break;
            }
            case R.id.rb_hardness_1: {
                hardness = "1";
                break;
            }
            case R.id.rb_hardness_2: {
                hardness = "2";
                break;
            }
            case R.id.btn_asd_reset: {
                message = "asd reset";
                sendSMS();
                break;
            }
            case R.id.btn_asd_imei: {
                message = "asd imei";
                sendSMS();
                break;
            }
            case R.id.btn_asd_gsminfo: {
                message = "asd gsminfo";
                sendSMS();
                break;
            }
            case R.id.btn_asd_getapn: {
                message = "asd getapn";
                sendSMS();
                break;
            }
        }
    }


    @Override
    public void response(String response) {
        if (response.equals("200")) {
            status = changeStatus;
            NetUtils.setImage(status, ivStatus);
            if (status == 2) {

                try {
                    for (Nassab n : MainActivity.users) {
                        if (!String.valueOf(n.getId()).equals(user_id) && n.getId() != nassab2.getId())
                            Pushe.sendCustomJsonToUser(context, n.getPush_id(), "{ \"title\":\"تاژان\", \"message\":\"" + pushMessage + "\" }");
                    }

                } catch (co.ronash.pushe.i.d d) {
                    Utils.mToast(context, d.getMessage(), Toast.LENGTH_SHORT);
                }

                onBackPressed();
            }
        } else {

            changeStatusRadio(status);
            Utils.mToast(context, response, Toast.LENGTH_SHORT);
        }
        dialogInstallFinished.dismiss();
//        onBackPressed();
//        Log.d(MainActivity.TAG, response);
    }

    @Override
    public void response(String response, ArrayList<Report> reports, ArrayList<Finance> finances, ArrayList<Nassab> users) {

    }

    @Override
    public void usersReceived() {

    }

    @Override
    public void usersLogout(String response) {

    }

    private void sendSMS() {

        // check permission is given
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    0);
        } else {
            // permission already granted run sms send
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(gps_simcard, null, message, null, null);
            Utils.mToast(getApplicationContext(), "پیام ارسال شد!", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(gps_simcard, null, message, null, null);
                    Utils.mToast(getApplicationContext(), "پیام ارسال شد!", Toast.LENGTH_LONG);
                } else {
                    Utils.mToast(getApplicationContext(),
                            " لطفا اجازه ارسال را تایید کنید!", Toast.LENGTH_LONG);

                }
            }
        }

    }
}
