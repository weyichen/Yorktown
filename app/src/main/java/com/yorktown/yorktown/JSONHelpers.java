package com.yorktown.yorktown;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public static List<JSONObject> getJSONObjectList(JSONArray jsonArray) {
        ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();

        try {
            for(int i=0; i<jsonArray.length(); i++) {
                jsonObjects.add(i, jsonArray.getJSONObject(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObjects;
    }

    public static JSONArray getJSONArrayFromList(List<JSONObject> list) {
        JSONArray jsonArray = new JSONArray();

        for(JSONObject jsonObject: list) {
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    public static int getInt(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;
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

    public static void putInArray(JSONArray jsonArray, int index, Object value) {
        try {
            jsonArray.put(index, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
