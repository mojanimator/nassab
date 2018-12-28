package com.mmr.nassab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mmr.nassab.Util.NetUtils;
import com.mmr.nassab.Util.Utils;

import java.io.ByteArrayOutputStream;

/**
 * Created by Mojtaba Rajabi on 22/12/2018.
 */

public class RegisterActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_FROM_GALLERY = 200;
    private ImageView ivRegister;
    private Button btnRegister;
    private int fileSize;
    private String encodedString = "";
    private EditText et_username, et_password, et_card, et_phone;
    private String username = "", password = "", card = "", phone = "";
    private AppCompatActivity activity;
    private View view;
    private ProgressBar spinner;
    private ProgressBar pbRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        view = this.getWindow().getDecorView().findViewById(R.id.layout_register);
        Utils.overrideFonts(this, view);
        final NetUtils netUtils = new NetUtils(this);
        activity = this;


        et_username = findViewById(R.id.et_username2);
        et_password = findViewById(R.id.et_password2);
        et_card = findViewById(R.id.et_card);
        et_phone = findViewById(R.id.et_phone);

        ivRegister = findViewById(R.id.ivRegister);
        ivRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                    } else {
                        pickImageFromDeviceGallery();
                    }
                }

            }
        });
        btnRegister = findViewById(R.id.btn_register2);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                card = et_phone.getText().toString();
                phone = et_card.getText().toString();

                if (username.equals("") || password.equals("") || encodedString.equals("") ||
                        card.equals("") || phone.equals("")) {
                    Utils.mToast(getBaseContext(), "ورودی ها نامعتبر هستند", Toast.LENGTH_SHORT);
                } else

                    netUtils.registerEditLoginLogout("register", username, password, encodedString, phone, card, android.os.Build.BRAND + '~' + android.os.Build.MODEL + "~" + Utils.screenSizeInches(activity));
            }
        });
    }

    private void pickImageFromDeviceGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_FROM_GALLERY && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.d(MainActivity.TAG, "onActivityResult: " + picturePath);
            // image compress
            final int MAX_IMAGE_SIZE = 500 * 1024;
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = calculateInSampleSize(options, 800, 800);
// Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap myImg = BitmapFactory.decodeFile(picturePath, options);
            ivRegister.setImageBitmap(myImg);
            int compressQuality = 100;
            int streamLength;
            byte[] bmpPicByteArray;
            do {
                ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                Log.d(MainActivity.TAG, "Quality: " + compressQuality);
                myImg.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
                bmpPicByteArray = bmpStream.toByteArray();
                streamLength = bmpPicByteArray.length;
                compressQuality -= 10;
                Log.d(MainActivity.TAG, "len " + streamLength + "," + MAX_IMAGE_SIZE);

            } while (streamLength >= MAX_IMAGE_SIZE);

            try {
                encodedString = Base64.encodeToString(bmpPicByteArray, 0);
            } catch (OutOfMemoryError e) {
                Utils.mToast(getBaseContext(), "no enough memory", Toast.LENGTH_SHORT);

            }

//            timeBeforeUpload = System.currentTimeMillis();

        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(MainActivity.TAG, "image height: " + height + "---image width: " + width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromDeviceGallery();
                } else {
                    Utils.mToast(getApplicationContext(),
                            " لطفا اجازه دسترسی به حافظه را تایید کنید!", Toast.LENGTH_LONG);

                }
            }
        }

    }
}
