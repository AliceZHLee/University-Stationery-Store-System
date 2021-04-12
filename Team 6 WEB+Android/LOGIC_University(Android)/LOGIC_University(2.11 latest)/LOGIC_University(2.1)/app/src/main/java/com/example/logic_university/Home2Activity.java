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
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class Home2Activity extends AppCompatActivity implements View.OnClickListener,AsyncTaskGET.IServerResponse3 {
    Button disbursementFormBtn;
    ListView list1;
    private List<ListItem> listItems;
    static int disbursement_num=0;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        //get shared preference
        pref = getSharedPreferences("ClerkInfo",MODE_PRIVATE);
        String staffName = pref.getString("username","");//used for retrieving the department info
        ListView list1=(ListView)findViewById(R.id.listView1);
        int disbursementId;
        listItems=new ArrayList<>();
        Intent callerIntent=getIntent();
        if(callerIntent!=null){
            String user=callerIntent.getStringExtra("username");
            Toast welmsg=Toast.makeText(this,"welcome "+staffName,Toast.LENGTH_LONG);
            welmsg.show();
        }

        disbursementFormBtn=(Button)findViewById(R.id.disbursementform);
        if(disbursementFormBtn!=null){
            disbursementFormBtn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v2) {
        ClickDisbursementButton();
    }

    private void ClickDisbursementButton() {
        AsyncTaskGET task2=new AsyncTaskGET();
        String url_disbursement="http://10.0.2.2:62084/api/Disbursement/DisbursementList";
        Command3 disbursement_task=new Command3(this,url_disbursement);
        task2.execute(disbursement_task);
    }

    @Override
    public void onServerResponse(JSONObject jsonObject) {
        Intent disbursement_intent=new Intent(this,clerk_disbursementForm.class);
        disbursement_intent.putExtra("disbursement_data",jsonObject.toString());
        startActivity(disbursement_intent);
        disbursement_num=0;

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
