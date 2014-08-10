package org.technologyhatchery.edgar;

import com.sun.net.ssl.HttpsURLConnection;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.technologyhatchery.utilities.*;
/**
 * Created by Alfred on 8/6/2014.
 */
public class Edgar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @OneToMany
    public ArrayList<Filing> filings;
    @OneToMany
    public ArrayList<RSS> feeds;
    private static boolean overwriteOptional = false;

    public Edgar() {
        feeds = new ArrayList<>();
        filings = new ArrayList<>();
    }

    /**
     * Generates the feed url from the given year and month.
     *
     * @param year
     * @param month
     * @return
     */
    public String feed_select(String year, String month) {
        return "http://www.sec.gov/Archives/edgar/monthly/xbrlrss-" + year + "-" + month + ".xml";
    }

    /***
     * Checks whether RSS feed is up to date
     *
     * @param year
     * @param month
     * @return
     */
    public boolean checkRSS(String year, String month) {
        //TODO-Alfred Needs implementing
        return true;
    }

    /***
     * Download the enclosures contained in the given RSS
     *
     * @param year
     * @param month
     * @param target
     */
    public void downloadEnclosures(int year, int month, String target) {
        //TODO-Alfred Update to download the xml file directly from the enclosures to capture pre 04-2009 files
        String monthStr = ("0" + String.valueOf(month));
        monthStr = monthStr.substring(monthStr.length() - 2, monthStr.length());   //Get last two characters of string
        String yearStr = String.valueOf(year);

        //Import RSS feed from file
        FileInputStream inputStream;
        String readXml;
        XStream xstream = new XStream();
        RSS rss;
        rss = importRSS(year, month, target);

        for (Item item : rss.items) {

            String zipFile;
            URL url;
            URLConnection con;
            //HttpsURLConnection con;
            InputStream stream;
            try {
                zipFile = item.itemsMap.get("enclosure");
                if (zipFile != null) {
                    zipFile = zipFile.substring(zipFile.lastIndexOf("/")+1, zipFile.length()).trim();
                    if (checkExistingFile(target + "\\sec\\" + yearStr + "\\" + monthStr + "\\" + zipFile)) {
                        System.out.println("Enclosure " + zipFile + " already exists.");
                        continue;
                    }
                } else {
                    return;
                }


                url = new URL(item.itemsMap.get("enclosure"));
                //con = (HttpsURLConnection) url.openConnection();
                con = url.openConnection();
                stream = con.getInputStream();
                Files.copy(stream, Paths.get(target + "\\sec\\" + yearStr + "\\" + monthStr + "\\" + zipFile));
            } catch (MalformedURLException e) {
                //Do nothing
            } catch (IOException e) {
                //Do nothing
            }
        }
        /*//Print out all the keys
        for (String key : rss.infoMap.keySet()) {
            System.out.println(key + ": " + rss.infoMap.get(key));
        }*/
   }

    /***
     *
     * @param year
     * @param month
     * @param target
     */
    public RSS importRSS(int year, int month, String target) {
        String monthStr = ("0" + String.valueOf(month));
        monthStr = monthStr.substring(monthStr.length()-2,monthStr.length());   //Get last two characters of string
        String yearStr = String.valueOf(year);

        String readXml;
        XStream xstream;
        RSS rss;
        try {
            FileInputStream inputStream = new FileInputStream(target + "\\sec\\" + yearStr + "\\" + monthStr + "\\rss.xml");
            //DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            //DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            //Document document = dBuilder.parse(inputStream);
            readXml = IOUtils.toString(inputStream);
            inputStream.close();

            //Create class from file
            xstream = new XStream();
            rss = (RSS)xstream.fromXML(readXml);
            //System.out.println("Year: " + yearStr + " Month: " + monthStr);
            return rss;
        } catch (FileNotFoundException e) {
            System.out.println("RSS XML file not found for year: " + yearStr + " month: " + monthStr);
            return null;
        } catch (IOException e) {
            System.out.println("IO Exception during RSS XML file read for year: " + yearStr + " month: " + monthStr);
            return null;
        }
    }

    /***
     * Returns all the enclosures that are stored in XML filings for the given year and month
     *
     * @param year
     * @param month
     * @param target
     */
    public ArrayList<Filing> importEnclosure(int year, int month, String target) {
        String monthStr = ("0" + String.valueOf(month));
        monthStr = monthStr.substring(monthStr.length()-2,monthStr.length());   //Get last two characters of string
        String yearStr = String.valueOf(year);

        String readXml;
        XStream xstream;
        ArrayList<Filing> filings = new ArrayList<>();
        Filing filing;
        try {
            File curDir = new File(target + "\\sec\\" + yearStr + "\\" + monthStr);
            File[] filesList = curDir.listFiles();
            for (File f : filesList) {
                if(f.isDirectory()) {
                    File[] fileSubList = f.listFiles();
                    for (File fSub : fileSubList) {
                        if (fSub.getName().equalsIgnoreCase("filing.xml")) {
                            FileInputStream inputStream = new FileInputStream(fSub.getPath());
                            //DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                            //DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                            //Document document = dBuilder.parse(inputStream);
                            readXml = IOUtils.toString(inputStream);
                            inputStream.close();

                            //Create class from file
                            xstream = new XStream();
                            filing = (Filing)xstream.fromXML(readXml);
                            filings.add(filing);
                            //System.out.println("Year: " + yearStr + " Month: " + monthStr);
                        }
                    }
                }
             }
        } catch (FileNotFoundException e) {
            //Do nothing
            return null;
        } catch (IOException e) {
            //Do nothing
            return null;
        }
        return filings;
    }

    /***
     * Imports RSS feed from file
     *
     * @param year
     * @param month
     * @param target
     * @param stylesheet
     * @return
     */
    public RSS convertRSS(int year, int month, String target, String stylesheet) {
        //Import downloaded RSS
        String monthStr = ("0" + String.valueOf(month));
        monthStr = monthStr.substring(monthStr.length()-2,monthStr.length());   //Get last two characters of string
        String yearStr = String.valueOf(year);

        if (checkExistingFile(target + "\\sec\\" + yearStr + "\\" + monthStr + "\\" + "rss.xml")) {
            System.out.println("Feed " + yearStr + "\\" + monthStr + "\\" + "rss.xml already exists");
            return null;
        }

        try {
            FileInputStream inputStream = new FileInputStream(target + "\\sec\\" + yearStr + "\\" + monthStr + "\\rss_feed.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(inputStream);

            //Apply XSLT template
            dbFactory.setNamespaceAware(true);
            dbFactory.setValidating(true);

            //File stylesheet = new File(argv[0]);
            //File datafile = new File(argv[1]);

            //DocumentBuilder builder = factory.newDocumentBuilder();
            //document = builder.parse(datafile);

            // Use a Transformer for output
            SAXTransformerFactory tFactory = (SAXTransformerFactory)TransformerFactory.newInstance();
            //TransformerFactory tFactory = TransformerFactory.newInstance();
            //StreamSource stylesource = new StreamSource(stylesheet);
            Source xslt = new StreamSource(new File(stylesheet));
            Transformer transformer = tFactory.newTransformer(xslt);

            DOMSource source = new DOMSource(document);
            PrintWriter writer = new PrintWriter(target + "\\sec\\" + yearStr + "\\" + monthStr + "\\rss.xml", "UTF-8");
            //writer.println(urlString);
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            writer.close();
        } catch (TransformerConfigurationException tce) {
            // Error generated by the parser
            System.out.println("\n** Transformer Factory error");
            System.out.println("   " + tce.getMessage());

            // Use the contained exception, if any
            Throwable x = tce;

            if (tce.getException() != null) {
                x = tce.getException();
            }

            x.printStackTrace();
        } catch (TransformerException te) {
            // Error generated by the parser
            System.out.println("\n** Transformation error");
            System.out.println("   " + te.getMessage());

            // Use the contained exception, if any
            Throwable x = te;

            if (te.getException() != null) {
                x = te.getException();
            }

            x.printStackTrace();
        } catch (SAXException sxe) {
            // Error generated by this application
            // (or a parser-initialization error)
            Exception x = sxe;

            if (sxe.getException() != null) {
                x = sxe.getException();
            }

            x.printStackTrace();
        } catch (ParserConfigurationException pce) {
            // Parser with specified options can't be built
            pce.printStackTrace();
        } catch (IOException ioe) {
            // I/O error
            ioe.printStackTrace();
        }

        RSS rss = new RSS();
        //RSS rss = new RSS(doc);
        feeds.add(rss);

        return rss;
    }

    /***
     * Simulates optional parameter.
     *
     * @param year
     * @param month
     * @param target
     */
    public void sec_download(int year, int month, String target) {
        sec_download(year, month, target, overwriteOptional);
    }

    /***
     * Downloads the RSS feed associated with the given year and month
     * and saves it to a file.
     *
     * @param year
     * @param month
     * @param target
     */
    public void sec_download(int year, int month, String target, boolean overwrite) {

        //Check for existing directories
        checkDirectory(target + "\\sec\\" + year);
        String monthStr = ("0" + String.valueOf(month));
        monthStr = monthStr.substring(monthStr.length()-2,monthStr.length());   //Get last two characters of string
        String yearStr = String.valueOf(year);
        checkDirectory(target + "\\sec\\" + yearStr + "\\" + monthStr);

        //Check for existing download
        if (checkExistingFile(target + "\\sec\\" + yearStr + "\\" + monthStr + "\\" + "rss_feed.xml")) {
            System.out.println("Feed " + yearStr + "\\" + monthStr + "\\" + "rss_feed.xml already exists");
            return;
        }

        try {
            //URL url = new URL(feed_select(year, month));
            URL url = new URL(feed_select(yearStr, monthStr));
            InputStream stream = url.openStream();
            String urlString = IOUtils.toString(stream, "UTF-8");
            //OutputStream output = new OutputStream()
            PrintWriter writer = new PrintWriter(target + "\\sec\\" + yearStr + "\\" + monthStr + "\\rss_feed.xml", "UTF-8");
            writer.println(urlString);
            writer.close();
            //Util.copyStream(stream, writer);

        }
        catch (MalformedURLException e) {
            // new URL() failed
            // ...

        }
        catch (IOException e) {
            // openConnection() failed
            // ...

        }
    }

    /***
     * Checks whether the given directory exists.  It creates it if it does not.
     *
     * @param target
     * @return
     */
    private boolean checkDirectory(String target) {
        Path path = Paths.get(target);
        if (Files.notExists(path)) {
            //Files.createDirectories(path.getParent());
            try {
                Files.createDirectory(path, new FileAttribute<?>[0]);
            }
            catch(IOException ex) {
                System.err.println("Could not create new directory");
            }
            return false;
        }
        return true;
    }

    /***
     * Checks for existing file
     *
     * @param target
     * @return
     */
    public boolean checkExistingFile(String target) {
        File file = new File(target);
        return file.exists() && !file.isDirectory();
    }

    /***
     * Extracts the zip files.
     *
     * @param target
     * @return
     */
    public void extractZipFiles(int year, int month, String target) {
        String monthStr = ("0" + String.valueOf(month));
        monthStr = monthStr.substring(monthStr.length() - 2, monthStr.length());   //Get last two characters of string
        String yearStr = String.valueOf(year);
        String extractDirectory;

        UnZip unZip = new UnZip();
        File curDir = new File(target + "\\sec\\" + yearStr + "\\" + monthStr);
        File[] filesList = curDir.listFiles();
        for (File f : filesList) {
            //if(f.isDirectory())
            //    System.out.println(f.getName());
            if (f.isFile()) {
                if (FilenameUtils.getExtension(f.getPath()).equalsIgnoreCase("zip")) {
                    extractDirectory = target + "\\sec\\" + yearStr + "\\" + monthStr + "\\" + f.getName().substring(0,f.getName().length()-4);
                    extractDirectory = "O:\\TntDrive\\" + extractDirectory.substring(3);
                    if (!checkDirectory(extractDirectory)) {
                        unZip.unZipIt(f.getPath(), extractDirectory);
                    }
                }
                //System.out.println(f.getName());
            }
        }
    }

    /***
     * Converts the extracted enclosures to class Filing xml files using XSLT
     *
     * @param year
     * @param month
     * @param target
     */
    public void convertEnclosures(int year, int month, String target, String stylesheet) {
        String monthStr = ("0" + String.valueOf(month));
        monthStr = monthStr.substring(monthStr.length() - 2, monthStr.length());   //Get last two characters of string
        String yearStr = String.valueOf(year);

        //Loop through all the folder, which are assumed to be extracted filings
        File curDir = new File(target + "\\sec\\" + yearStr + "\\" + monthStr);
        File curSubDir;
        File[] filesList = curDir.listFiles();
        File[] filesSubList;
        //File[] fileSubSubList;
        for (File f : filesList) {
            //if(f.isDirectory())
            //    System.out.println(f.getName());
            if (!f.isFile()) {
                curSubDir = new File(f.getPath());
                filesSubList = curSubDir.listFiles();
                    for (File fSub : filesSubList) {
                        if (FilenameUtils.getExtension(fSub.getPath()).equalsIgnoreCase("xml")) {
                            if (!fSub.getName().equals("defnref.xml") && !fSub.getName().equals("filing.xml")) {
                                if (!fSub.getName().contains("_")) {
                                    try {
                                        FileInputStream inputStream = new FileInputStream(fSub.getPath());
                                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                                        Document document = dBuilder.parse(inputStream);

                                        //Apply XSLT template
                                        dbFactory.setNamespaceAware(true);
                                        dbFactory.setValidating(true);

                                        // Use a Transformer for output
                                        SAXTransformerFactory tFactory = (SAXTransformerFactory)TransformerFactory.newInstance();
                                        //TransformerFactory tFactory = TransformerFactory.newInstance();
                                        //StreamSource stylesource = new StreamSource(stylesheet);
                                        Source xslt = new StreamSource(new File(stylesheet));
                                        Transformer transformer = tFactory.newTransformer(xslt);

                                        DOMSource source = new DOMSource(document);
                                        PrintWriter writer = new PrintWriter(f.getPath() + "\\filing.xml", "UTF-8");
                                        //writer.println(urlString);
                                        StreamResult result = new StreamResult(writer);
                                        transformer.transform(source, result);
                                        writer.close();
                                    } catch (IOException e) {
                                        //Do nothing
                                    } catch (ParserConfigurationException e) {
                                        //Do nothing
                                    } catch (SAXException e) {
                                        //Do nothing
                                    } catch (TransformerConfigurationException e) {
                                        //Do nothing
                                    } catch (TransformerException e) {
                                        //Do nothing
                                    }
                                }
                            }
                        }
                    }
                //System.out.println(f.getName());
            }
        }
    }

    /***
     * Saves the given class to an XML file using XStream
     *
     * @param obj
     * @param target
     */
    public void saveToFile(Object obj, String target) {

            XStream xstream = new XStream();
            try {
                String writeXml = xstream.toXML(obj);
                xstream = new XStream();
                PrintWriter writer = new PrintWriter(target, "UTF-8");
                writer.println(writeXml);
                //System.out.println(writeXml);
                writer.close();
            } catch (UnsupportedEncodingException e) {
                //Do nothing
            } catch (FileNotFoundException e) {
                //Do nothing
            }

    }
}
