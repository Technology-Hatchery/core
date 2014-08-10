package org.technologyhatchery.beans;

/**
 * Created by Alfred on 8/5/2014.
 */

import java.util.*;

public class Foo {
    private String foo;
    private int something;

    public void setFoo(String s) {
        Random rnd = new Random();

        foo = s;
        something = rnd.nextInt(5);
    }

    public String getFoo() {
        return foo;
    }

    public String message() {return "Hi!"; }

    public void setSomething (int i) { something = i; }
    public int getSomething () { return something; }
}
