package com.example.logic_university;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/


public class Home3Activity extends AppCompatActivity implements AsyncTaskGET.IServerResponse3{
    Button viewDisbursementBtn;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home3);
        Intent callerIntent=getIntent();

        //get shared preference
        pref = getSharedPreferences("RepInfo",MODE_PRIVATE);
        String recorded_DeptID = pref.getString("Dept_ID","");//used for retrieving the department info

        viewDisbursementBtn=(Button)findViewById(R.id.viewDisbursementBtn);
        if(viewDisbursementBtn!=null){
            viewDisbursementBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickDisbursementButton();
                }
            });
        }
    }
    private void ClickDisbursementButton() {
        AsyncTaskGET task2=new AsyncTaskGET();
        String url_disbursement="http://10.0.2.2:62084/api/Disbursement/DisbursementList";
        Command3 disbursement_task=new Command3(this,url_disbursement);
        task2.execute(disbursement_task);
    }

    @Override
    public void onServerResponse(JSONObject jsonObject) {
        Intent dep_disbursement_intent=new Intent(this, DepSideDisbursementActivity.class);
        dep_disbursement_intent.putExtra("disbursement_data",jsonObject.toString());
        startActivity(dep_disbursement_intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                SharedPreferences.Editor editor=pref.edit();
                editor.clear();
                editor.commit();
                finish();
                Intent backToLoginIntent=new Intent(this,MainActivity.class);
                startActivity(backToLoginIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}




