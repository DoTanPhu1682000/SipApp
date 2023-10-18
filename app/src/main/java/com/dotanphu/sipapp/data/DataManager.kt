package com.dotanphu.sipapp.data

import com.dotanphu.sipapp.data.api.AppApiHelper
import com.dotanphu.sipapp.data.prefs.AppPreferenceHelper
import javax.inject.Inject

class DataManager @Inject constructor() {
    @Inject
    lateinit var mAppApiHelper: AppApiHelper

    @Inject
    lateinit var mAppPreferenceHelper: AppPreferenceHelper
}