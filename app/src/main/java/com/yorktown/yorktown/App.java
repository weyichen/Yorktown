package com.yorktown.yorktown;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;

/**
 * Created by Daniel on 1/27/2015.
 */
public class App extends Application {

    // create a global context
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        // enable Parse
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "tDtljnEIxQTHl6wXVmTv4sz53C4JTy3lKRRlaKnG", "Vw0GRfNzg62njiwMj6Pu6qHSOycIshnIIFPR8ojj");
    }

    public static Context getContext() {
        return mContext;
    }
}
