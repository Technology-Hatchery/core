package org.technologyhatchery.edgar;

import org.w3c.dom.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alfred on 8/6/2014.
 */
public class RSS {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @OneToMany
    public ArrayList<Item> items;
    @OneToMany
    public HashMap<String, String> infoMap;
    //private static HashMap<String, String> itemsMap = new HashMap<>();

    public RSS() {
        //Do nothing
    }

    public RSS(Document doc) {
        items = new ArrayList<>();
        infoMap = new HashMap<>();
        processFile(doc);
    }

    private void processFile(Document doc) {

        try {
            //File fXmlFile = new File(filePath);
            //DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            //DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            //Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();
            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            items = processRSS(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*//Print out all the keys
        for (String key : itemsMap.keySet()) {
            System.out.println(key + ": " + itemsMap.get(key));
        }*/

    }




    private ArrayList<Item> processRSS (Document doc){
        //Extract RSS Feed information
        NodeList nItems = doc.getElementsByTagName("channel").item(0).getChildNodes();
        Node nNode;
        Element eElement;
        //NamedNodeMap nAttributes;
        String nValue;
        String nName;
        for (int i = 0; i < nItems.getLength(); i++) {
            //Item newItem = new Item();
            //items.add(newItem);
            //NodeList nChildrenFirst = nItems.item(i).getChildNodes();
            //for (int j = 0; j < nChildrenFirst.getLength(); j++) {
            nNode = nItems.item(i);
            //System.out.println("\nCurrent Element: " + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                eElement = (Element) nNode;
                nName = eElement.getNodeName();
                //nAttributes = eElement.getAttributes();
                //Check if nil attribute is set to true
                //if (nAttributes.getNamedItem("xsi:nil") == null && nAttributes.getNamedItem("decimals") != null) {
                if (!nName.equalsIgnoreCase("item")) {
                    nValue = eElement.getTextContent().trim();
                    //System.out.println("Name: " + nName + " Value: " + nValue);
                    infoMap.put(nName, nValue);
                } else {
                    //Do nothing
                }
            } else {
                //Do nothing
            }
        //}
        }


        nItems = doc.getElementsByTagName("item");
        System.out.println("----------------------------");
        System.out.println("Num Items: " + nItems.getLength());

        //ArrayList<Item> items = new ArrayList<Item>();
        //NamedNodeMap nAttributes;
        for (int i = 0; i < nItems.getLength(); i++) {
            Item newItem = new Item();
            items.add(newItem);
            NodeList nChildrenFirst = nItems.item(i).getChildNodes();
            for (int j = 0; j < nChildrenFirst.getLength(); j++) {
                nNode = nChildrenFirst.item(j);
                //System.out.println("\nCurrent Element: " + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    eElement = (Element) nNode;
                    nName = eElement.getNodeName();
                    //nAttributes = eElement.getAttributes();
                    //Check if nil attribute is set to true
                    //if (nAttributes.getNamedItem("xsi:nil") == null && nAttributes.getNamedItem("decimals") != null) {
                    if (!nName.equalsIgnoreCase("edgar:xbrlFiling")) {
                        nValue = eElement.getTextContent().trim();
                        //System.out.println("Name: " + nName + " Value: " + nValue);
                        newItem.itemsMap.put(nName, nValue);
                    } else {
                        //Do nothing
                    }
                } else {
                    //Do nothing
                }
            }
        }

        //Print out titles of all items
        for (Item item : items) {
            System.out.println("Title: " + item.itemsMap.get("title"));
        }

        return items;
    }

}