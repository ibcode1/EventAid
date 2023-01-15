package com.ib.eventaid.common.data.api.interceptor

import com.ib.eventaid.common.data.api.ConnectionManager
import com.ib.eventaid.common.domain.NetworkUnavailableException
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkStatusInterceptor @Inject constructor(private val connectionManager: ConnectionManager):Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (connectionManager.isConnected){
            chain.proceed(chain.request())
        }else{
            throw NetworkUnavailableException()
        }
    }
}