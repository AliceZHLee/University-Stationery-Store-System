package com.example.logic_university;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class CancelDelegationActivity extends AppCompatActivity implements AsyncTaskPOST.IServerResponse2,AsyncTaskGET.IServerResponse3{
    Button unallocateBtn;
    Button allocateBtn;
    TextView EmployeeName;
    TextView StartDate;
    TextView EndDate;
    public static TextView none;
    public static LinearLayout infoForm;
    String url1;
    String url2;
    Intent intent;
    static int DelegationID;
    AsyncTaskPOST.IServerResponse2 callback;
    AsyncTaskGET.IServerResponse3 callback2;
    ArrayList<String> staffList;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_delegation);


        unallocateBtn=(Button)findViewById(R.id.unallocate);
        allocateBtn=(Button)findViewById(R.id.allocate);
        none=(TextView)findViewById(R.id.none);
        infoForm=(LinearLayout) findViewById(R.id.delegation_information);
        EmployeeName=(TextView) findViewById(R.id.employeeName);
        StartDate=(TextView)findViewById(R.id.start_date) ;
        EndDate=(TextView)findViewById(R.id.end_date);
        intent=new Intent(this,HomeActivity.class);
        // Bundle b=intent.getExtras();
        DelegationID = getIntent().getIntExtra("DelegationID", 0);

            //goforDelagation();

        System.out.println("DelegationID"+DelegationID);
        String name=getIntent().getStringExtra("Username");
        String start_date= getIntent().getStringExtra("StartDate");
        String end_date=getIntent().getStringExtra("EndDate");
        EmployeeName.setText(name);
        StartDate.setText(start_date);
        EndDate.setText(end_date);

        unallocateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onclick();
            }
        });
        allocateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goforDelagation();
            }
        });
    }
    public void onclick(){

        JSONObject JO=new JSONObject();//this JO needs to put the current
        // allocated dephead object,and pass back to  API
        none.setVisibility(View.VISIBLE);
        infoForm.setVisibility(View.INVISIBLE);

        String URL = "http://10.0.2.2:62084/api/Department/RemoveDelegation/"+ DelegationID;
        AsyncTaskGET postUnallocateStaff = new AsyncTaskGET();
        Command3 cmd = new Command3(this,URL);
        postUnallocateStaff.execute(cmd);
    }

    public void goforDelagation(){

        pref = getSharedPreferences("userinfo",MODE_PRIVATE);
        String DeptID = pref.getString("Dept_ID","");
        url2 = "http://10.0.2.2:62084/api/Department/DelegateEmployee/"+DeptID;

        System.out.println(url2);
        AsyncTaskGET getStaff = new AsyncTaskGET();
        Command3 cmd=new Command3(this,url2);
        getStaff.execute(cmd);
        //this GET needs to add dept_id into the url query string in order to fetch staff information
    }

    @Override
    public void onServerResponse() {
        CancelDelegationActivity.none.setVisibility(View.VISIBLE);
        CancelDelegationActivity.infoForm.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onServerResponse(JSONObject jsonObject)
    {
        // System.out.println("143 Jabilli");

        try{
            JSONArray jsonArray = jsonObject.getJSONArray("DepStaff");
            staffList = new ArrayList<String>();
            //UserModel u = new UserModel();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                   /*  u.Empid = jsonObject1.getInt("UserID");
                     u.Empname = jsonObject1.getString("Username");*/
                String StaffName = jsonObject1.getString("Username");
                // dep_assignRepresentative.StaffList.add(StaffName);
                staffList.add(StaffName);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Bundle B = new Bundle();
        B.putStringArrayList("Emplist",staffList);
        Intent intentToAllocate= new Intent(CancelDelegationActivity.this,dep_delegateToEmployee.class);
        intentToAllocate.putExtras(B);
        startActivity(intentToAllocate);
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
