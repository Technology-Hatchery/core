package org.technologyhatchery.edgar;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Alfred on 8/6/2014.
 */
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @OneToMany
    public HashMap<String, String> itemsMap;
    //public Date creationDate;
    //public String creationString;

    public Item() {
        itemsMap = new HashMap<>();
        //creationDate = new java.util.Date();
        //creationString =  new java.util.Date().toString();
    }

    public void printElements() {
        //Print out all the keys
        for (String key : itemsMap.keySet()) {
            System.out.println(key + ": " + itemsMap.get(key));
        }
    }
}
