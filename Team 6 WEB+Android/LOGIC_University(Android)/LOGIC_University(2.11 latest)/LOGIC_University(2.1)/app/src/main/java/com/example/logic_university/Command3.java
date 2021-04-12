package com.example.logic_university;

import org.json.JSONObject;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class Command3 {
    protected AsyncTaskGET.IServerResponse3 callback;
    protected String url;

    public Command3(AsyncTaskGET.IServerResponse3 callback, String url) {
        this.callback = callback;
        this.url=url;
    }
}
