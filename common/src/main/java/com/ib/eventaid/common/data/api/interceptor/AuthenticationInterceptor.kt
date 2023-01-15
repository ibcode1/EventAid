package com.ib.eventaid.common.data.api.interceptor

import com.ib.eventaid.common.data.api.ApiConstants.AUTHORIZATION_ENDPOINT
import com.ib.eventaid.common.data.api.ApiConstants.AUTH_ENDPOINT
import com.ib.eventaid.common.data.api.ApiConstants.CLIENT
import com.ib.eventaid.common.data.api.ApiConstants.SCOPES
import com.ib.eventaid.common.data.api.ApiConstants.SECRET
import com.ib.eventaid.common.data.api.ApiParameters.CLIENT_ID
import com.ib.eventaid.common.data.api.ApiParameters.CLIENT_SECRET
import com.ib.eventaid.common.data.api.ApiParameters.CODE
import com.ib.eventaid.common.data.api.ApiParameters.CODE_KEY
import com.ib.eventaid.common.data.api.ApiParameters.GRANT_TYPE
import com.ib.eventaid.common.data.api.ApiParameters.GRANT_TYPE_KEY
import com.ib.eventaid.common.data.api.ApiParameters.REDIRECT_URI
import com.ib.eventaid.common.data.api.ApiParameters.SCOPE
import com.ib.eventaid.common.data.api.ApiParameters.URI
import com.ib.eventaid.common.data.api.model.ApiToken
import com.ib.eventaid.common.data.preferences.Preferences
import com.squareup.moshi.Moshi
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.time.Instant
import javax.inject.Inject


class AuthenticationInterceptor @Inject constructor(
    private val preferences: Preferences
) : Interceptor {

    companion object {
        const val UNAUTHORIZED = 401
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = preferences.getToken()
        val tokenExpirationTime = Instant.ofEpochSecond(preferences.getTokenExpirationTime())
        val request = chain.request()

        val interceptedRequest: Request

        if (tokenExpirationTime.isAfter(Instant.now())) {
            //token is still valid, so proceed with the request
            interceptedRequest = chain.createAuthenticatedRequest(token)
        } else {
            //if token is expired.gotta refresh it
            val tokenRefreshResponse = chain.refreshToken()

            interceptedRequest = if (tokenRefreshResponse.isSuccessful) {
                val newToken = mapToken(tokenRefreshResponse)

                if (newToken.isValid()) {
                    storeNewToken(newToken)
                    chain.createAuthenticatedRequest(newToken.accessToken!!)
                } else {
                    request
                }
            } else {
                request
            }
        }
        return chain.proceedDeletingTokenIfUnauthorized(interceptedRequest)
    }

    private fun Interceptor.Chain.createAuthenticatedRequest(token: String): Request {
        return request()
            .newBuilder()
            .url(AUTHORIZATION_ENDPOINT)
            .build()
    }

//    private fun Interceptor.Chain.token():Response{
//        val uri = request()
//            .url
//            .newBuilder(AUTHORIZATION_ENDPOINT)!!
//            .addEncodedQueryParameter("scope", SCOPES)
//            .addEncodedQueryParameter(CLIENT_ID, CLIENT)
//            .build()
//
//    }


    private fun Interceptor.Chain.refreshToken(): Response {
        val url = request()
            .url
            .newBuilder(AUTH_ENDPOINT)!!
            .addEncodedQueryParameter(CLIENT_ID, CLIENT)
            //.addEncodedQueryParameter(SCOPE, SCOPES)
            .addEncodedQueryParameter(GRANT_TYPE_KEY, GRANT_TYPE)
            //.addEncodedQueryParameter(REDIRECT_URI,URI)
            //.addEncodedQueryParameter(CODE, CODE_KEY)
            .build()

        val body = FormBody.Builder()
            //.add(CODE_VALUE, CODE)
            //.add(SCOPE, SCOPES)
            //.add(CLIENT_ID, ApiConstants.CLIENT)
            .add(CLIENT_SECRET, SECRET)
            //.add(GRANT_TYPE_KEY, GRANT_TYPE)
            .build()

        val tokenRefresh = request()
            .newBuilder()
            .post(body)
            .url(url)
            .build()

        return proceedDeletingTokenIfUnauthorized(tokenRefresh)
    }

    private fun Interceptor.Chain.proceedDeletingTokenIfUnauthorized(request: Request): Response {
        val response = proceed(request)

        if (response.code == UNAUTHORIZED) {
            preferences.deleteTokenInfo()
        }
        return response
    }

    private fun mapToken(tokenRefreshResponse: Response): ApiToken {
        val moshi = Moshi.Builder().build()
        val tokenAdapter = moshi.adapter(ApiToken::class.java)
        val responseBody = tokenRefreshResponse.body!!

        return tokenAdapter.fromJson(responseBody.string()) ?: ApiToken.INVALID
    }

    private fun storeNewToken(apiToken: ApiToken) {
        with(preferences) {
            putTokenType(apiToken.meta?.status!!)
            putTokenExpirationTime(apiToken.expireAt)
            putToken(apiToken.accessToken!!)
        }
    }

}
