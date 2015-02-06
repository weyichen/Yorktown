package com.yorktown.yorktown.eventbus;

import com.parse.ParseObject;

/**
 * Created by Daniel on 2/4/2015.
 */
public class NewStepEvent {

    public final ParseObject parseObject;

    public NewStepEvent(ParseObject parseObject) {
        this.parseObject = parseObject;
    }

    public class FragmentEvent {

        public final ParseObject parseObject;

        public FragmentEvent () {
            this.parseObject = NewStepEvent.this.parseObject;
        }
    }
}
