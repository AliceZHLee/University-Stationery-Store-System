package com.example.logic_university;

import android.os.AsyncTask;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class AsyncTaskPOST extends AsyncTask<Command2,Void,Void> {

    AsyncTaskPOST.IServerResponse2 callback;

    protected Void doInBackground(Command2... commands) {
        Command2 cmd = commands[0];
        this.callback = cmd.callback;
        try {
            URL url = new URL(cmd.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // send data
            if (cmd.data != null) {
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                DataOutputStream outstream = new DataOutputStream(conn.getOutputStream());
                outstream.writeBytes(cmd.data.toString());
                outstream.flush();
                outstream.close();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
        @Override
    protected void onPostExecute(Void avoid){
        this.callback.onServerResponse();
    }



    public interface IServerResponse2{
        void onServerResponse();
    }

}
