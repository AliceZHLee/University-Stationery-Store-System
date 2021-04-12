package com.example.logic_university;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.logic_university.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class HomeActivity extends AppCompatActivity implements View.OnClickListener,AsyncTaskGET.IServerResponse3{
    Button delegateBtn;
    Button assignRepBtn;
    static int viewDisbursement_num=0;
    static int assign_num=0;
    static int canceldelegate_num=0;
    ArrayList<String> staffList;
    static  String Current_DepRep;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent callerIntent=getIntent();

        //get shared preference
        pref = getSharedPreferences("userinfo",MODE_PRIVATE);
        String recorded_DeptID = pref.getString("Dept_ID","");//used for retrieving the department info

        delegateBtn=(Button)findViewById(R.id.deleBtn);
        assignRepBtn=(Button)findViewById(R.id.assiBtn);

        if(delegateBtn!=null){
            delegateBtn.setOnClickListener(this);
        }

        if(assignRepBtn!=null){
            assignRepBtn.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch(v.getId()){
            case R.id.deleBtn: {
                canceldelegate_num=1;
                SharedPreferences sharedPref=getSharedPreferences("DeptInfo",MODE_PRIVATE);
                String DeptID = sharedPref.getString("Dept_ID","");                         //used for retrieving department info
                String url1="http://10.0.2.2:62084/api/Department/ViewDelegations?DeptID="+DeptID;
                System.out.println("mon is my jan"+url1);
                Command3 getCurrentDelegation=new Command3(this,url1);
                AsyncTaskGET task=new AsyncTaskGET();
                task.execute(getCurrentDelegation);
                intent = new Intent(HomeActivity.this,CancelDelegationActivity.class);
                break;}
            case R.id.assiBtn:{
                assign_num=1;
                SharedPreferences pref = getSharedPreferences("userinfo",MODE_PRIVATE);
                String DeptID = pref.getString("Dept_ID","");                               //used for retrieving the department info
                String url2= "http://10.0.2.2:62084/api/Department/Assginrepresentative/"+DeptID;
                System.out.println(url2);
                Command3 getStaff = new Command3(this,url2);
                AsyncTaskGET task = new AsyncTaskGET();
                task.execute(getStaff);

            }
            break;
        }
        if(intent!=null){
//            intent.putExtra("person_info",person_info);
            startActivity(intent);
        }
    }

    @Override
    public void onServerResponse(JSONObject jsonObject) {
        if(assign_num == 1)
        {
            try{
                // getting firstName and lastName
                Current_DepRep = (String) jsonObject.get("Item1");
                System.out.println(Current_DepRep);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            try{
                JSONArray jsonArray = jsonObject.getJSONArray("Item2");
                staffList = new ArrayList<String>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String StaffName = jsonObject1.getString("Username");
                    staffList.add(StaffName);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            assign_num=0;

            Bundle B = new Bundle();
            B.putString("RepName",Current_DepRep);
            B.putStringArrayList("Emplist",staffList);
            Intent intent;
            intent = new Intent(this,dep_assignRepresentative.class);
            intent.putExtras(B);
            startActivity(intent);
        }
        if(canceldelegate_num==1){
            try{
                int DelegationID = (int) jsonObject.get("DelegationID");
                String DeptID = (String)jsonObject.get("DeptID");
                int UserID =(int)jsonObject.get("UserID");
                String StartDate = (String)jsonObject.get("StartDate");
                String EndDate = (String)jsonObject.get("EndDate");
                String DelegationStatus=(String)jsonObject.get("DelegationStatus");
                String Username=(String)jsonObject.get("Username");
                Bundle bundle=new Bundle();
                bundle.putInt("DelegationID",DelegationID);
                bundle.putString("Username",Username);
                bundle.putString("StartDate",StartDate);
                bundle.putString("EndDate",EndDate);
                Intent intent=new Intent(this,CancelDelegationActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            canceldelegate_num=0;
        }
    }
}


