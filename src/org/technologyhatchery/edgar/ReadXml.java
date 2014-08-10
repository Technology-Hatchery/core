package org.technologyhatchery.edgar;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.IOUtils;
import org.technologyhatchery.datasource.Person;
import org.technologyhatchery.datasource.PhoneNumber;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Alfred on 8/5/2014.
 */
public class ReadXml {

    public static void main (String [] args) throws Exception {
        //String filePath;
        Edgar edgar = new Edgar();

        //Download RSS
        String filePath = "C:\\Users\\Alfred\\Dropbox\\Technology Hatchery Inc\\technical\\Git\\IB_client";
        String stylesheetRss = "C:\\Users\\Alfred\\Dropbox\\Technology Hatchery Inc\\technical\\Git\\IB_client\\transform\\rss_feed.xsl";
        String stylesheetFiling = "C:\\Users\\Alfred\\Dropbox\\Technology Hatchery Inc\\technical\\Git\\IB_client\\transform\\filing.xsl";
        for (int year = 2005; year < 2015; year++) {
            for (int month = 1; month < 13; month++) {
                if (year==2005 && month<4) {
                    continue;
                }
                else if (year==2014 && month>7) {
                    continue;
                } else {
                    //Download RSS feed (from internet)
                    //edgar.sec_download(year, month, filePath);

                    //Convert RSS (from existing file to new file)
                    //edgar.convertRSS(year, month, filePath, stylesheetRss);

                    //Import RSS (from existing file to new object)
                    RSS rss = edgar.importRSS(year, month, filePath);
                    edgar.feeds.add(rss);

                    //Download enclosures (from internet)
                    //edgar.downloadEnclosures(year, month, filePath);

                    //Extract enclosures (from existing file to new folder)
                    //edgar.extractZipFiles(year, month, filePath);

                    //Convert enclosures (from existing file to new file)
                    //edgar.convertEnclosures(year, month, filePath, stylesheetFiling);

                    //Import enclosure (from existing file to new object)
                    ArrayList<Filing> filings = edgar.importEnclosure(year, month, filePath);
                    for (Filing filing : filings) {
                        edgar.filings.add(filing);
                    }
                }
            }
        }
        System.out.println("Finished Reading!");


    }

}
