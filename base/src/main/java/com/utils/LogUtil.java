package com.utils;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

public class LogUtil {
    private static String appTag = "APP";

    public static void init(String tag, final boolean isLoggable) {
        init(tag, isLoggable, 1);
    }

    public static void init(String tag, final boolean isLoggable, int methodOffset) {
        appTag = tag;
        Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)
                .methodCount(1)
                .methodOffset(methodOffset)
                .tag(tag)
                .build()) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return isLoggable;
            }
        });
    }

    public static void d(String message, @Nullable Object... args) {
        Logger.d(message != null ? message : "null", args);
    }

    public static void d(@Nullable Object object) {
        Logger.d(object);
    }

    public static void e(String message, @Nullable Object... args) {
        Logger.e(null, message != null ? message : "null", args);
    }

    public static void e(@Nullable Throwable throwable, String message, @Nullable Object... args) {
        Logger.e(throwable, message != null ? message : "null", args);
    }

    public static void i(String message, @Nullable Object... args) {
        Logger.i(message != null ? message : "null", args);
    }

    public static void v(String message, @Nullable Object... args) {
        Logger.v(message != null ? message : "null", args);
    }

    public static void w(String message, @Nullable Object... args) {
        Logger.w(message != null ? message : "null", args);
    }

    /**
     * Tip: Use this for exceptional situations to log
     * ie: Unexpected errors etc
     */
    public static void wtf(String message, @Nullable Object... args) {
        Logger.wtf(message != null ? message : "null", args);
    }

    /**
     * Formats the given json content and print it
     */
    public static void json(@Nullable String json) {
        Logger.json(json);
    }

    /**
     * Formats the given xml content and print it
     */
    public static void xml(@Nullable String xml) {
        Logger.xml(xml);
    }

    public static void clearLogAdapters() {
        Logger.clearLogAdapters();
    }


    public static void info(String message) {
        Log.i(appTag, message);
    }

    public static void error(String message) {
        Log.e(appTag, message);
    }

    public static void object(Object object) {
        LogUtil.clearLogAdapters();
        LogUtil.init(appTag, true, 2);
        LogUtil.wtf(object != null ? new Gson().toJson(object) : "null");
        LogUtil.clearLogAdapters();
        LogUtil.init(appTag, true);
    }
}
