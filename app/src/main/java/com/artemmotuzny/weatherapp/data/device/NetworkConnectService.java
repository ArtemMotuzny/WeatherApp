package com.artemmotuzny.weatherapp.data.device;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;

/**
 * Created by tema_ on 13.10.2016.
 */

public class NetworkConnectService {
    private NetworkInfo networkInfo;

    public NetworkConnectService(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
    }

    public boolean getConnectState(){
        return networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED;
    }
}
