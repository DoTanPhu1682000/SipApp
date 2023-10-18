package com.dotanphu.sipapp.data

import com.dotanphu.sipapp.data.api.AppApiHelper
import com.dotanphu.sipapp.data.prefs.AppPreferenceHelper
import javax.inject.Inject

class DataManager @Inject constructor() {
    @Inject
    lateinit var mApiHelper: AppApiHelper

    @Inject
    lateinit var mPreferenceHelper: AppPreferenceHelper
}