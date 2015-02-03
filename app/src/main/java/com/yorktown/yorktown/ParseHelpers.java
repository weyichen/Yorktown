package com.yorktown.yorktown;

import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Daniel on 1/27/2015.
 */
public class ParseHelpers {

    public static String getString(String key, ParseObject pO) {
        if (pO.has(key)) {
            return pO.getString(key);
        } else {
            return "";
        }
    }

    public static JSONObject getJSONObject(String key, ParseObject pO) {
        if (pO.has(key)) {
            return pO.getJSONObject(key);
        } else {
            return null;
        }
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
