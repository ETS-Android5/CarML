/* This class parses JSON data and returns it as a string array.
 * Author: Sean Coll
 * Date Created: 19/02/22
 * Last Modified: 19/02/22
 */
package ie.tudublin.carml;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {

    public JSONParser() {

    }

    public String[] parseData (String raw, String name) {
        try {
            // Create a JSONArray using the raw JSON string
            JSONArray jsonArray = new JSONArray(raw);
            // Create a String array with a length equal to the JSONArray's
            String[] data = new String[jsonArray.length()];
            for(int i = 0; i < jsonArray.length(); i++) {
                // Get the JSONObject
                JSONObject obj = jsonArray.getJSONObject(i);
                // Store the string value the object has in the string array
                data[i] = obj.getString(name);
            }
            return data;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
