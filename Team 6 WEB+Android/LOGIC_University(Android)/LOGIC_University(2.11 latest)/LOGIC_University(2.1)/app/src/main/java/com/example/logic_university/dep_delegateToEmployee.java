package com.example.logic_university;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class dep_delegateToEmployee extends AppCompatActivity implements View.OnClickListener{
    Button delegateBtn;
    EditText startDate;
    EditText endDate;
    private static final String TAG = "dep_delegateToEmployee";
    private DatePickerDialog.OnDateSetListener SetListener;
    private DatePickerDialog.OnDateSetListener SetListener1;
    Spinner mySpinner;
    ArrayList<String> StaffList;
    Intent in;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dep_delegate_to_employee);
        Intent delegate_intent=getIntent();

        StaffList=new ArrayList<>();
        delegateBtn=(Button)findViewById(R.id.SubmitButton);
        startDate=(EditText)findViewById(R.id.sDate);
        endDate=(EditText)findViewById(R.id.eDate);

        //get shared preference
        pref = getSharedPreferences("userinfo",MODE_PRIVATE);
        String recorded_DeptID = pref.getString("Dept_ID","");//used for retrieving the department info


        in = new Intent(this,dep_delegateToEmployee.class);                          //read data from Intent which sent by previous Activity.
        ArrayList<String>  StaffEmpList = getIntent().getStringArrayListExtra("Emplist");

        mySpinner=(Spinner)findViewById(R.id.spinner2);

        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, StaffEmpList);

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinner.setAdapter(myAdapter);

        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        delegateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sDate: {
                final Calendar s = Calendar.getInstance();
                int year = s.get(Calendar.YEAR);
                int month = s.get(Calendar.MONTH);
                int day = s.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickDialog = new DatePickerDialog(
                        dep_delegateToEmployee.this,
                        android.R.style.Theme_DeviceDefault_Dialog, SetListener, year, month, day);
                datePickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickDialog.show();

                SetListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        month = month + 1;
                        Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                        String date = month + "/" + day + "/" + year;
                        startDate.setText(date);
                    }
                };

                break;}

            case R.id.eDate:{
                String getFromDate = startDate.getText().toString().trim();
                String getFrom[] = getFromDate.split("/");
                int year,month,day;
                year= Integer.parseInt(getFrom[2]);
                month = Integer.parseInt(getFrom[0]);
                day = Integer.parseInt(getFrom[1]);
                final Calendar c = Calendar.getInstance();
                month = month-1;
                c.set(year,month,day);
                DatePickerDialog datePickDialog = new DatePickerDialog(
                        dep_delegateToEmployee.this,
                        android.R.style.Theme_DeviceDefault_Dialog, SetListener1, year, month, day);
                datePickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickDialog.show();

                SetListener1 = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        datePicker.setMinDate(c.getTimeInMillis());
                        month = month + 1;
                        Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                        String date = month + "/" + day + "/" + year;
                        endDate.setText(date);
                    }
                };
                break;}
            case R.id.SubmitButton:{
                if(startDate.getText()==null ||endDate.getText()==null)
                {
                    Toast message=Toast.makeText(this,"You haven't selected date",Toast.LENGTH_LONG);
                    message.show();
                }
                else if( mySpinner.getItemAtPosition(mySpinner.getSelectedItemPosition()).toString()==null){
                    Toast message2=Toast.makeText(this,"Please select an employee to delegate",Toast.LENGTH_LONG);
                    message2.show();
                }
                else{
                    saveDelegation();
                }
                break;
            }
        }
    }

    private void saveDelegation(){
        String url9 ="http://10.0.2.2:62084/api/Department/SaveDelegation";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url9,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Username",mySpinner.getItemAtPosition(mySpinner.getSelectedItemPosition()).toString());
                params.put("StartDate",startDate.getText().toString());
                params.put("EndDate",endDate.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        Intent intentToHome= new Intent(this,HomeActivity.class);
        startActivity(intentToHome);

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

