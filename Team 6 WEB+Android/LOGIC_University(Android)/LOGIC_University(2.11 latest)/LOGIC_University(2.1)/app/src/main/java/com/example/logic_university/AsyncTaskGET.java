package com.example.logic_university;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/


public class AsyncTaskGET extends AsyncTask<Command3,Void, JSONObject> {
  JSONObject jsonObject=null;
  AsyncTaskGET.IServerResponse3 callback;
  String JSON_STRING;

  @Override
  protected JSONObject doInBackground(Command3...commands ) {
    Command3 cmd = commands[0];
    this.callback = cmd.callback;
    HttpURLConnection conn=null;


    try {
      URL url1=new URL(cmd.url);
      conn=(HttpURLConnection)url1.openConnection();
      InputStream inputStream=new BufferedInputStream(conn.getInputStream());
      BufferedReader r=new BufferedReader(new InputStreamReader(inputStream));
      StringBuilder response = new StringBuilder();

      while((JSON_STRING=r.readLine())!=null){
        response.append(JSON_STRING+"\n");
      }
      r.close();
      inputStream.close();
      try{
        jsonObject=new JSONObject(response.toString().trim());
      } catch (Exception e) {
        e.printStackTrace();
      }

    } catch (Exception ex) {
      return jsonObject;
    }
    finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return jsonObject ;
  }


  @Override
  protected void onPostExecute(JSONObject jo){
    super.onPostExecute(jo);
    this.callback.onServerResponse(jo);
  }
  public interface IServerResponse3{
    void onServerResponse(JSONObject jsonObject);
  }
}
