package com.yorktown.yorktown;

/**
 * Created by Daniel on 1/22/2015.
 */
public class Trip {
    String title;
    int color;
    String desc;
    Step[] steps;

    public Trip() {
        super();
    }

    public Trip(String title, int color, Step[] steps) {
        super();

        this.title = title;
        this.color = color;
        this.steps = steps;
    }
}
