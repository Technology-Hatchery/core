/**********************************************************************
Copyright (c) 2006 Andy Jefferson and others. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors:
    ...
**********************************************************************/
package org.technologyhatchery.samples.datasources;

import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;
//import org.apache.commons.io.IOUtils;
//import org.datanucleus.api.jpa.JPAEntityManager;
//import org.datanucleus.jpa.EntityManagerImpl;
//import org.datanucleus.samples.jpa.tutorial.*;
import org.datanucleus.util.NucleusLogger;
//import org.datanucleus.jpa.EntityManagerFactoryImpl;
//import org.datanucleus.jpa.EntityManagerImpl;
import org.json.JSONObject;
//import javax.json;

import com.mobinsight.server.*;
import org.technologyhatchery.items.*;

//import javax.json.Json;
import javax.persistence.*;
import java.nio.Buffer;
import java.util.*;
import java.io.*;
import java.net.*;

/**
 * Controlling application for the DataNucleus Tutorial using JPA.
 * Uses the "persistence-unit" called "Tutorial".
 */
public class LoadData
{
    private static String persistenceUnit = "MobinsightAlfred";

    //Does nothing constructor
    public LoadData() {

    }

    //Constructor that accepts a PrintWriter and assigns it to the internal PrintWriter
    public LoadData(PrintWriter printWriter) {
        out = printWriter;
    }

    static PrintWriter out = null;

    /*public static void main(String args[])
    {
        //Calls the internal functions when executing from the command lind
        String filePath = "C:\\Users\\Alfred\\Dropbox\\Technology Hatchery Inc\\technical\\Git\\visualization-data-source_testing\\json\\mobinsight\\";
        addDataFromJson(filePath + "alfred.json");
        //List<Survey> results = retrieveSurvey("Registration branch");
        //listSurveys(results, filePath + "listSurveys.json");
        //listQuestions(results.get(0), filePath + "listQuestions.json");
    }*/

    public static void addDataFromJson(String filePath) {
        // Create an EntityManagerFactory for this "persistence-unit"
        // See the file "META-INF/persistence.xml"
        System.out.println("Create Entity Manager Factory<br />");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnit);
        //LoadData ex = new LoadData();

        System.out.println("Starting to execute the statements<br />");
        System.out.println("=============================");
        System.out.println("CreateAdd<br />");


        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            //tx.begin();

            System.out.println("New Survey<br />");
            //Survey survey = new Survey();
            System.out.println("New Question<br />");
            //Question question = new Question();
            System.out.println("New Answer<br />");
            //Answer answer = new Answer();

            //Read survey from a file
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            JsonReader jsonReader = new JsonReader(br);

            //File f = new File(filePath);
            //InputStream is = new FileInputStream(f);

            //System.out.println(convertStreamToString(is));
            System.out.println("Begin Reading in from File<br />");
            //survey.readFromJson(em, jsonReader);
            System.out.println("End Reading in from File<br />");
            System.out.println("Replaying all the Questions:<br />");
            //System.out.println(survey.getQuestions() + "<br />");

            System.out.println("<br />Here are the questions I read in from file 'alfred.json':<br />");
            //for (Question q : survey.getQuestions()) {
                //This writes to the console
            //    System.out.println(q.getText() + "<br />");
            //}

            System.out.println("Persisted has begun<br />");
            tx.begin();
            //em.persist(survey);
            tx.commit();
            System.out.println("Persisted has completed<br />");

            //Product product = new Product("Sony Discman", "A standard discman from Sony", 200.00);
            //inv.getProducts().add(product);
            //Book book = new Book("Lord of the Rings by Tolkien", "The classic story", 49.99, "JRR Tolkien",
            //        "12345678", "MyBooks Factory");
            //inv.getProducts().add(book);

            //em.persist(survey);
            //em.persist(question);
            //em.persist(answer);
        } catch (Exception e) {
            //NucleusLogger.GENERAL.error(">> Exception persisting data", e);
            System.err.println("Error persisting data : " + e.getMessage());
            return;
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            em.close();
        }
        //TODO-Alfred What does this do?
        //emf.getCache().evictAll();
        System.out.println("");
    }

    public static void createSurvey(EntityManagerFactory emf, PrintWriter out, String json) {
        EntityManager em;
        EntityTransaction tx;
        em = emf.createEntityManager();
        tx = em.getTransaction();

        try
        {
            //TODO-Alfred Update code so that it pulls in all files in this folder
            //Loops through all json files, assuming that each one is an individual survey
            out.println("Attempting to read json files4<br />");


            System.err.println("Read string");
            //JSONObject jObject = new JSONObject(alfredJson);
            JsonReader jsonReader = new JsonReader(new StringReader(json));
            System.err.println("Created jsonReader");

            //Json.createReader(new StringReader(alfredJson));
            //JsonReader jsonReader = Json.createReader(new StringReader(alfredJson));

            //FileReader fr = new FileReader(filePath + "/alfred.json");
            //JsonReader jsonReader = new JsonReader(fr);
            //out.println("Successfully input json<br />");
            //out.println("Start read information from json<br />");
            Survey curSurvey = new Survey();
            em = emf.createEntityManager();
            System.err.println("Created entityManager");
            out.println("Created entityManager<br />");
            curSurvey.readFromJson(em, jsonReader, out);
            out.println("Finished reading from alfred.json<br />");
            System.out.println("Finished reading from alfred.json");
            System.err.println("Finished reading from alfred.json");
            //out.println("Finish read information from json<br />");
            out.println("<br />Here are the questions I read in from file 'alfred.json':<br />");
            for ( Question q : curSurvey.getQuestions() ) {
                //This writes to the webpage
                out.println(q.getText() + "<br />");
                //Add random response for Multiple Choice
                if (q.getAnswerType() == 0) {
                    q.addAnswers_MultipleChoice(emf, out);
                    for ( Answer answer : q.getAnswers() ) {
                        out.println("User Id: " + answer.mUser.getId() + " AnswerIndex: " + answer.mAnswerIndex + "<br />");
                    }
                }
                //Add some text response for Text
                if (q.getAnswerType() == 2) {
                    q.addAnswers_Text(emf, out);
                    for ( Answer answer : q.getAnswers() ) {
                        out.println("User Id: " + answer.mUser.getId() + " AnswerText: " + answer.mAnswerText + "<br />");
                    }
                }
                //Add some numeric response to Range
                if (q.getAnswerType() == 1) {
                    q.addAnswers_Range(emf, out);
                    for ( Answer answer : q.getAnswers() ) {
                        out.println("User Id: " + answer.mUser.getId() + " AnswerTRange: " + answer.mAnswerRangeValue + "<br />");
                    }
                }
            }


            out.println("Persisted has begun for " + curSurvey.getName() + "<br />");
            tx = em.getTransaction();
            tx.begin();
            em.clear();
            em.flush();
            //em.persist(product);
            em.persist(curSurvey);
            tx.commit();
            out.println("Persisted has completed for " + curSurvey.getName() + "<br />");
            System.err.println("Persisted has completed for " + curSurvey.getName() + "<br />");

        }
        catch (Exception e)
        {
            //NucleusLogger.GENERAL.error(">> Exception persisting data", e);
            System.err.println("Error persisting data : " + e.getMessage());
            return;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            em.close();
        }
        //TODO-Alfred What does this do?
        //emf.getCache().evictAll();
        System.out.println("");



    }

    /**
     *
     * @param emf
     * @param out
     * @param filePath  The directory that has the json files
     */
    public static void createAdd(EntityManagerFactory emf, PrintWriter out, String filePath) {
        //EntityManager em;
        //EntityTransaction tx;
        //em = emf.createEntityManager();
        //tx = em.getTransaction();


        //out.println("Creating objects<br />");
        //Survey survey = new Survey();
        //Question question = new Question();
        //Answer answer = new Answer();
        //out.println("Successfully Created objects<br />");

        // ------- alfred.json ----------
        out.println("Started reading from alfred.json<br />");
        System.out.println("Started reading from alfred.json");
        System.err.println("Started reading from alfred.json");

        //Read in alfred.json directly from code
        String alfredJson1 = "{ \"creator\" : \"alfred\",\n" +
                "  \"name\" : \"Alfred's Survey1\",\n" +
                "  \"questions\" : [ { \"answerType\" : 2,\n" +
                "        \"image\" : \"http://2.375585449099093977.surveyinsight.appspot.com/api/image?question=ag9zfnN1cnZleWluc2lnaHRyHAsSBlN1cnZleRiZxRAMCxIIUXVlc3Rpb24YAQw\",\n" +
                "        \"serialNumber\" : \"1\",\n" +
                "        \"text\" : \"How is survey formed?\"\n" +
                "      },\n" +
                "      { \"answerRange\" : { \"high\" : 100.0,\n" +
                "            \"low\" : 18.0,\n" +
                "            \"step\" : 1.0\n" +
                "          },\n" +
                "        \"answerType\" : 1,\n" +
                "        \"serialNumber\" : \"2\",\n" +
                "        \"text\" : \"What is your age?\"\n" +
                "      },\n" +
                "      { \"answerChoices\" : [ \"Male\",\n" +
                "            \"Female\"\n" +
                "          ],\n" +
                "        \"answerType\" : 0,\n" +
                "        \"serialNumber\" : \"3\",\n" +
                "        \"text\" : \"What is your gender?\"\n" +
                "      }\n" +
                "    ]\n" +
                "}";
        String alfredJson2 = "{ \"creator\" : \"alfred\",\n" +
                "  \"name\" : \"Alfred's Survey2\",\n" +
                "  \"questions\" : [ { \"answerType\" : 2,\n" +
                "        \"image\" : \"http://2.375585449099093977.surveyinsight.appspot.com/api/image?question=ag9zfnN1cnZleWluc2lnaHRyHAsSBlN1cnZleRiZxRAMCxIIUXVlc3Rpb24YAQw\",\n" +
                "        \"serialNumber\" : \"1\",\n" +
                "        \"text\" : \"How is the weather right now?\"\n" +
                "      },\n" +
                "      { \"answerRange\" : { \"high\" : 100.0,\n" +
                "            \"low\" : 18.0,\n" +
                "            \"step\" : 1.0\n" +
                "          },\n" +
                "        \"answerType\" : 1,\n" +
                "        \"serialNumber\" : \"2\",\n" +
                "        \"text\" : \"What is the temperature?\"\n" +
                "      },\n" +
                "      { \"answerChoices\" : [ \"Yes\",\n" +
                "            \"No\"\n" +
                "          ],\n" +
                "        \"answerType\" : 0,\n" +
                "        \"serialNumber\" : \"3\",\n" +
                "        \"text\" : \"Are you single?\"\n" +
                "      }\n" +
                "    ]\n" +
                "}";
        createSurvey(emf, out, alfredJson1);
        createSurvey(emf, out, alfredJson2);


        //TODO-Alfred This code throws an error because I manually changed the key something is wrong with the encoding
        //TODO-Alfred My guess is that you set mId to null in the survey and the Datastore generates the value automatically
        // ------- mandar.json ----------
        out.println("Started reading from mandar.json");
        System.out.println("Started reading from mandar.json");
        //Read in alfred.json directly from code
        String mandarJson =
                "{ \"creator\" : \"mandar\",\n" +
                        "  \"name\" : \"Mandar's Survey\",\n" +
                        "  \"questions\" : [ { \"answerType\" : 2,\n" +
                        "        \"image\" : \"http://2.375585449099093977.surveyinsight.appspot.com/api/image?question=ag8zfnN1cnZleWluc2lnaHRyHAsSBlN1cnZleRiZxRAMCxIIUXVlc3Rpb24YAQw\",\n" +
                        "        \"serialNumber\" : \"1\",\n" +
                        "        \"text\" : \"What can we do to improve?\"\n" +
                        "      },\n" +
                        "      { \"answerRange\" : { \"high\" : 100.0,\n" +
                        "            \"low\" : 18.0,\n" +
                        "            \"step\" : 1.0\n" +
                        "          },\n" +
                        "        \"answerType\" : 1,\n" +
                        "        \"serialNumber\" : \"2\",\n" +
                        "        \"text\" : \"How many times a week do you exercise?\"\n" +
                        "      },\n" +
                        "      { \"answerChoices\" : [ \"Dog\",\n" +
                        "            \"Cat\"\n" +
                        "          ],\n" +
                        "        \"answerType\" : 0,\n" +
                        "        \"precondition\" : { \"answerIndex\" : -1,\n" +
                        "            \"answerRangeHigh\" : 100.0,\n" +
                        "            \"answerRangeLow\" : 0.0,\n" +
                        "            \"answerText\" : \"magic\",\n" +
                        "            \"question\" : \"ag8zfnN1cnZleWluc2lnaHRyHAsSBlN1cnZleRiZxRAMCxIIUXVlc3Rpb24YAQw\",\n" +
                        "            \"questionSerialNumber\" : \"1\"\n" +
                        "          },\n" +
                        "        \"serialNumber\" : \"3\",\n" +
                        "        \"text\" : \"Are you a dog person or a cat person?\"\n" +
                        "      }\n" +
                        "    ]\n" +
                        "}";
        createSurvey(emf, out, mandarJson);
        // ----------------------------

        out.println("Finish read json files<br />");

        //File f = new File(filePath);
        //InputStream is = new FileInputStream(f);

        //System.out.println(convertStreamToString(is));
        //out.println("Start reading json<br />");
        //survey.readFromJson(em, jsonReader);
        //out.println("Finished reading data from json<br />");
        //out.println(survey.getQuestions() + "<br />");
        //out.println("Finished getting questions<br />");


    }

    public static void retrieveItems(EntityManagerFactory emf, PrintWriter out) {

        //emf = Persistence.createEntityManagerFactory("transactions-optional");
        out.println("Try to Create Entity Manager<br />");
        // Perform a retrieve of the Inventory and detach it (by closing the EM)
        EntityManager em = emf.createEntityManager();
        //EntityTransaction tx = em.getTransaction();
        try
        {
            // Do a find() of the Survey
            System.out.println("Executing find() on Surveys");
            TypedQuery<Survey> surveyquery = em.createQuery(
                    "SELECT s FROM Survey s",
                    Survey.class);
            List<Survey> surveys = surveyquery.getResultList();
            out.println("Printing names of surveys<br />");
            for (Survey item : surveys) {
                out.println(item.getName() + "<br />");
            }
            out.println("End of names of surveys<br />");
        }
        catch (Exception e)
        {
            //NucleusLogger.GENERAL.error(">> Exception performing find() on data", e);
            System.err.println("Error performing find() on data : " + e.getMessage());
            return;
        }
        finally
        {
            em.close(); // This will detach all current managed objects
        }
    }


    public static void deleteItems(EntityManagerFactory emf, PrintWriter out) {

        EntityManager em = emf.createEntityManager();
        //EntityTransaction tx = em.getTransaction();
        int deleted;
        try
        {
            // Do a find() of the Survey
            System.out.println("Starting delete survey query");
            out.println("Starting delete survey query<br />");
            Query q = em.createQuery("DELETE FROM Survey s");
            deleted = q.executeUpdate();
            out.println("Deleted value is " + deleted + "<br />");

        }
        catch (Exception e)
        {
            //NucleusLogger.GENERAL.error(">> Exception performing find() on data", e);
            System.err.println("Error performing find() on data : " + e.getMessage());
            return;
        }
        finally
        {
            em.close(); // This will detach all current managed objects
        }

        em = emf.createEntityManager();
        //EntityTransaction tx = em.getTransaction();
        try
        {
            // Do a find() of the Survey
            System.out.println("Starting delete user query");
            out.println("Starting delete user query<br />");
            Query q = em.createQuery("DELETE FROM User u");
            deleted = q.executeUpdate();
            out.println("Deleted value is " + deleted + "<br />");

        }
        catch (Exception e)
        {
            //NucleusLogger.GENERAL.error(">> Exception performing find() on data", e);
            System.err.println("Error performing find() on data : " + e.getMessage());
            return;
        }
        finally
        {
            em.close(); // This will detach all current managed objects
        }


    }

    public static void createUsers(EntityManagerFactory emf, PrintWriter out) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        User user = new User();
        user.setEmail("alfred@mobinsight.com");
        tx.begin();
        em.persist(user);
        tx.commit();

        user = new User();
        user.setEmail("mandar@mobinsight.com");
        tx.begin();
        em.persist(user);
        tx.commit();

        user = new User();
        user.setEmail("karan@mobinsight.com");
        tx.begin();
        em.persist(user);
        tx.commit();

        user = new User();
        user.setEmail("sood@mobinsight.com");
        tx.begin();
        em.persist(user);
        tx.commit();

        user = new User();
        user.setEmail("david@mobinsight.com");
        tx.begin();
        em.persist(user);
        tx.commit();

        user = new User();
        user.setEmail("berkeley@mobinsight.com");
        tx.begin();
        em.persist(user);
        tx.commit();

        user = new User();
        user.setEmail("ucla@mobinsight.com");
        tx.begin();
        em.persist(user);
        tx.commit();

        user = new User();
        user.setEmail("amazon@mobinsight.com");
        tx.begin();
        em.persist(user);
        tx.commit();

        em.close();
    }



}