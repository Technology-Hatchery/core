// To save as "<CATALINA_HOME>\webapps\helloservlet\WEB-INF\src\mypkg\HelloWorldExample.java"
package org.technologyhatchery.servlets;

//import com.google.gson.stream.JsonWriter;
//import com.mobinsight.server.EMF;
import org.technologyhatchery.samples.datasources.LoadData;
import org.technologyhatchery.utilities.WebCall;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.ResourceBundle;

public class SimpleServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // Set the response message's MIME type.
        response.setContentType("text/html;charset=UTF-8");
        // Allocate an output writer to write the response message into the network socket.
        PrintWriter out = response.getWriter();
        //JsonWriter writer = new JsonWriter(out);

        // Use a ResourceBundle for localized string in "LocalStrings_xx.properties" for i18n.
        // The request.getLocale() sets the locale based on the "Accept-Language" request header.
        //ResourceBundle rb = ResourceBundle.getBundle("LocalStrings", request.getLocale());

        //Read survey from a file
        String filePath = "../../assets/json/surveys/";

        // Write the response message, in an HTML document.
        try {
            out.println("<!DOCTYPE html>");  // HTML 5
            out.println("<html><head>");
            out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
            String title = "Hello World!";
            out.println("<title>" + title + "</title></head>");
            out.println("<body>");

            Date date = new Date();
            out.println(String.valueOf(date));
            out.println("<br />");

            // ------------- Data Entries (Start) ---------------------
            // Create an EntityManagerFactory for this "persistence-unit"
            // See the file "META-INF/persistence.xml"
            out.println("Try to Create Entity Manager Factory<br />");
            //TODO-Alfred Figure out how this works
            //EntityManagerFactory emf = EMF.get();
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("transactions-optional");
            out.println("Try to Create Entity Manager<br />");
            EntityManager em = emf.createEntityManager();
            out.println("Successfully Created Entity Manager<br />");
            LoadData ex = new LoadData();
            WebCall webCall = new WebCall("http://www.earthtools.org/timezone/40.71417/-74.00639");

            /*out.println("Testing Webservice<br />");
            out.println("=============================<br />");
            out.println(webCall.performWebCall());
            out.println("<br /><br />");*/

            out.println("Starting to execute the statements<br />");
            out.println("=============================<br />");

            out.println("Starting DeleteItems<br />");
            ex.deleteItems(emf, out);
            out.println("Finished DeleteItems<br />");

            out.println("Starting CreateUsers<br />");
            ex.createUsers(emf, out);
            out.println("Finished CreateUsers<br />");


            out.println("Starting CreateAdd<br />");
            ex.createAdd(emf, out, filePath);
            out.println("Finished CreateAdd<br />");

            out.println("Starting RetrieveItems<br />");
            ex.retrieveItems(emf, out);
            out.println("Finished RetrieveItems<br />");
            // ------------- Data Entries (End) ---------------------

            out.println("<h1>" + title + "</h1>");  // Prints "Hello, world!"
            // Set a hyperlink image to refresh this page
            out.println("<a href='" + request.getRequestURI() + "'><img src='webapps/SimpleServlet/images/return.gif'></a>");
            out.println("</body></html>");
        } finally {
            out.close();  // Always close the output writer
        }
    }
}