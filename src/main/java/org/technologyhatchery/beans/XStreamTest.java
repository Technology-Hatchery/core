package org.technologyhatchery.beans;

/**
 * Created by Alfred on 8/5/2014.
 */

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.IOUtils;
import org.technologyhatchery.datasource.*;


//import javax.jms.Session;
import java.io.FileInputStream;
import java.io.PrintWriter;

public class XStreamTest {

    public static void main (String [] args) throws Exception {
        // New XStream object
        XStream xstream = new XStream();

        //Create a new object
        Person joe = new Person("Joe", "Walnes");
        joe.setPhone(new PhoneNumber(123, "1234-456"));
        joe.setFax(new PhoneNumber(123, "9999-999"));

        //Convert to XML
        String writeXml = xstream.toXML(joe);
        String filePath = "Person.xml";
        PrintWriter writer = new PrintWriter(filePath, "UTF-8");
        writer.println(writeXml);
        writer.close();
        System.out.println(writeXml);

        //Read in file
        FileInputStream inputStream = new FileInputStream(filePath);
        String readXml;
        try {
            readXml = IOUtils.toString(inputStream);
        } finally {
            inputStream.close();
        }

        //Create class from file
        Person newJoe = (Person)xstream.fromXML(readXml);
        //newJoe.getPhone().setNumber("newNumber!!");
        System.out.println(newJoe.getPhone().getNumber());
    }


}
