// IServerCallBack.aidl
package com.example.aidlserverdemo;

// Declare any non-default types here with import statements
//事件引用传递给Service ，用于Server给Client发消息

interface IServerCallBack {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void valueChanged(int i);
}
