package com.ib.eventaid.common.data.api.interceptor

import com.ib.logging.Logger
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class LoggingInterceptor @Inject constructor():HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Logger.i(message)
    }
}