package com.yorktown.yorktown;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Daniel on 2/3/2015.
 */
public class JSONHelpers {

    public static JSONArray newJSONArray(String arrayString) {
        JSONArray jsonArray = new JSONArray();

        try {
            jsonArray = new JSONArray(arrayString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    public static JSONObject[] getJSONObjectArray(JSONArray jsonArray) {
        JSONObject[] jsonObjects = new JSONObject[jsonArray.length()];

        try {
            for(int i=0; i<jsonArray.length(); i++) {
                jsonObjects[i] = jsonArray.getJSONObject(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObjects;
    }
}
