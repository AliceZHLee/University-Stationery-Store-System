package com.example.logic_university;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class DepSideDisbursementDetails extends AppCompatActivity {

    String jsonString;
    ListView listview;
    Dep_disbursementDetailAdapter disbursementDetailAdapter;
    JSONObject jsonObject1;
    JSONObject jsonObject2;
    TextView departName;
    TextView collectionPoint;
    TextView repName;
    TextView email;
    JSONArray array1;
    JSONArray array2;
    String disbursementID;
    public static int clickTime;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dep_side_disbursement_details);

        departName = (TextView) findViewById(R.id.DEPdisbursement_department);
        collectionPoint = (TextView) findViewById(R.id.DEPcollectin_point);
        repName = (TextView) findViewById(R.id.DEPrepresentative_name);
        email = (TextView) findViewById(R.id.DEPemail);

        //get shared preference
        pref = getSharedPreferences("RepInfo",MODE_PRIVATE);
//        String recorded_DeptID = pref.getString("Dept_ID","");//used for retrieving the department info

        disbursementID=getIntent().getExtras().getString("disbursmentID");
        jsonString = getIntent().getExtras().getString("list_details");
        listview = (ListView) findViewById(R.id.listView4);
        disbursementDetailAdapter = new Dep_disbursementDetailAdapter(this, R.layout.row_for_dep_side_disbursement_details);

        try {
            jsonObject1 = new JSONObject(jsonString);
            array1 = jsonObject1.getJSONArray("model1");
            array2 = jsonObject1.getJSONArray("model2");

            departName.setText(array1.getJSONObject(0).getString("Departmentname").toString());
            repName.setText(array1.getJSONObject(0).getString("UserName").toString());
            collectionPoint.setText(array1.getJSONObject(0).getString("CollectionPoint").toString());
            email.setText(array1.getJSONObject(0).getString("EmailID").toString());

            for (int i = 0; i < array2.length(); i++) {
                jsonObject2 = array2.getJSONObject(i);
                ListofDisbursementItem item = new ListofDisbursementItem(
                        jsonObject2.getString("ItemID"),
                        jsonObject2.getString("ItemName"),
                        jsonObject2.getString("ActualQty"),
                        jsonObject2.getString("DeliveredQty")
                );
                disbursementDetailAdapter.add(item);
            }
            listview.setAdapter(disbursementDetailAdapter);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

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
