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
            JSONArray jsonArray = new JSONArray(raw);
            String[] data = new String[jsonArray.length()];
            for(int i = 0; i < jsonArray.length(); i++) {
                // Create a new StringBuilder each time so it is clear
                JSONObject obj = jsonArray.getJSONObject(i);
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
