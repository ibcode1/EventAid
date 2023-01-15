package com.ib.eventaid.common.data.di

import com.ib.eventaid.common.data.api.ApiConstants
import com.ib.eventaid.common.data.api.EventAidApi
import com.ib.eventaid.common.data.api.PerformerAidApi
import com.ib.eventaid.common.data.api.interceptor.AuthenticationInterceptor
import com.ib.eventaid.common.data.api.interceptor.LoggingInterceptor
import com.ib.eventaid.common.data.api.interceptor.NetworkStatusInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideApi(builder: Retrofit.Builder): EventAidApi {
        return builder
            .build()
            .create(EventAidApi::class.java)
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
    }

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkStatusInterceptor: NetworkStatusInterceptor,
        //authenticationInterceptor: AuthenticationInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkStatusInterceptor)
            //.addInterceptor(authenticationInterceptor)
            /*.addInterceptor {
                it.proceed(
                    it.request().newBuilder()
                        .build()
                )
            }*/
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun provideHttpLoggingInterceptor(loggingInterceptor: LoggingInterceptor): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(loggingInterceptor)

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return interceptor
    }

    @Provides
    @Singleton
    fun providePerformerApi(builder: Retrofit.Builder):PerformerAidApi{
        return builder
            .build()
            .create(PerformerAidApi::class.java)
    }
}