package com.test.creditinfo;

public class ViewReportList {
    private String customer;
    private String date;
    private String yougave;
    private String yougot;

    public ViewReportList()
    {

    }

    public ViewReportList(String customer, String date, String yougave, String yougot) {
        this.customer = customer;
        this.date = date;
        this.yougave = yougave;
        this.yougot = yougot;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYougave() {
        return yougave;
    }

    public void setYougave(String yougave) {
        this.yougave = yougave;
    }

    public String getYougot() {
        return yougot;
    }

    public void setYougot(String yougot) {
        this.yougot = yougot;
    }
}
