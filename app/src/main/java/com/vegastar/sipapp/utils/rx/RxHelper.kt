package com.vegastar.sipapp.utils.rx

import android.content.Context
import io.reactivex.rxjava3.core.SingleTransformer
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

interface RxHelper {
    fun <T> handleApiToListObject(type: Class<T>): SingleTransformer<JSONObject, List<T>>

    fun <T> toListObject(keyJSONArray: String, type: Class<T>): SingleTransformer<JSONObject, List<T>>

    fun <T : Any> toObject(keyJSONObject: String, type: Class<T>): SingleTransformer<JSONObject, T>

    fun <T> toListObject(type: Class<T>): SingleTransformer<JSONArray, List<T>>
}