package org.technologyhatchery.beans;

/**
 * Created by Alfred on 8/5/2014.
 */

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;

public class Beans {
    private String foo ;

    public void setFoo(String s) {
        foo = s;
    }

    public String getFoo() {
        return foo;
    }
}
