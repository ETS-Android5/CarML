/* This class provides access to the database. It can be used to
* - Retrieve additional details about a car.
* - Retrieve the lists of manufacturers and models to populate the drop down menus.
* Author: Seán Coll
* Date Created: 7/02/22
* Last Modified: 20/02/22
*/

package ie.tudublin.carml;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class DatabaseAccess {

    // Create execServ to manage the thread
    ExecutorService execServ = Executors.newSingleThreadExecutor();

    String DBURL = "http://192.168.1.6/CarML/";
    String data = " ";

    public String runThread(String option, String user_query) {

        // Thread to get data from the database
        Runnable bgGetData = new Runnable() {
            @Override
            public void run() {
                switch (option) {
                    case "details": {
                        data = getCarDetails(user_query);
                        break;
                    }
                    case "manufacturers": {
                        data = getManufacturers();
                        break;
                    }
                    case "models": {
                        data = getModels(user_query);
                        break;
                    }
                    case "years": {
                        data = getYears(user_query);
                        break;
                    }
                    case "ping": {
                        data = pingServer();
                        break;
                    }
                    case "encodedVals": {
                        data = getEncodedVals(user_query);
                        break;
                    }
                    case "addPrediction": {
                        data = addPrediction(user_query);
                        break;
                    }
                }
            }
        };
        try {
            // Execute the thread
            Future<?> futureGetData = execServ.submit(bgGetData);
            // Wait for the thread's completion
            futureGetData.get();
            return data;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return "ERROR. Unable to run thread";
    }

    public String getCarDetails(String query) {
        String[] car = query.split(",");
        try {
            URL url = new URL(DBURL + "getCarDetails.php");
            return runQuery(url, car);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "ERROR. Unable to retrieve details";
    }

    public String getManufacturers() {
        // Nothing needs to be sent
        String[] car = {" ", " ", " "};
        try {
            URL url = new URL(DBURL + "getManufacturers.php");
            return runQuery(url, car);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i("CarML DBA", "Unable to retrieve manufacturers");
        }
        return "ERROR. Unable to retrieve manufacturers";
    }

    public String getModels(String query) {
        String[] car = query.split(",");
        try {
            URL url = new URL(DBURL + "getModels.php");
            return runQuery(url, car);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "ERROR. Unable to retrieve models";
    }

    public String getYears(String query) {
        String[] car = query.split(",");
        try {
            URL url = new URL(DBURL + "getYears.php");
            return runQuery(url, car);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "ERROR. Unable to retrieve years";
    }

    public String pingServer() {
        String[] car = {",",",",","};
        try {
            URL url = new URL(DBURL + "pingServer.php");
            return runQuery(url, car);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "ERROR. Server unavailable.";
    }

    public String getEncodedVals(String query) {
        String[] car = query.split(",");
        try {
            URL url = new URL(DBURL + "getEncodedValues.php");
            return runQuery(url, car);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "ERROR. Unable to retrieve encoded values";
    }

    public String addPrediction(String query) {
        String[] car = query.split(",");
        try {
            URL url = new URL(DBURL + "addPrediction.php");
            return runQuery(url, car);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "ERROR. Unable to retrieve encoded values";
    }

    public String runQuery(URL url, String[] car) {
        HttpURLConnection httpURLConnection = null;
        try {
            // Create a URL connection
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // Specify the type of request
            httpURLConnection.setRequestMethod("POST");
            // Allow output (Sending data from client)
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(3000);
            // Accept the output through the OutputStream
            OutputStream OS = httpURLConnection.getOutputStream();
            if(OS == null) {
                return "ERROR. Unable to getOutputStream";
            }
            // Buffered Writer used to apply parameters
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, StandardCharsets.UTF_8));
            // Encode the data to be sent
            String data = encodeData(car);
            // Write the data to the BufferedWriter
            bufferedWriter.write(data);
            // Flush the BufferedWriter
            bufferedWriter.flush();
            // Close the BufferedWriter
            bufferedWriter.close();
            // Close the OutputStream
            OS.close();
            // Create InputStream to receive data from server
            InputStream IS = httpURLConnection.getInputStream();
            // Capture the data return from server
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, StandardCharsets.ISO_8859_1));
            // Create StringBuilder to format the data
            StringBuilder sb = new StringBuilder();
            String json;
            // Append each line of data to a single string which will form the JSON string
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json).append("\n");
            }
            // Close the InputStream
            IS.close();
            Log.i("CarML DBA", "Data retrieved: " + sb.toString().trim());
            // Return the JSON string
            return sb.toString().trim();
        } catch (Exception ioe) {
//            ioe.printStackTrace();
            httpURLConnection.disconnect();
            Log.i("CarML DBA Error", "Error: " + ioe.getMessage());
            return "ERROR. Server unavailable.";
        }
    }

    public String encodeData(String[] details) {
        String encData = "";
        if(details.length == 3) {
            try {
                encData = URLEncoder.encode("manufacturer","UTF-8")+"="+
                        URLEncoder.encode(details[0], "UTF-8")+"&"+
                        URLEncoder.encode("model","UTF-8")+"="+
                        URLEncoder.encode(details[1],"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+
                        URLEncoder.encode(details[2], "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.i("CarML DBA Encoding", e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            try {
                encData = URLEncoder.encode("manufacturer","UTF-8")+"="+
                        URLEncoder.encode(details[0], "UTF-8")+"&"+
                        URLEncoder.encode("model","UTF-8")+"="+
                        URLEncoder.encode(details[1],"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+
                        URLEncoder.encode(details[2], "UTF-8")+"&"+
                        URLEncoder.encode("prediction","UTF-8")+"="+
                        URLEncoder.encode(details[3],"UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.i("CarML DBA Encoding", e.getMessage());
                e.printStackTrace();
            }
        }
        return encData;
    }
}
