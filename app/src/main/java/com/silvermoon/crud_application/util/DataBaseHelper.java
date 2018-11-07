package com.silvermoon.crud_application.util;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.silvermoon.crud_application.db.UserTable;

import java.util.ArrayList;

public class DataBaseHelper {

    public int insertDate(String First_Name, String Last_Name, String Mobile, String Email, String Location, String Address, String Password, String Current_location) {
        UserTable userTable = new UserTable(First_Name, Last_Name, Mobile, Email, Location, Address, Password, Current_location);
        userTable.save();
        return userTable.getId().intValue();
    }

    // this is used for showing record where id = ?
    public ArrayList<UserTable> getSingleRecord(String id) {
        Select select = new Select();
        return select.from(UserTable.class).where("id = ?", id).execute();
    }

    //check user using email_id
    public ArrayList<UserTable> check_user_exist(String email) {
        Select select = new Select();
        return select.from(UserTable.class).where("email_id = ?", email).execute();
    }

    public int updateRecords(String id, String First_Name, String Last_Name, String Mobile, String Email, String Location, String Address, String Password) {
        // update user
        ArrayList<UserTable> data = new Select().from(UserTable.class)
                .where("id = ?", id).execute();
        for (int i = 0; i < data.size(); i++) {
            data.get(i).first_name = First_Name;
            data.get(i).last_name = Last_Name;
            data.get(i).mobile_number = Mobile;
            data.get(i).email_id = Email;
            data.get(i).location = Location;
            data.get(i).address = Address;
            data.get(i).password = Password;
            data.get(i).save();

        }
        return Integer.parseInt(id);
    }

    public void update_location(String id, String current_location) {
        ArrayList<UserTable> data = new Select().from(UserTable.class).where("id = ?", id).execute();
        ArrayList<UserTable> data1 = new Select().from(UserTable.class).where("id = ?", id).execute();
    //    data.get(0).current_location = current_location;
     //   data.get(0).save();
    }

    public void deleteRecord(String id) {
        // to delete user
        new Delete().from(UserTable.class).where("id = ?", id)
                .executeSingle();

    }

}
