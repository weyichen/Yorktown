package com.yorktown.yorktown.eventbus;

import com.parse.ParseObject;

/**
 * Created by Daniel on 2/4/2015.
 */
public class ReadStepEvent {

    public final ParseObject parseObject;
    public final int position; // denotes the order of this step in the trip

    public ReadStepEvent(ParseObject parseObject, int position) {
        this.parseObject = parseObject;
        this.position = position;
    }

    public class FragmentEvent {

        public final ParseObject parseObject;
        public final int position;

        public FragmentEvent () {
            this.parseObject = ReadStepEvent.this.parseObject;
            this.position = ReadStepEvent.this.position;
        }
    }
}
