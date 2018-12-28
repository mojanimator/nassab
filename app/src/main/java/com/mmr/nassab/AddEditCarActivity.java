package com.mmr.nassab;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mmr.nassab.Adapter.SpinnersAdapter;
import com.mmr.nassab.Util.NetUtils;
import com.mmr.nassab.Util.Utils;

import java.io.File;
import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class AddEditCarActivity extends AppCompatActivity implements View.OnClickListener, ZBarScannerView.ResultHandler, AdapterView.OnItemSelectedListener, NetUtils.Communicator {
    private ZBarScannerView mScannerView;
    private EditText etGpsIMEI, etGpsSimcard, etCarName, etCarNumberplate, etDriverName, etDriverPhone, etCluster, etProject, etGpsType;
    private Button btnAddCar, btnImportFile;
    private ImageView ivOpenCamera;
    private LayoutInflater lf;
    private View view;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialogScanner;
    private Spinner spinnerDevices, spinnerClusters, spinnerProjects;
    private SpinnersAdapter spinnerAdapterDevices, spinnerAdapterClusters, spinnerAdapterProjects;
    private TextView tv;
    private NetUtils netUtils;
    private SwipeRefreshLayout swipeRefreshCarStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_car);

        netUtils = new NetUtils(this);
        netUtils.setCommunicator(this);

        ivOpenCamera = findViewById(R.id.ivOpenCamera);
        ivOpenCamera.setOnClickListener(this);

        etGpsIMEI = findViewById(R.id.et_gps_imei);
        etGpsSimcard = findViewById(R.id.et_gps_simcard);
        etGpsType = findViewById(R.id.et_gps_type);
        etCarName = findViewById(R.id.et_car_name);
        etCarNumberplate = findViewById(R.id.et_car_numberplate);
        etDriverName = findViewById(R.id.et_driver_name);
        etDriverPhone = findViewById(R.id.et_driver_phone);
        etCluster = findViewById(R.id.et_cluster);
        etProject = findViewById(R.id.et_project);

        spinnerDevices = findViewById(R.id.spinner_gps_type);
        spinnerClusters = findViewById(R.id.spinner_cluster);
        spinnerProjects = findViewById(R.id.spinner_project);

        spinnerDevices.setOnItemSelectedListener(this);
        spinnerClusters.setOnItemSelectedListener(this);
        spinnerProjects.setOnItemSelectedListener(this);

        spinnerAdapterDevices = new SpinnersAdapter(this, new ArrayList<>(MainActivity.utils.getDevices().keySet()));
        spinnerAdapterClusters = new SpinnersAdapter(this, new ArrayList<>(MainActivity.utils.getClusters().keySet()));
        spinnerAdapterProjects = new SpinnersAdapter(this, new ArrayList<>(MainActivity.utils.getProjects().keySet()));

        spinnerDevices.setAdapter(spinnerAdapterDevices);
        spinnerClusters.setAdapter(spinnerAdapterClusters);
        spinnerProjects.setAdapter(spinnerAdapterProjects);

        btnAddCar = findViewById(R.id.btn_add_car);
        btnAddCar.setOnClickListener(this);
        btnImportFile = findViewById(R.id.btn_import_file);
        btnImportFile.setOnClickListener(this);

        mScannerView = new ZBarScannerView(this);

        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(mScannerView);
        dialogBuilder.setCancelable(true);
        dialogScanner = dialogBuilder.create();
        dialogScanner.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mScannerView.stopCamera();
            }
        });

        swipeRefreshCarStatus = findViewById(R.id.swipeRefreshCarsEdit);
        swipeRefreshCarStatus.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshCarStatus.setRefreshing(false);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tv = view.findViewById(R.id.tv_name);

//        Log.d(MainActivity.TAG, id + "");
        switch (parent.getId()) {
            case R.id.spinner_gps_type:
                etGpsType.setText(tv.getText());
//                for (Map.Entry<Integer, String> entry : testMap.entrySet()) {
//                    if (entry.getValue().equals("c")) {
//                        System.out.println(entry.getKey());
//                    }
//                }
                break;
            case R.id.spinner_cluster:
                etCluster.setText(tv.getText());
                break;
            case R.id.spinner_project:
                etProject.setText(tv.getText());
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_car:
                if (etGpsIMEI.getText().toString().equals("") || etCarName.getText().toString().equals("") || etCarNumberplate.getText().toString().equals("") || etGpsSimcard.getText().toString().equals("")
                        || etDriverName.getText().toString().equals("") || etDriverPhone.getText().toString().equals("") || etCluster.getText().toString().equals("") || etGpsType.getText().toString().equals("") || etProject.getText().toString().equals(""))
                    Utils.mToast(this, "لطفاْ تمام ورودی ها را پر کنید", Toast.LENGTH_SHORT);
                else
                    netUtils.editCar(this, "add", "", "", "", etGpsIMEI.getText().toString(), "", etCarName.getText().toString(), etCarNumberplate.getText().toString(), etGpsSimcard.getText().toString()
                            , etDriverName.getText().toString(), etDriverPhone.getText().toString(), etCluster.getText().toString(), etGpsType.getText().toString(), etProject.getText().toString());

                break;

            case R.id.btn_import_file:
                fileChooser();
                break;
            case R.id.ivOpenCamera:
                cameraPermission();


                break;
        }
    }

    public void fileChooser() {

        String type = "application/x-excel";

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType(type);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(i, "select file"), 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri uploadFileUri = data.getData();
            File file = null;
            if (uploadFileUri != null) {
                String format[] = uploadFileUri.getPath().split("\\.");
                if (format.length > 0 && format[format.length - 1].equals("xlsx"))
                    importFileToDatabase(new File(uploadFileUri.getPath()));
                else
                    Utils.mToast(this, "تنها فرمت xlsx مجاز است", Toast.LENGTH_SHORT);

            }
        }
    }

    private void importFileToDatabase(File file) {

    }

    @Override
    public void response(String response) {

        if (response.equals("200"))
            Utils.mToast(this, "با موفقیت اضافه شد!", Toast.LENGTH_SHORT);
        else
            Utils.mToast(this, response, Toast.LENGTH_SHORT);
    }


    @Override
    public void handleResult(Result rawResult) {

//        Log.v(MainActivity.TAG, rawResult.getContents()); // Prints scan results
//        Log.v(MainActivity.TAG, rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)
        etGpsIMEI.setText(rawResult.getContents());

        mScannerView.stopCamera();
        dialogScanner.dismiss();
//         If you would like to resume scanning, call this method below:
//        mScannerView.resumeCameraPreview(this);
    }

    private void cameraPermission() {

        // check permission is given
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    0);
        } else {
            // permission already granted
            dialogScanner.show();
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialogScanner.show();
                    mScannerView.setResultHandler(this);
                    mScannerView.startCamera();
                } else {
                    Utils.mToast(getApplicationContext(),
                            " لطفا اجازه استفاده از دوربین را تایید کنید!", Toast.LENGTH_LONG);

                }
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
//        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
//        mScannerView.startCamera();
//        Log.d(MainActivity.TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
//        Log.d(MainActivity.TAG, "onPause: ");// Stop camera on pause
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mScannerView.stopCamera();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void usersReceived() {

    }

    @Override
    public void usersLogout(String response) {

    }
}
