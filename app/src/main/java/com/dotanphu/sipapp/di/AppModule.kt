package com.dotanphu.sipapp.di

import android.content.Context
import com.dotanphu.sipapp.utils.ErrorHandlerUtil
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    fun provideErrorHandlerUtil(@ApplicationContext context: Context): ErrorHandlerUtil {
        return ErrorHandlerUtil(context)
    }
}