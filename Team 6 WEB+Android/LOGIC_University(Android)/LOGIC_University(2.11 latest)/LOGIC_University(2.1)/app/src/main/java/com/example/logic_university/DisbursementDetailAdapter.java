package com.example.logic_university;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

/*Instead, we put disbursementDetailAdapter class inside the disbursementDetailActivity  for some other function to realize*/



//public class DisbursementDetailAdapter  extends ArrayAdapter {
//
//    List list=new ArrayList();
//    public DisbursementDetailAdapter(Context context, int resource)
//    {
//        super(context, resource);
//    }
//
//    public void add(ListofDisbursementItem object) {
//        super.add(object);
//        list.add(object);
//    }
//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list.get(position);
//    }
//
//    @NonNull
//    @Override
//    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        DetailHolder holder=new DetailHolder();
//        View row=convertView;
//        if(row==null){
//            LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            row=layoutInflater.inflate(R.layout.row1,parent,false);
//            holder.itemCode=row.findViewById(R.id.ITEMCODE);
//            holder.stationery_description=row.findViewById(R.id.DESCRIPTION);
//            holder.required_Qty=row.findViewById(R.id.REQUIREDqty);
//            holder.receivedQty=row.findViewById(R.id.RECEIVEDqty);
//            holder.createDiscrepancyBtn=row.findViewById(R.id.CreateDiscrepancyBtn);
//            row.setTag(holder);
//        }
//        else{
//            holder=(DetailHolder)row.getTag();
//        }
//        ListofDisbursementItem listItem=(ListofDisbursementItem) this.getItem(position);
//        holder.itemCode.setText(listItem.getItemCode());
//        holder.stationery_description.setText(listItem.getStationeryDescription());
//        holder.required_Qty.setText(listItem.getRequiredQty());
//        holder.receivedQty.setText(listItem.getReceivedQty());
//
//        holder.createDiscrepancyBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //
//            }
//        });
//
//        return row;
//    }
//    static class DetailHolder{
//        TextView itemCode,stationery_description,required_Qty;
//        EditText receivedQty;
//        Button createDiscrepancyBtn;
//    }
//}





