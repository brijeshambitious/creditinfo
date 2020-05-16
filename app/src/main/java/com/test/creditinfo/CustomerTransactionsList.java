package com.test.creditinfo;

public class CustomerTransactionsList {

    private String date;
    private String yougave;
    private String yougot;

    public CustomerTransactionsList()
    {

    }

    public CustomerTransactionsList(String date, String yougave, String yougot) {
        this.date = date;
        this.yougave = yougave;
        this.yougot = yougot;
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
