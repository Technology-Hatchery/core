package org.technologyhatchery.datasource;

/**
 * Created by Alfred on 8/5/2014.
 */
public class PhoneNumber {
    private int code;
    private String number;

    public PhoneNumber() {
        //Do nothing
    }

    public PhoneNumber(int myCode, String myNumber) {
        code = myCode;
        number = myNumber;
    }

    public void setCode(int myCode) {
        code = myCode;
    }

    public int getCode() {
        return code;
    }

    public void setNumber(String myNumber) {
        number = myNumber;
    }

    public String getNumber() {
        return number;
    }
}
