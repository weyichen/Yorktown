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

    public static JSONObject getJSONObject(JSONArray jsonArray, int position) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject = jsonArray.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
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

    public static String getString(JSONObject jsonObject, String key) {
        String string = null;

        try {
            string = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return string;
    }

    public static void put(JSONObject jsonObject, String key, Object value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
