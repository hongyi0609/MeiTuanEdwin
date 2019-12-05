package com.common.react.events;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

/**
 * Created by Edwin,CHEN on 2019/11/21.
 */

public class TextViewStateChangedEvent extends Event<TextViewStateChangedEvent> {

	public static final String EVENT_NAME = "topStateChanged";

	private int state;

	public TextViewStateChangedEvent(int viewId, int state) {
		super(viewId);
		this.state = state;
	}

	public int getState() {
		return state;
	}

	@Override
	public String getEventName() {
		return EVENT_NAME;
	}

	@Override
	public short getCoalescingKey() {
		// All events for a given view can be coalesced.
		return 0;
	}

	@Override
	public void dispatch(RCTEventEmitter rctEventEmitter) {
		WritableMap event = Arguments.createMap();
		event.putInt("state", getState());
		rctEventEmitter.receiveEvent(getViewTag(),getEventName(),event);
	}
}
