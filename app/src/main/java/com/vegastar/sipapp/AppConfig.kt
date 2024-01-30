package com.vegastar.sipapp

object AppConfig {
    const val IS_PRODUCTION = false

    const val REMOVE_USER_DATA_WHEN_APP_START = false

    const val CHECK_PHONE_NUMBER = "0392719775" //TEST: điền sẵn số điện thoại vào CheckPhoneFragment, PRODUCTION xóa trống =""
    const val ADDRESS_SIP = "10.10.216.202"

    /*----------------------------------[DEFINE]--------------------------------------------------*/
    var BASE_URL: String? = null

    init {
        // BASE_URL = if (IS_PRODUCTION) "https://api.365medihome.com.vn" else "https://sandboxapi.365medihome.com.vn"
        // BASE_URL = if (IS_PRODUCTION) "https://api.365medihome.com.vn" else "http://192.168.14.209:8080/admin/api/api"
        BASE_URL = if (IS_PRODUCTION) "https://api.365medihome.com.vn" else "http://10.10.216.202/admin/api/api"
    }

    /*----------------------------------[CONFIG]--------------------------------------------------*/
    const val TAG = "APP"
    const val IS_DEBUG = true //BuildConfig.DEBUG;
    const val BACK_TO_EXIT_TIME = 2 * 1000L //Thời gian chờ thoát ứng dụng (Milliseconds)
    const val REQUEST_TIMEOUT = 12

    const val OTP_RESEND_WAIT_TIME = 120 * 1000 //Thời gian chờ gửi lại OTP (Milliseconds)
    const val MIN_CLICK_INTERVAL: Long = 700 //Thời gian tối thiểu giữa 2 lần nhấn Click

    /*----------------------------------[SHARE PREFERENCE]----------------------------------------*/
    const val PREFERENCE_NAME = "app" //Tên SharePreference (Không nên thay đổi)

    /*----------------------------------[IMAGE COMPRESS]------------------------------------------*/ //Thông số nén ảnh
    const val COMPRESS_IMAGE_MAX_WIDTH = 1200 //Chiều rộng tối đa (px)
    const val COMPRESS_IMAGE_MAX_HEIGHT = 1100 //Chiều cao tối đa (px)
    const val COMPRESS_IMAGE_QUALITY = 80 //Tỉ lệ ảnh sau khi nén (giảm 20%)

    /*----------------------------------[OTHER]---------------------------------------------------*/
    const val DEFAULT_ITEMS_PER_PAGE = 20
}