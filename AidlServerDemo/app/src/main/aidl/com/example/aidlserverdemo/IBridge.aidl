// IBridge.aidl
package com.example.aidlserverdemo;

import com.example.aidlserverdemo.WebSite;
// Declare any non-default types here with import statements

interface IBridge {

    WebSite getSingleWebSite(String name);

    List<WebSite> getWebSites();

    String getGoogleUrl(int id);

    int addWebSite(in WebSite site);



}
