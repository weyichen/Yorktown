package com.yorktown.yorktown.eventbus;

import com.parse.ParseObject;

/**
 * Created by Daniel on 2/6/2015.
 */
public class CreateTripEvent {

    public final ParseObject parseObject;

    public CreateTripEvent(ParseObject parseObject) {
        this.parseObject = parseObject;
    }

    public class FragmentEvent {

        public final ParseObject parseObject;

        public FragmentEvent () {
            this.parseObject = CreateTripEvent.this.parseObject;
        }
    }
}
