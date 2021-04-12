package com.example.logic_university;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.TokenWatcher;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class clerk_disbursementForm extends AppCompatActivity implements AsyncTaskGET.IServerResponse3{
    String jsonString;
    ListView listview;
    DisbursementAdapter disbursementAdapter;
    String url="";
    JSONArray array;
    JSONObject jsonObject;
    String disbursementId;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk_disbursement_form);

        listview=(ListView)findViewById(R.id.listView1);
        disbursementAdapter=new DisbursementAdapter(this,R.layout.row1);
        jsonString=getIntent().getExtras().getString("disbursement_data");
        try {
            jsonObject = new JSONObject(jsonString);
            array = jsonObject.getJSONArray("model");
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                ListItem item = new ListItem(
                        o.getString("Departmentname"),
                        o.getString("DisbursementID")
                );
                disbursementAdapter.add(item);
            }
            listview.setAdapter(disbursementAdapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                    try {
                        disbursementId=jsonObject.getJSONArray("model").getJSONObject(position).getString("DisbursementID");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    FetchDisbursementDetail();

                }


            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void FetchDisbursementDetail() {
        Command3 cmd=new Command3(this,"http://10.0.2.2:62084/api/Disbursement/ViewDisbursementDetail/"+disbursementId);
        AsyncTaskGET fetchdisbursment_detail=new AsyncTaskGET();
        fetchdisbursment_detail.execute(cmd);
    }
    @Override
    public void onServerResponse(JSONObject jsonObject){
        Intent DetailIntent=new Intent(this,disbursementDetailActivity.class);
        DetailIntent.putExtra("disbursmentID",disbursementId);
        DetailIntent.putExtra("list_details",jsonObject.toString());
        startActivity(DetailIntent);
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



