package com.example.logic_university;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.CertPathValidatorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class disbursementDetailActivity extends AppCompatActivity
{
    StringRequest stringRequest;
    SharedPreferences pref;
    String jsonString;
    ListView listview;
    DisbursementDetailAdapter disbursementDetailAdapter;
    String url = "";
    JSONObject jsonObject1;
    JSONObject jsonObject2;
    TextView departName;
    TextView collectionPoint;
    TextView repName;
    TextView email;
    JSONArray array1;
    JSONArray array2;
    String disbursementID;
    Button updateBtn;
    Button OTPbtn;
    TextView otp;
    public static int clickTime;
    String status;
    String[] receivedArray=new String[1000];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement_detail);

        updateBtn=(Button)findViewById(R.id.update_save);
        OTPbtn=(Button)findViewById(R.id.generateOTP);
        departName = (TextView) findViewById(R.id.disbursement_department);
        collectionPoint = (TextView) findViewById(R.id.collectin_point);
        repName = (TextView) findViewById(R.id.representative_name);
        email = (TextView) findViewById(R.id.email);
        status=getIntent().getExtras().getString("status");
        disbursementID=getIntent().getExtras().getString("disbursmentID");
        jsonString = getIntent().getExtras().getString("list_details");
        listview = (ListView) findViewById(R.id.listView3);
        otp=(TextView)findViewById(R.id.otp);
        //get shared preference
        pref = getSharedPreferences("ClerkInfo",MODE_PRIVATE);
        disbursementDetailAdapter = new DisbursementDetailAdapter(this, R.layout.row_for_disbursement_details);

        try {
            jsonObject1 = new JSONObject(jsonString);
            array1 = jsonObject1.getJSONArray("model1");
            array2 = jsonObject1.getJSONArray("model2");

            departName.setText(array1.getJSONObject(0).getString("Departmentname").toString());
            repName.setText(array1.getJSONObject(0).getString("UserName").toString());
            collectionPoint.setText(array1.getJSONObject(0).getString("CollectionPoint").toString());
            email.setText(array1.getJSONObject(0).getString("EmailID").toString());
            String OTP=array1.getJSONObject(0).getString("OTP").toString();
            if(OTP.compareTo("0")==0){
                OTPbtn.setEnabled(true);
            }
            else{
                OTPbtn.setEnabled(false);
                otp.setText(OTP);
            }
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

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickTime==1){
                    Toast.makeText(disbursementDetailActivity.this,"Updated Already",Toast.LENGTH_LONG).show();
                }
                else{
                    ClickUpdateButton();
                }
            }
        });

        OTPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickGenerateOTP();
            }
        });
    }

    private void ClickGenerateOTP(){
        String url ="http://10.0.2.2:62084/api/Disbursement/validateOTP";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject JO=new JSONObject(response);
                            String otp1=JO.get("model").toString();
                            otp.setText(otp1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(disbursementDetailActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("DisbursementID",disbursementID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void ClickUpdateButton() {
        final JSONArray receivedQtyUpdate = new JSONArray();
        for (int j = 0; j < disbursementDetailAdapter.getCount(); j++) {
            ListofDisbursementItem listofDisbursementItem = (ListofDisbursementItem) disbursementDetailAdapter.getItem(j);
            RequisitionDetails item = new RequisitionDetails(Integer.parseInt(disbursementID), listofDisbursementItem.getItemCode(), Integer.parseInt(receivedArray[j]), "pending");
            try {
                receivedQtyUpdate.put(item.getReqDetJSONObject());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clickTime=1;
        String url="http://10.0.2.2:62084/api/Disbursement/UpdateQuantity";

        System.out.println(receivedQtyUpdate.toString());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, receivedQtyUpdate, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
        Toast.makeText(this,"Update Successfully",Toast.LENGTH_LONG).show();
        updateBtn.setEnabled(false);
    }


    private class DisbursementDetailAdapter extends ArrayAdapter {
        String received_qty;
        List list=new ArrayList();
        public DisbursementDetailAdapter(Context context, int resource)
        {
            super(context, resource);
        }

        public void add(ListofDisbursementItem object) {
            super.add(object);
            list.add(object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            DetailHolder holder=new DetailHolder();
            View row=convertView;
            if(row==null){
                LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row=layoutInflater.inflate(R.layout.row_for_disbursement_details,parent,false);
                holder.itemCode=row.findViewById(R.id.ITEMCODE);
                holder.stationery_description=row.findViewById(R.id.DESCRIPTION);
                holder.required_Qty=row.findViewById(R.id.REQUIREDqty);
                holder.receivedQty=row.findViewById(R.id.RECEIVEDqty);
                holder.createDiscrepancyBtn=row.findViewById(R.id.CreateDiscrepancyBtn);
                row.setTag(holder);
            }
            else{
                holder=(DetailHolder)row.getTag();
            }
            final ListofDisbursementItem listItem=(ListofDisbursementItem) this.getItem(position);
            holder.itemCode.setText(listItem.getItemCode());
            holder.stationery_description.setText(listItem.getStationeryDescription());
            holder.required_Qty.setText(String.valueOf(listItem.getRequiredQty()));

            final DetailHolder finalHolder = holder;

            holder.createDiscrepancyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    received_qty=finalHolder.receivedQty.getText().toString();
                    receivedArray[position]=received_qty;

                    Intent createDiscrepacyIntent=new Intent(disbursementDetailActivity.this,CreateDiscrepancyActivity.class);
                    createDiscrepacyIntent.putExtra("disbursmentID",disbursementID);
                    createDiscrepacyIntent.putExtra("ItemCode",listItem.getItemCode());
                    createDiscrepacyIntent.putExtra("RequiredQty",listItem.getRequiredQty());
                    createDiscrepacyIntent.putExtra("ReceivedQty",received_qty);
                    startActivity(createDiscrepacyIntent);

                }
            });
            return row;
        }
        class DetailHolder{
            TextView itemCode,stationery_description,required_Qty;
            EditText receivedQty;
            Button createDiscrepancyBtn;
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

