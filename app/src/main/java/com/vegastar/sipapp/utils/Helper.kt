package com.vegastar.sipapp.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.widget.R
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Random

object Helper {
    fun getTextNotNull(input: String): String {
        return getTextNotNull(input, "")
    }

    fun getTextNotNull(input: String, defaultString: String?): String {
        return if (!TextUtils.isEmpty(input) && !input.equals("null", ignoreCase = true)) input else defaultString!!
    }

    fun isTablet(context: Context): Boolean {
        return context.resources.getBoolean(R.bool.isTablet)
    }

    fun setTabletLimitMatchWidthLayout(context: Context, viewGroup: ViewGroup, width: Int) {
        if (!isTablet(context)) return
        val layoutParams = viewGroup.layoutParams
        layoutParams.width = width
        //layoutParams.height = WRAP_CONTENT;
        viewGroup.layoutParams = layoutParams
    }

    fun setTabletLimitMatchWidthLayout(activity: Activity, viewGroup: ViewGroup) {
        if (!isTablet(activity)) return
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        var width = size.x
        width = width / 3 * 2
        setTabletLimitMatchWidthLayout(activity, viewGroup, width)
    }

    fun formatInputNumber0(data: Double): String {
        val symbols = DecimalFormatSymbols()
        symbols.decimalSeparator = '.'
        val formatter: NumberFormat = DecimalFormat("###", symbols)
        return formatter.format(data)
    }

    fun formatInputNumber1(data: Double): String {
        val symbols = DecimalFormatSymbols()
        symbols.decimalSeparator = '.'
        val formatter: NumberFormat = DecimalFormat("###.#", symbols)
        return formatter.format(data)
    }

    fun round(value: Double, places: Int): Double {
        var places = places
        if (places < 0) places = 1
        var bd = BigDecimal.valueOf(value)
        bd = bd.setScale(places, RoundingMode.HALF_UP)
        return bd.toDouble()
    }

    fun smoothSnapToPosition(context: Context?, linearLayoutManager: LinearLayoutManager, position: Int) {
        val smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }

            override fun getHorizontalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = position
        linearLayoutManager.startSmoothScroll(smoothScroller)
    }

    fun formatNumber(data: Double): String {
        val symbols = DecimalFormatSymbols()
        symbols.decimalSeparator = ','
        symbols.groupingSeparator = '.'
        val formatter: NumberFormat = DecimalFormat("###,###,###.###", symbols)
        return formatter.format(data)
    }

    fun generateRandomPin(): String {
        return generateRandomChars("1234567890", 6)
    }

    fun generateRandomChars(candidateChars: String, length: Int): String {
        val sb = StringBuilder()
        val random = Random()
        for (i in 0 until length) {
            sb.append(candidateChars[random.nextInt(candidateChars.length)]
            )
        }
        return sb.toString()
    }

    fun setMaterialButtonLoading(activity: Activity?, materialButton: MaterialButton, text: String?, isLoading: Boolean) {
        setMaterialButtonLoading(activity, materialButton, text, isLoading, R.color.white, null)
    }

    fun setMaterialButtonLoading(activity: Activity?, materialButton: MaterialButton, text: String?, isLoading: Boolean, color: Int) {
        setMaterialButtonLoading(activity, materialButton, text, isLoading, color, null)
    }

    fun setMaterialButtonLoading(activity: Activity?, materialButton: MaterialButton, text: String?, isLoading: Boolean, color: Int, defaultDrawable: Drawable?) {
        if (isLoading) {
            materialButton.text = ""
            val spec = CircularProgressIndicatorSpec(activity!!, null)
            spec.indicatorColors = intArrayOf(ContextCompat.getColor(activity, color))
            val drawable = IndeterminateDrawable.createCircularDrawable(activity, spec)
            materialButton.icon = drawable
        } else {
            materialButton.text = text
            materialButton.icon = defaultDrawable
        }
    }

    fun randInt(min: Int, max: Int): Int {
        return Random().nextInt(max - min + 1) + min
    }
}