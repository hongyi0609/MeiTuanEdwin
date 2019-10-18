package com.edwin;

import com.common.BaseEvent;

/**
 * Created by Edwin,CHEN on 2019/10/15.
 */

public class HomeEvent extends BaseEvent {

    public HomeEvent() {

    }

    public HomeEvent(int eventType) {
        this(eventType, null);
    }

    public HomeEvent(int eventType, Object event) {
        setEventType(eventType);
        setEvent(event);
    }
}
