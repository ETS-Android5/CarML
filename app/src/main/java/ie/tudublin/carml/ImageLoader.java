/* This class provides the ImageLoader object that will get the URL for an image and return it.
 * Author: Sean Coll
 * Date Created: 21/1/22
 * Last Modified: 23/1/22
 *
 * Concurrency code adapted from:
 * https://stackoverflow.com/questions/64724824/how-to-implement-a-background-thread-using-java-util-concurrent-package
 * Image loading code adapted from:
 * https://stackoverflow.com/questions/5776851/load-image-from-url
 */
package ie.tudublin.carml;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ImageLoader {

    Bitmap image;
    String url;
    ImageView imgView;

    public ImageLoader(ImageView iV) {
        this.image = null;
        this.imgView = iV;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // Manages threads and their execution
    ExecutorService execServ = Executors.newFixedThreadPool(2);

    public Bitmap getBitmapImage(String query) {

        // Thread to get an image URL
        Runnable bgGetUrl = new Runnable() {
            @Override
            public void run() {
                Log.i("CarML bgGetUrl", "Starting bgGetUrl");
                String url = getImageUrl(query);
                setUrl(url);
                Log.i("CarML bgGetUrl", "Finished bgGetUrl");
            }
        };

        try {
            // Execute the thread
            Future<?> futureGetUrl = execServ.submit(bgGetUrl);
            // Wait for the thread's completion
            futureGetUrl.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Runnable bgGetImage = new Runnable() {
            @Override
            public void run() {
                Log.i("CarML bgGetImage", "Starting bgGetImage");
                Bitmap image;
                try {
                    String url = getUrl();
                    InputStream inStream = new java.net.URL(url).openStream();
                    Log.i("CarML URL to Stream", getUrl());
                    image = BitmapFactory.decodeStream(inStream);
                    setImage(image);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("CarML bgGetImage", "Finished bgGetImage");
            }
        };

        try {
            // Execute the thread
            Future<?> futureGetImage = execServ.submit(bgGetImage);
            // Wait for the thread's completion
            futureGetImage.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return this.image;
    }

    public String getImageUrl(String query) {
        // query is manufacturer,model,year
        // Split the query to it can be rearranged
        String[] split = query.split(",");
        // Build the search query with the details used
        String searchQuery = split[2] + "+" + split[0] + "+" + split[1];
        // Replace any spaces with +
        searchQuery = searchQuery.replace(" ", "+");
        try {
            // For the Google Images URL
            String connectUrl = "https://www.google.com/search?tbm=isch&q=" + searchQuery;
            Log.i("CarML URL", connectUrl);
            String userAgent = "Mozilla/5.0 (Linux; Android 9; POT-LX1A) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.87 Mobile Safari/537.36";
            // Get the HTML document using the user agent with no maximum document size and the
            // referrer https://www.google.com
            Document doc = Jsoup.connect(connectUrl).userAgent(userAgent).maxBodySize(0).referrer("https://www.google.com").get();
            // Get the second element with the attribute data-src
            // The first element is not an image
            Element ele = doc.select("[data-src]").get(1);
            // Get the image URL from the element
            String sourceUrl = ele.attr("data-src");
            Log.i("CarML Source URL", "Source URL " + sourceUrl + " ?");
            // If the URL is not empty
            if (sourceUrl.equals("")) {
                Log.i("CarML Source URL" ,"URL is empty");
            }
            else {
                Log.i("CarML Source URL", sourceUrl + " ?");
                return sourceUrl;
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        // Return a string the will lead to the default image being displayed
        return "https://forums.galaxy-of-heroes.starwars.ea.com/themes/swgh/design/images/logo-starwars-galaxy-of-heroes.png";
    }

}

