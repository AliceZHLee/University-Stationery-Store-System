package com.example.logic_university;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class MainActivity extends AppCompatActivity implements AsyncTaskGET.IServerResponse3 {
    EditText Username;
    EditText Password;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickButton();
            }
        });

    }
    private void ClickButton(){
        Command3 login_authentication=new Command3(this,
                "http://10.0.2.2:62084/api/Home/Login?username="+Username.getText().toString()+"&passcode="+Password.getText().toString());
        new AsyncTaskGET().execute(login_authentication);
    }

    @Override
    public void onServerResponse(JSONObject jsonObj) {
        Object obj = null;
        try {
            obj = jsonObj.get("role");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(obj instanceof JsonNull){
                Toast.makeText(MainActivity.this,"Password or username is wrong",Toast.LENGTH_LONG);
        }
        else {
            try {
                String role = (String) jsonObj.get("role");
                if (role.compareTo("DepHead") == 0) {
                    Intent intent1 = new Intent(MainActivity.this, HomeActivity.class);
                    intent1.putExtra("DeptHeadusername", Username.getText().toString());
                    //shared preference
                    SharedPreferences sharedPref = getSharedPreferences("userinfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("Dept_ID", jsonObj.get("DeptID_FK").toString());
                    editor.putString("UserID", jsonObj.get("UserID").toString());
                    editor.commit();
                    startActivity(intent1);
                } else if (role.compareTo("SClerk") == 0) {
                    Intent intent2 = new Intent(MainActivity.this, Home2Activity.class);
                    //shared preference
                    SharedPreferences sharedPref = getSharedPreferences("ClerkInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", jsonObj.get("Username").toString());
                    editor.commit();
                    startActivity(intent2);
                } else if (role.compareTo("DepRep") == 0) {
                    Intent intent3 = new Intent(MainActivity.this, Home3Activity.class);
                    //shared preference
                    SharedPreferences sharedPref = getSharedPreferences("RepInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", jsonObj.get("Username").toString());
                    editor.commit();
                    startActivity(intent3);
                } else {
                    Toast errormsg = Toast.makeText(MainActivity.this,
                            "Sorry,this app only gives access to department head,department rep and store clerk", Toast.LENGTH_LONG);
                    errormsg.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}





