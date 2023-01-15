
package com.ib.eventaid

import android.app.Application
import com.ib.logging.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EventAidApplication: Application() {

  // initiate analytics, crashlytics, etc

  override fun onCreate() {
    super.onCreate()

    initLogger()
  }

  private fun initLogger() {
    Logger.init()
  }
}
