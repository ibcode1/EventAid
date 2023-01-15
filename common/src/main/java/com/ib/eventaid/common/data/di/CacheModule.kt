package com.ib.eventaid.common.data.di


import android.content.Context
import androidx.room.Room
import com.ib.eventaid.common.data.cache.Cache
import com.ib.eventaid.common.data.cache.EventAidDatabase
import com.ib.eventaid.common.data.cache.RoomCache
import com.ib.eventaid.common.data.cache.daos.EventDao
import com.ib.eventaid.common.data.cache.daos.PerformerDao
import com.ib.eventaid.common.data.cache.daos.TaxonomyDao
import com.ib.eventaid.common.data.cache.daos.VenueDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Binds
    abstract fun bindCache(cache: RoomCache): Cache

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ): EventAidDatabase {
            return Room.databaseBuilder(
                context,
                EventAidDatabase::class.java,
                "eventAid.db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        @Provides
        fun provideEventsDao(eventAidDatabase: EventAidDatabase): EventDao =
            eventAidDatabase.eventDao()

        @Provides
        fun provideVenuesDao(eventAidDatabase: EventAidDatabase): VenueDao =
            eventAidDatabase.venuesDao()

        @Provides
        fun provideTaxonomyDao(eventAidDatabase: EventAidDatabase): TaxonomyDao =
            eventAidDatabase.taxonomyDao()

        @Provides
        fun providePerformerDao(eventAidDatabase: EventAidDatabase): PerformerDao =
            eventAidDatabase.performerDao()
    }
}

