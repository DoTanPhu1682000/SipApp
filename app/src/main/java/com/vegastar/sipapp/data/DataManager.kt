package com.vegastar.sipapp.data

import com.vegastar.sipapp.data.api.AppApiHelper
import com.vegastar.sipapp.data.prefs.AppPreferenceHelper
import javax.inject.Inject

class DataManager @Inject constructor() {
    @Inject
    lateinit var mApiHelper: AppApiHelper

    @Inject
    lateinit var mPreferenceHelper: AppPreferenceHelper
}