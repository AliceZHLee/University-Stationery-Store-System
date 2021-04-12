package com.example.logic_university;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class AsyncTaskToServer extends AsyncTask<Command,Void, JSONObject> {
    IServerResponse1 callback;

    @Override
    protected JSONObject doInBackground(Command... commands) {
        Command command=commands[0];
        this.callback=command.callback;

        JSONObject jsonObject=null;
        StringBuilder response=new StringBuilder();
        try{
            URL url=new URL(command.url);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();

            //send data
            if(command.data!=null) {
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
                outStream.writeBytes(command.data.toString());
                outStream.flush();
                outStream.close();
            }
            //receive response
            InputStream inputStream=new BufferedInputStream(conn.getInputStream());
            BufferedReader r=new BufferedReader(new InputStreamReader(inputStream));
            for(String line;(line=r.readLine())!=null;){
                response.append(line).append('\n');
            }
            try{
                jsonObject=new JSONObject(response.toString());}
            catch(Exception e){
                e.printStackTrace();
            }
            r.close();
            inputStream.close();
            conn.disconnect();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    protected void onPostExecute(JSONObject jsonObject){
        if(jsonObject!=null){
            this.callback.onServerResponse(jsonObject);
        }
    }
    public interface IServerResponse1{
        void onServerResponse(JSONObject jsonObject);
    }
}
