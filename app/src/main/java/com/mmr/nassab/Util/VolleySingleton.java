package com.mmr.nassab.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.mmr.nassab.MainActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by mmr on 25/02/2018.
 */

class VolleySingleton {

    private static VolleySingleton mInstance;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private Context mCtx;
    final int Mb = 1024 * 1024;

    public VolleySingleton(final Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();


    }

    private HurlStack stack = new HurlStack() {
        @Override
        protected HttpURLConnection createConnection(java.net.URL url)
                throws IOException {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super
                    .createConnection(url);
            try {
                httpsURLConnection
                        .setSSLSocketFactory(new TLSSocketFactory());
//                Log.d(MainActivity.TAG, "successful TLS v1.2");
                // httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
            } catch (KeyManagementException e) {
                e.printStackTrace();
                Log.d(MainActivity.TAG, "Could not create new stack for TLS v1.2");
                stack = new HurlStack();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                Log.d(MainActivity.TAG, "Could not create new stack for TLS v1.2");
                stack = new HurlStack();
            }
            return httpsURLConnection;
        }
    };

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 10 * Mb);
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache, network);

            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), stack);
        }
        return requestQueue;

    }

    ImageLoader getImageLoader() {
        if (imageLoader != null) {

            return imageLoader;
        } else {
            imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap> cache = new LruCache<>(100 * Mb);

                @Override
                public Bitmap getBitmap(String url) {
//                    BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.no_image)
                    return cache.get(url);
                }


                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url, bitmap);
                }


            });

            return imageLoader;
        }
    }

    static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    <T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }

    public void cancelPendingRequests(String tag) {
        requestQueue.cancelAll(tag);
    }
}
