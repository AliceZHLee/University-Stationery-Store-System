package com.example.logic_university;

import org.json.JSONObject;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class Command2 {
    protected AsyncTaskPOST.IServerResponse2 callback;
    protected String url;
    protected JSONObject data;

    public Command2(AsyncTaskPOST.IServerResponse2 callback, String url, JSONObject data) {
        this.callback = callback;
        this.url=url;
        this.data = data;
    }
}
