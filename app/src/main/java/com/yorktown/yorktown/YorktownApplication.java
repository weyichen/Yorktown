package com.yorktown.yorktown;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Daniel on 1/27/2015.
 */
public class YorktownApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // enable Parse
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "tDtljnEIxQTHl6wXVmTv4sz53C4JTy3lKRRlaKnG", "Vw0GRfNzg62njiwMj6Pu6qHSOycIshnIIFPR8ojj");
    }
}
