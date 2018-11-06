package com.silvermoon.crud_application.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "userTable")
public class UserTable extends Model {

    @Column(name = "first_name")
    public String first_name;
    @Column(name = "last_name")
    public String last_name;
    @Column(name = "mobile_number")
    public String mobile_number;
    @Column(name = "email_id")
    public String email_id;
    @Column(name = "location")
    public String location;
    @Column(name = "address")
    public String address;
    @Column(name = "password")
    public String password;

    public UserTable() {
        super();
    }

    public UserTable(String First_Name, String Last_Name, String Mobile, String Email, String Location, String Address, String Password) {
        super();
        this.first_name = First_Name;
        this.last_name = Last_Name;
        this.mobile_number = Mobile;
        this.email_id = Email;
        this.location = Location;
        this.address = Address;
        this.password = Password;
    }
}
