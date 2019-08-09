// IRemoteService.aidl
package com.example.aidlserverdemo;

// Declare any non-default types here with import statements

import com.example.aidlserverdemo.IServerCallBack;

interface IRemoteService {

     void registerCallback(IServerCallBack callBack);

     void unregisterCallback(IServerCallBack callBack);
}
