package com.test.creditinfo;

public class UserHelperClass {

    String name,businessname,phonenumber,email,password;

    public UserHelperClass() {
    }

    public UserHelperClass(String name, String businessname, String phonenumber, String email, String password) {
        this.name = name;
        this.businessname = businessname;
        this.phonenumber = phonenumber;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
