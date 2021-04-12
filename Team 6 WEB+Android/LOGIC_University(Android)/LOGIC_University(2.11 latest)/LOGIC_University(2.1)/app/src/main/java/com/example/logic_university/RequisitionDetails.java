package com.example.logic_university;

import org.json.JSONException;
import org.json.JSONObject;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class RequisitionDetails {

    public int DisbursementID;
    public String ItemID;
    public int DeliveredQty;
    public String DisbursementDetailsStatus;

    public RequisitionDetails(int disbursementID, String itemID, int deliveredQty, String disbursementDetailsStatus) {
        DisbursementID = disbursementID;
        ItemID = itemID;
        DeliveredQty = deliveredQty;
        DisbursementDetailsStatus = disbursementDetailsStatus;
    }

    public int getDisbursementID() {
        return DisbursementID;
    }

    public void setDisbursementID(int disbursementID) {
        DisbursementID = disbursementID;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public int getDeliveredQty() {
        return DeliveredQty;
    }

    public void setDeliveredQty(int deliveredQty) {
        DeliveredQty = deliveredQty;
    }

    public String getDisbursementDetailsStatus() {
        return DisbursementDetailsStatus;
    }

    public void setDisbursementDetailsStatus(String disbursementDetailsStatus) {
        DisbursementDetailsStatus = disbursementDetailsStatus;
    }


    public JSONObject getReqDetJSONObject() {

        JSONObject obj = new JSONObject();

        try {
            obj.put("DisbursementID", DisbursementID);
            obj.put("ItemID", ItemID);
            obj.put("DeliveredQty",DeliveredQty);
            obj.put("DisbursementDetailsStatus", DisbursementDetailsStatus);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
