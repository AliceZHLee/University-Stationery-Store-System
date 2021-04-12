package com.example.logic_university;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class Dep_disbursementDetailAdapter extends ArrayAdapter {
    List list=new ArrayList();
    public Dep_disbursementDetailAdapter(Context context, int resource)
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DepHolder holder=new DepHolder();
        View row=convertView;
        if(row==null){
            LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.row_for_dep_side_disbursement_details,parent,false);
            holder.itemCode=row.findViewById(R.id.depITEMCODE);
            holder.stationery_description=row.findViewById(R.id.depDESCRIPTION);
            holder.required_Qty=row.findViewById(R.id.depREQUIREDqty);
            holder.receivedQty=row.findViewById(R.id.depRECEIVEDqty);
            row.setTag(holder);
        }
        else{
            holder= (DepHolder) row.getTag();
        }
        ListofDisbursementItem listItem=(ListofDisbursementItem) this.getItem(position);
        holder.itemCode.setText(listItem.getItemCode());
        holder.stationery_description.setText(listItem.getStationeryDescription());
        holder.required_Qty.setText(listItem.getRequiredQty());
        holder.receivedQty.setText(listItem.getReceivedQty());
        return row;
    }
    static class DepHolder{
        TextView itemCode,stationery_description,required_Qty,receivedQty;
    }
}

