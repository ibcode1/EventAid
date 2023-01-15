package com.ib.eventaid.common.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ib.eventaid.common.data.api.EventAidApi
import com.ib.eventaid.common.data.api.model.mappers.ApiEventMapper
import com.ib.eventaid.common.data.api.model.mappers.ApiPaginationMapper
import com.ib.eventaid.common.data.api.utils.FakeServer
import com.ib.eventaid.common.data.cache.Cache
import com.ib.eventaid.common.data.di.PreferencesModule
import com.ib.eventaid.common.domain.repositories.EventRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.After
import com.google.common.truth.Truth.assertThat
import com.ib.eventaid.common.data.cache.EventAidDatabase
import com.ib.eventaid.common.data.cache.RoomCache
import com.ib.eventaid.common.data.di.CacheModule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(PreferencesModule::class, CacheModule::class)
class SeatGeekEventRepositoryTest {
    private val fakeServer = FakeServer()
    private lateinit var repository: EventRepository
    private lateinit var api: EventAidApi


    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var cache: Cache

    @Inject
    lateinit var database: EventAidDatabase

    @Inject
    lateinit var retrofitBuilder: Retrofit.Builder

    @Inject
    lateinit var apiEventMapper: ApiEventMapper

    @Inject
    lateinit var apiPaginationMapper: ApiPaginationMapper


    @Before
    fun setup() {
        fakeServer.start()

        hiltRule.inject()

        api = retrofitBuilder
            .baseUrl(fakeServer.baseEndpoint)
            .build()
            .create(EventAidApi::class.java)

        cache = RoomCache(
            database.eventDao(),
            database.taxonomyDao(), database.venuesDao(), database.performerDao()
        )

        repository = com.ib.eventaid.common.data.SeatGeekEventRepository(
            api, cache, apiEventMapper, apiPaginationMapper
        )
    }

    @After
    fun teardown() {
        fakeServer.shutDown()
    }

    @Test
    fun requestMoreEvents_success() = runBlocking {
        //Given
        val expectedEventId = 5825090
        fakeServer.setHappyPathDispatcher()

        //when
        val paginatedEvents = repository.requestMoreEvents(1, 100)

        //then
        val event = paginatedEvents.events.first()
        assertThat(event.id).isEqualTo(expectedEventId)
    }

    @Test
    fun insertEvents_success() {
        //given
        val expectedEventId = 5825090

        runBlocking {
            fakeServer.setHappyPathDispatcher()

            val paginatedEvents = repository.requestMoreEvents(1, 100)
            val event = paginatedEvents.events.first()

            //when
            repository.storeEvents(listOf(event))
        }

        //then
        val testObserver = repository.getEvents().test()
        testObserver.assertNoErrors()
        testObserver.assertNotComplete()
        testObserver.assertValue { it.first().id == expectedEventId }

    }
}