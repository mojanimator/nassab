package com.mmr.nassab.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.mmr.nassab.R;

/**
 * Created by mmr on 01/03/2018.
 */

public class Utils {
    private static Drawable status_background_service;
    private static Drawable status_background_using;
    private static Drawable status_background_auction;
    private static Drawable status_background_ready;

    //    private static ArrayList<ProvinceCounties> provinceCountiesList = new ArrayList<>();
    private static String[] stausNamesArray;
    private static int[] statusDrawablesArray;

//    public static ArrayList<ProvinceCounties> getProvinceCounties() {
//
//        return provinceCountiesList;
//    }


    public static String[] getStatusNamesArray() {
        return stausNamesArray;
    }

    public static int[] getStatusDrawablesArray() {
        return statusDrawablesArray;
    }

    //this runs in application class
//    public static void prepareResources(Context context) {
//        status_background_service = context.getResources().getDrawable(R.drawable.status_service_background);
//        status_background_using = context.getResources().getDrawable(R.drawable.status_using_background);
//        status_background_auction = context.getResources().getDrawable(R.drawable.status_auction_background);
//        status_background_ready = context.getResources().getDrawable(R.drawable.status_ready_background);
//        stausNamesArray = new String[]{"r", "s", "a", "u"};//ready,service,auction,using
//        statusDrawablesArray = new int[]{R.drawable.status_ready_background, R.drawable.status_service_background, R.drawable.status_auction_background, R.drawable.status_using_background};
//    }


    public static void changeAppbarFont(Toolbar toolbar, Context ctx) {
        Typeface myCustomFont = Typeface.createFromAsset(ctx.getAssets(), "fonts/B Koodak.ttf");

        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);

            if (view instanceof TextView) {
                TextView textView = (TextView) view;

                textView.setTypeface(myCustomFont);
            }
        }

//        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
//        int tabsCount = vg.getChildCount();
//        for (int j = 0; j < tabsCount; j++) {
//            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
//            int tabChildsCount = vgTab.getChildCount();
//            for (int i = 0; i < tabChildsCount; i++) {
//                View tabViewChild = vgTab.getChildAt(i);
//                if (tabViewChild instanceof TextView) {
//                    ((TextView) tabViewChild).setTypeface(myCustomFont);
//                }
//            }
//        }
    }

    public static void overrideFonts(final Context context, final View v) {

        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/B Koodak.ttf"));

            }
        } catch (Exception e) {
        }


    }

    public static void hideSoftKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void mToast(Context ctx, String message, int duration) {
        Toast toast = Toast.makeText(ctx, message, duration);
        View view = toast.getView();

        view.setBackgroundResource(R.drawable.toast_background);

        TextView text = toast.getView().findViewById(android.R.id.message);
        text.setShadowLayer(5, 5, 1, Color.TRANSPARENT);
        text.setTypeface(Typeface.createFromAsset(ctx.getAssets(), "fonts/B Koodak.ttf"));
//        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        text.setTextColor(ContextCompat.getColor(ctx, R.color.colorText));
        text.setTextSize(ctx.getResources().getDimension(R.dimen.textSize_toast));
        toast.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL, 0, 200);
        toast.show();
    }

    public static void setupStatus(TextView tvStatus, String status) {

        Drawable drawable;
        String splitStatus = status.split("-")[0];

        switch (splitStatus) {
            case "r":
                tvStatus.setText("آماده");
                drawable = status_background_ready;
                break;
            case "s":
                tvStatus.setText("در سرویس");
                drawable = status_background_service;
                break;
            case "a":
                tvStatus.setText("در مزایده");
                drawable = status_background_auction;
                break;
            case "u":
                tvStatus.setText("رزرو شده");
                drawable = status_background_using;
                break;
            default:
                tvStatus.setText("نامشخص");
                drawable = status_background_service;
        }

        tvStatus.setBackground(drawable);
    }


    public static double screenSizeInches(Context ctx) {
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();

        double density = dm.density * 160;
        double x = Math.pow(dm.widthPixels / density, 2);
        double y = Math.pow(dm.heightPixels / density, 2);

        return Math.round(Math.sqrt(x + y));
//        Log.d(MainActivity.TAG, "screenSizeInches: " + screenInches);
    }


}

//    @RealmModule(classes = {Province.class, County.class})
//    public class MyModule {
//    }

