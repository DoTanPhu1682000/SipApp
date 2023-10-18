package com.dotanphu.sipapp.di

import android.content.Context
import com.dotanphu.sipapp.data.api.AppApiHelper
import com.dotanphu.sipapp.data.prefs.AppPreferenceHelper
import com.dotanphu.sipapp.utils.ErrorHandlerUtil
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideAppPreferenceHelper(@ApplicationContext context: Context): AppPreferenceHelper {
        return AppPreferenceHelper(context)
    }

    @Singleton
    @Provides
    fun provideAppApiHelper(appPreferenceHelper: AppPreferenceHelper): AppApiHelper {
        return AppApiHelper(appPreferenceHelper)
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    fun provideErrorHandlerUtil(@ApplicationContext context: Context): ErrorHandlerUtil {
        return ErrorHandlerUtil(context)
    }
}