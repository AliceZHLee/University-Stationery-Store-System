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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class CreateDiscrepancyActivity extends AppCompatActivity{
    StringRequest stringRequest;
    TextView disbursementID;
    TextView itemID;
    TextView wantedQty;
    TextView DeliveredQty;
    EditText Reason;
    Button submitBtn;
    SharedPreferences pref;
    public static int clickTime2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_discrepancy);

        disbursementID=(TextView)findViewById(R.id.fetchedDisbursementID);
        itemID=(TextView)findViewById(R.id.ItemID);
        wantedQty=(TextView)findViewById(R.id.wantedQty);
        DeliveredQty=(TextView)findViewById(R.id.delivered_Qty);
        Reason=(EditText)findViewById(R.id.Reason);
        submitBtn=(Button)findViewById(R.id.submitBtn);
        //get shared preference
        pref = getSharedPreferences("ClerkInfo",MODE_PRIVATE);

        Intent intent=getIntent();
        disbursementID.setText(intent.getExtras().getString("disbursmentID"));
        itemID.setText(intent.getExtras().getString("ItemCode"));
        wantedQty.setText(intent.getExtras().getString("RequiredQty"));
        DeliveredQty.setText(intent.getExtras().getString("ReceivedQty"));

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ClickSubmitButton();

            }
        });
    }
    private void ClickSubmitButton() {
        clickTime2=1;
        String url ="http://10.0.2.2:62084/api/Disbursement/SubmitDiscrepency";
        stringRequest = new StringRequest(Request.Method.POST, url,
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
                Map<String,String> params = new HashMap<>();
                params.put("DisbursementID",disbursementID.getText().toString());
                params.put("ItemID",itemID.getText().toString());
                params.put("ActualQty",wantedQty.getText().toString());
                params.put("DeliveredQty",DeliveredQty.getText().toString());
                params.put("Reason",Reason.getText().toString());

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20*1000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        Toast.makeText(this,"Submit Successfully",Toast.LENGTH_LONG).show();

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
