package com.utils;


import android.os.Bundle;

import com.custom.ListOfSomething;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ParseUtil {

    public static String getStringJson(String key, JSONObject joData) {
        return getStringJson(key, joData, null);
    }

    public static String getStringJson(String key, JSONObject joData, String defValue) {
        try {
            return joData != null && joData.has(key) ? joData.getString(key) : defValue;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public static double getDoubleJson(String key, JSONObject joData) {
        try {
            if (joData != null && joData.has(key))
                return joData.getDouble(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Integer getIntJson(String key, JSONObject joData) {
        return getIntJson(key, joData, 0);
    }

    public static Integer getIntJson(String key, JSONObject joData, int defaultValue) {
        try {
            return joData != null && joData.has(key) ? joData.getInt(key) : defaultValue;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static Long getLongJson(String key, JSONObject joData) {
        try {
            return joData != null && joData.has(key) ? joData.getLong(key) : 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static boolean getBooleanJson(String key, JSONObject joData) {
        try {
            return joData.has(key) && joData.getBoolean(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static JSONObject getJSONObject(String key, JSONObject joData) {
        try {
            if (joData != null && joData.has(key))
                return joData.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getJSONArray(String key, JSONObject joData) {
        try {
            if (joData != null && joData.has(key))
                return joData.getJSONArray(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject createJSONObject(String jsonString) {
        try {
            if (jsonString != null)
                return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray createJSONArray(String jsonString) {
        try {
            if (jsonString != null)
                return new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T jsonToObject(String json, Class<T> classOfT) {
        try {
            if (json != null)
                return new Gson().fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            LogUtil.e(e.getMessage());
        }
        return null;
    }

    public static <T> List<T> jsonToListObject(String json, Class<T> classOfT) {
        try {
            if (json != null)
                return new Gson().fromJson(json, new ListOfSomething<>(classOfT));
        } catch (JsonSyntaxException e) {
            LogUtil.e(e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getSerializableList(Bundle bundle, String key) {
        if (bundle != null && bundle.containsKey(key)) {
            try {
                return (List<T>) bundle.getSerializable(key);
            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            }

        }
        return null;
    }

}