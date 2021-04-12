package com.example.logic_university;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import org.w3c.dom.Text;
import java.util.ArrayList;
import java.util.List;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class DisbursementAdapter extends ArrayAdapter {
    List list=new ArrayList();
    public DisbursementAdapter(Context context, int resource)
    {
        super(context, resource);
    }

    public void add(ListItem object) {
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
        Holder holder=new Holder();
        View row=convertView;
        if(row==null){
            LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.row1,parent,false);
            holder.deptName=row.findViewById(R.id.departmentName);
            holder.disbursmentID=row.findViewById(R.id.disbursementID);
//            holder.ViewBtn=row.findViewById(R.id.ViewBtn);
            row.setTag(holder);
        }
        else{
            holder=(Holder)row.getTag();
        }
        ListItem listItem=(ListItem)this.getItem(position);
        holder.deptName.setText(listItem.getDepname());
        holder.disbursmentID.setText(listItem.getID());
        return row;
    }
    static class Holder{
        TextView deptName,disbursmentID;
//        Button ViewBtn;
    }
}
