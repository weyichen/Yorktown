package com.yorktown.yorktown.eventbus;

import com.parse.ParseObject;

/**
 * Created by Daniel on 2/6/2015.
 */
public class CreateStepEvent {

    public final ParseObject parseObject;

    public CreateStepEvent(ParseObject parseObject) {
        this.parseObject = parseObject;
    }

    public class FragmentEvent {

        public final ParseObject parseObject;

        public FragmentEvent () {
            this.parseObject = CreateStepEvent.this.parseObject;
        }
    }
}
