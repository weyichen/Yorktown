package com.yorktown.yorktown.eventbus;

import com.parse.ParseObject;

/**
 * Created by Daniel on 2/4/2015.
 */
public class EditTripEvent {

    public final ParseObject parseObject;

    public EditTripEvent(ParseObject parseObject) {
        this.parseObject = parseObject;
    }

    public class FragmentEvent {

        public final ParseObject parseObject;

        public FragmentEvent () {
            this.parseObject = EditTripEvent.this.parseObject;
        }
    }
}
