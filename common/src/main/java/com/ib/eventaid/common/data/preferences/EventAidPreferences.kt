package com.ib.eventaid.common.data.preferences


import android.content.Context
import android.content.SharedPreferences
import com.ib.eventaid.common.data.preferences.PreferencesConstant.KEY_MAX_RANGE
import com.ib.eventaid.common.data.preferences.PreferencesConstant.KEY_POSTCODE
import com.ib.eventaid.common.data.preferences.PreferencesConstant.KEY_TOKEN
import com.ib.eventaid.common.data.preferences.PreferencesConstant.KEY_TOKEN_EXPIRATION_TIME
import com.ib.eventaid.common.data.preferences.PreferencesConstant.KEY_TOKEN_TYPE
import com.ib.eventaid.common.data.preferences.PreferencesConstant.PERFORMER_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventAidPreferences @Inject constructor(
    @ApplicationContext context: Context
) : Preferences {

    companion object {
        const val PREFERENCES_NAME = "EVENT_AID_PREFERENCES"
    }

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun putToken(token: String) {
        edit { putString(KEY_TOKEN,token) }
    }

    override fun putTokenExpirationTime(time: Long) {
        edit { putLong(KEY_TOKEN_EXPIRATION_TIME,time) }
    }

    override fun putTokenType(tokenType: Int) {
        edit { putInt(KEY_TOKEN_TYPE,tokenType) }
    }

    override fun getToken(): String {
        return preferences.getString(KEY_TOKEN, "").orEmpty()
    }

    override fun getTokenExpirationTime(): Long {
        return preferences.getLong(KEY_TOKEN_EXPIRATION_TIME, -1)
    }

    override fun getTokenType(): String {
        return preferences.getString(KEY_TOKEN_TYPE,"").orEmpty()
    }


    override fun deleteTokenInfo() {
        edit {
            remove(KEY_TOKEN)
            remove(KEY_TOKEN_EXPIRATION_TIME)
            remove(KEY_TOKEN_TYPE)
        }
    }

    override fun getPostcode(): String {
        return preferences.getString(KEY_POSTCODE, "").orEmpty()
    }

    override fun putPostcode(postcode: String) {
        edit { putString(KEY_POSTCODE, postcode) }
    }

    override fun getMaxDistanceAllowedToGetEvents(): String {
        return preferences.getString(KEY_MAX_RANGE,"0").orEmpty()
    }


    override fun putMaxDistanceAllowedToGetEvents(distance: String) {
        edit { putString(KEY_MAX_RANGE, distance + "mi") }
    }

    override fun getPerformerId(): Int {
        return preferences.getInt(PERFORMER_ID,0)
    }

    override fun putPerformerId(performerId: Int) {
        edit { putInt(PERFORMER_ID,performerId) }
    }

    private inline fun edit(block: SharedPreferences.Editor.() -> Unit){
      with(preferences.edit()){
          block()
          commit()
      }
    }
}
