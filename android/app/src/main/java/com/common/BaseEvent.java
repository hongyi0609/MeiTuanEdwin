package com.common;

import com.facebook.react.bridge.ReadableMap;

/**
 * Created by Edwin,CHEN on 2019/10/15.
 */

public class BaseEvent {

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Object getEvent() {
        return event;
    }

    public void setEvent(Object event) {
        this.event = event;
    }

    //可能类型有很多种，数据也不一样
    protected String eventType;

    //需要传输的数据
    protected Object event;

    public ReadableMap getMap() {
        return map;
    }

    public void setMap(ReadableMap map) {
        this.map = map;
    }

    protected ReadableMap map;
}
