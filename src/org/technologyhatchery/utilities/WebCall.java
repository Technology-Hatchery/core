package org.technologyhatchery.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Alfred on 8/2/2014.
 */
public class WebCall {

    private static String mResource;

    public WebCall(String resource) {
        mResource = resource;
    }

    /**
     *
     * @return A string of the return response
     */
    public static String performWebCall() {

        String out = "";

        try {
            URL url = new URL(mResource);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            System.out.println("Printing out response from webCall");
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                out = out + inputLine;
            }
            in.close();
            //out = in.toString();
        }
        catch (MalformedURLException e) {
            System.out.println("URL Exception: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }

        return out;
    }

}
