package com.common;

/**
 * Created by Edwin,CHEN on 2019/10/15.
 */

public class BaseEvent {

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Object getEvent() {
        return event;
    }

    public void setEvent(Object event) {
        this.event = event;
    }

    //可能类型有很多种，数据也不一样
    protected int eventType;

    //需要传输的数据
    protected Object event;
}
