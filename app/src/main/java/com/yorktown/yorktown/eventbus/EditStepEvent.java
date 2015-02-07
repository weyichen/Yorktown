package com.yorktown.yorktown.eventbus;

import com.parse.ParseObject;

/**
 * Created by Daniel on 2/4/2015.
 */
public class EditStepEvent {

    public final ParseObject parseObject;
    public final int position; // denotes the order of this step in the trip

    public EditStepEvent(ParseObject parseObject, int position) {
        this.parseObject = parseObject;
        this.position = position;
    }

    public class FragmentEvent {

        public final ParseObject parseObject;
        public final int position;

        public FragmentEvent () {
            this.parseObject = EditStepEvent.this.parseObject;
            this.position = EditStepEvent.this.position;
        }
    }
}
