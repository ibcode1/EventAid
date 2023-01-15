
package com.ib.eventaid.common.data.di



import com.ib.eventaid.common.data.preferences.EventAidPreferences
import com.ib.eventaid.common.data.preferences.Preferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds
    abstract fun providePreferences(preferences: EventAidPreferences):Preferences
}

