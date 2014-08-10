package org.technologyhatchery.datasource;

/**
 * Created by Alfred on 8/5/2014.
 */

public class Person {
    private String firstname;
    private String lastname;
    private PhoneNumber phone;
    private PhoneNumber fax;

    public Person() {
        phone = null;
        fax = null;
    }

    public Person(String first, String last) {
        firstname = first;
        lastname = last;
        phone = null;
        fax = null;
    }

    public void setPhone(PhoneNumber myPhone) {
        phone = myPhone;
    }

    public PhoneNumber getPhone() {
        return phone;
    }

    public void setFax(PhoneNumber myFax) {
        fax = myFax;
    }

   public PhoneNumber getFax() {
       return fax;
   }


}
