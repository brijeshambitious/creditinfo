package com.test.creditinfo;

public class CustomerList {

    private String name;
    private String phonenumber;

    public CustomerList()
    {

    }

    public CustomerList(String customer, String phonenumber) {
        this.name = customer;
        this.phonenumber = phonenumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
