package com.yorktown.yorktown;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Daniel on 1/30/2015.
 */
public class Connectivity {

    NetworkInfo activeNetwork;

    public Connectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
    }

    public boolean isConnected() {
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public boolean isWiFi() {
        return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }
}