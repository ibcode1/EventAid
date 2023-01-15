/*
 * Copyright (c) 2022 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ib.eventaid.common.data.di

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
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
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TestCacheModule {

    @Binds
    abstract fun bindCache(cache: RoomCache): Cache

    companion object {

        @Provides
        @Singleton
        fun provideRoomDatabase(): EventAidDatabase {
            return Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().context,
                EventAidDatabase::class.java
            )
                .allowMainThreadQueries()
                .build()
        }

        @Provides
        fun provideEventsDao(eventAidDatabase: EventAidDatabase): EventDao =
            eventAidDatabase.eventDao()

        @Provides
        fun provideVenuesDao(eventAidDatabase: EventAidDatabase): VenueDao =
            eventAidDatabase.venuesDao()

        @Provides
        fun providePerformerDao(eventAidDatabase: EventAidDatabase): PerformerDao =
            eventAidDatabase.performerDao()

        @Provides
        fun provideTaxonomyDao(eventAidDatabase: EventAidDatabase): TaxonomyDao = eventAidDatabase.taxonomyDao()
    }
}
