package com.example.logic_university;

/*
Android code is 95 % done by Li Zhengyi,with the help of teammate Sriram and Emma in some errors' debugging and fixing
*/

public class ListofDisbursementItem {
    private String ItemCode;
    private String StationeryDescription;
    private String requiredQty;
    private String receivedQty;

    public ListofDisbursementItem(String itemCode, String stationeryDescription, String requiredQty, String receivedQty) {
        ItemCode = itemCode;
        StationeryDescription = stationeryDescription;
        this.requiredQty = requiredQty;
        this.receivedQty = receivedQty;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getStationeryDescription() {
        return StationeryDescription;
    }

    public void setStationeryDescription(String stationeryDescription) {
        StationeryDescription = stationeryDescription;
    }

    public String getRequiredQty() {
        return requiredQty;
    }

    public void setRequiredQty(String requiredQty) {
        this.requiredQty = requiredQty;
    }

    public String getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(String receivedQty) {
        this.receivedQty = receivedQty;
    }
}
