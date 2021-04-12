package com.example.logic_university;

import org.json.JSONObject;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class Command {
    protected AsyncTaskToServer.IServerResponse1 callback;
    protected String url;
    protected JSONObject data;

    public Command(AsyncTaskToServer.IServerResponse1 callback, String url, JSONObject data) {
        this.callback = callback;
        this.url=url;
        this.data = data;
    }
}

