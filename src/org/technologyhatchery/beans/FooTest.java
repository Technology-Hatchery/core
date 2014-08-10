package org.technologyhatchery.beans;

/**
 * Created by Alfred on 8/5/2014.
 */

import java.util.*;

public class FooTest {
    public static void main (String [] args) throws Exception{
        Random rnd = new Random();

        Foo f1 = new Foo();
        f1.setFoo("bary");
        FooHelper.write(f1, "foo.xml");

        Foo f2 = FooHelper.read("foo.xml");
        System.out.println("Foo" + f2.getFoo());
        // the output : Foobar
    }
}
