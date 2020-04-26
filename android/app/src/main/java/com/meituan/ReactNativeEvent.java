package com.meituan;

import com.common.BaseEvent;

/**
 * Created by Edwin,CHEN on 2019/10/15.
 */

public class ReactNativeEvent extends BaseEvent {

    public ReactNativeEvent() {

    }

    public ReactNativeEvent(String eventType) {
        this(eventType, null);
    }

    public ReactNativeEvent(String eventType, Object event) {
        setEventType(eventType);
        setEvent(event);
    }
}
