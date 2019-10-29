/**
 * This exposes the native HomeReactNativeMessageEventModule module as a JS module. This has a
 * function 'sendEventToNative' which takes the following parameters:
 *
 * 1. String eventType: A string with the text to identify event
 * 2. Object readableMap: The data of send to native. In Android Platform, It may be 
 * one ReadableMap Object.
 */
'use strict';
import { NativeModules,Platform } from 'react-native';

const HomeReactNativeMessageEventModule = NativeModules.HomeReactNativeMessageEventModule;
function _send(eventType, data){
    if (Platform.OS === "android") {
        HomeReactNativeMessageEventModule.sendEventToNative(eventType, data);
    } else if (Platform.OS === "ios") {
        NativeModules.HomeReactNativeDispatcherModule.sendNotification({ name: eventName, body: data });
    }
}
module.exports = {send: _send}