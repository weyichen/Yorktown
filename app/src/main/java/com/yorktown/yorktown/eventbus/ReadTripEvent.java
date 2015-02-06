package com.yorktown.yorktown.eventbus;

import com.parse.ParseObject;

/**
 * Created by Daniel on 2/4/2015.
 */
public class ReadTripEvent {

    public final ParseObject parseObject;

    public ReadTripEvent(ParseObject parseObject) {
        this.parseObject = parseObject;
    }

    public class FragmentEvent {

        public final ParseObject parseObject;

        public FragmentEvent () {
            this.parseObject = ReadTripEvent.this.parseObject;
        }
    }
}
