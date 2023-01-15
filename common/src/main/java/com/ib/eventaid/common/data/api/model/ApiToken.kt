package com.ib.eventaid.common.data.api.model

import com.ib.eventaid.common.data.api.model.oauth.Meta
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

@JsonClass(generateAdapter = true)

/*data class ApiToken(
    //@field:Json(name = "code") val code: String?,
    @field:Json(name = "token_type") val tokenType: String?,
    @field:Json(name = "expires") val expiresInSeconds: Int?,
    @field:Json(name = "access_token") val accessToken: String?
) {

    companion object {
        val INVALID = ApiToken("", -1, "")
    }

    @Transient
    private val requestAt: Instant = Instant.now()

    val expireAt: Long
        get() {
            if (expiresInSeconds == null) return 0L

            return requestAt.plusSeconds(expiresInSeconds.toLong()).epochSecond
        }

    fun isValid(): Boolean {

        //code != null && code.isNotEmpty() &&
        return expiresInSeconds != null && expiresInSeconds >= 0 &&
                accessToken != null && accessToken.isNotEmpty()
    }
}*/


data class ApiToken(
    @field:Json(name = "access_token") val accessToken: String?,
    @field:Json(name = "expires") val expiresInSeconds: Int?,
    @field:Json(name = "meta") val meta: Meta?
) {
    companion object {
        val INVALID = ApiToken("", -1, Meta(-1))
    }

    @Transient
    private val requestAt: Instant = Instant.now()

    val expireAt: Long
        get() {
            if (expiresInSeconds == null) return 0L

            return requestAt.plusSeconds(expiresInSeconds.toLong()).epochSecond
        }

    fun isValid(): Boolean {
        return meta?.status != null && meta.status >= 0
                && expiresInSeconds != null && expiresInSeconds >= 0 &&
                accessToken != null && accessToken.isNotEmpty()
    }
}
