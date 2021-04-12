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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class dep_assignRepresentative extends AppCompatActivity implements AsyncTaskGET.IServerResponse3 {
    Spinner mySpinner;
    static ArrayList StaffList;
    Button saveBtn;
    Intent in;
    TextView input;
    AsyncTaskGET.IServerResponse3 callback;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dep_assign_representative);

        //get shared preference
        pref = getSharedPreferences("userinfo",MODE_PRIVATE);
        String recorded_DeptID = pref.getString("Dept_ID","");//used for retrieving the department info

        in = new Intent(this,HomeActivity.class);    //read data from Intent which sent by previous Activity.
        String DepRep = getIntent().getStringExtra("RepName");
        System.out.println(DepRep);
        ArrayList<String>  StaffEmpList = getIntent().getStringArrayListExtra("Emplist");

        input = (TextView) findViewById(R.id.currRepName);
        saveBtn=(Button)findViewById(R.id.save_btn);

        input.setText(DepRep);
        String url="";

        mySpinner=(Spinner)findViewById(R.id.spinner1);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, StaffEmpList);

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                saveBtn.setEnabled(true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                saveBtn.setEnabled(false);
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ClickButton();
            }
        });

    }


    private void ClickButton(){
        final String name = mySpinner.getItemAtPosition(mySpinner.getSelectedItemPosition()).toString();

        String url3 = "http://10.0.2.2:62084/api/Department/SaveRepresentative/"+name;
        // System.out.println(URL);
        Command3 saveNewRep = new Command3(this,url3);
        AsyncTaskGET task = new AsyncTaskGET();
        task.execute(saveNewRep);
    }



    @Override
    public void onServerResponse(JSONObject jsonObject) {

        Intent i;
        i = new Intent(this,HomeActivity.class);
        startActivity(i);

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
