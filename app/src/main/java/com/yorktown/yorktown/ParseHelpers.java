package com.yorktown.yorktown;

import com.parse.ParseException;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONObject;



/**
 * Created by Daniel on 1/27/2015.
 */
public class ParseHelpers {

    public static String getString(ParseObject pO, String key) {
        if (pO.has(key)) {
            return pO.getString(key);
        } else {
            return "";
        }
    }

    public static JSONArray getJSONArray(ParseObject pO, String key) {
        if (pO.has(key)) {
            return pO.getJSONArray(key);
        } else {
            return null;
        }
    }

    public static JSONObject getJSONObject(ParseObject pO, String key) {
        if (pO.has(key)) {
            return pO.getJSONObject(key);
        } else {
            return null;
        }
    }

    public static void pin(ParseObject pO, String name) {
        try {
            pO.pin(name);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
