package com.example.logic_university;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class DepSideDisbursementActivity extends AppCompatActivity implements AsyncTaskGET.IServerResponse3 {
    String jsonString;
    ListView listview;
    DisbursementAdapter DepdisbursementAdapter;
    JSONArray array;
    JSONObject jsonObject;
    String disbursementId;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dep_side_disbursement);

        //get shared preference
        pref = getSharedPreferences("RepInfo",MODE_PRIVATE);
//        String recorded_DeptID = pref.getString("username","");//used for retrieving the department info
        listview=(ListView)findViewById(R.id.listView2);
        DepdisbursementAdapter=new DisbursementAdapter(this,R.layout.row1);
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
                DepdisbursementAdapter.add(item);
            }
            listview.setAdapter(DepdisbursementAdapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                    try {
                        disbursementId=jsonObject.getJSONArray("model").getJSONObject(position).getString("DisbursementID");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    FetchDepDisbursementDetail();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void FetchDepDisbursementDetail() {
        Command3 cmd=new Command3(this,"http://10.0.2.2:62084/api/Disbursement/ViewDisbursementDetail/"+disbursementId);
        AsyncTaskGET fetchdisbursment_detail=new AsyncTaskGET();
        fetchdisbursment_detail.execute(cmd);
    }
    @Override
    public void onServerResponse(JSONObject jsonObject){
        Intent DEPDetailIntent=new Intent(this,DepSideDisbursementDetails.class);
        DEPDetailIntent.putExtra("disbursmentID",disbursementId);
        DEPDetailIntent.putExtra("list_details",jsonObject.toString());
        startActivity(DEPDetailIntent);
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
