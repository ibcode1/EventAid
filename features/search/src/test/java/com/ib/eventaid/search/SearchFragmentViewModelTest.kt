package com.ib.eventaid.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ib.eventaid.RxImmediateSchedulerRule
import com.ib.eventaid.TestCoroutineRule
import com.ib.eventaid.common.data.FakeRepository
import com.ib.eventaid.common.presentation.mappers.UiEventMapper
import com.ib.eventaid.common.presentation.model.Event
import com.ib.eventaid.common.utils.DispatchersProvider
import com.ib.eventaid.search.presentation.SearchEvent
import com.ib.eventaid.search.presentation.SearchFragmentViewModel
import com.ib.eventaid.search.presentation.SearchViewState
import com.ib.eventaid.search.usecases.GetSearchFilters
import com.ib.eventaid.search.usecases.SearchEvents
import com.ib.eventaid.search.usecases.SearchEventsRemotely
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchFragmentViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var viewModel: SearchFragmentViewModel
    private lateinit var repository: FakeRepository
    private lateinit var getSearchFilters: GetSearchFilters

    private val uiEventsMapper = UiEventMapper()

    @Before
    fun setup() {
        val dispatchersProvider = object : DispatchersProvider {
            override fun io() = testCoroutineRule.testDispatcher
        }
        repository = FakeRepository()
        getSearchFilters = GetSearchFilters(repository,dispatchersProvider)

        viewModel = SearchFragmentViewModel(
            uiEventsMapper,
            SearchEventsRemotely(repository,dispatchersProvider),
            SearchEvents(repository),
            getSearchFilters,
            CompositeDisposable()
        )
    }

    @Test
    fun `SearchFragmentViewModel remote search with success`() = runTest {
        //Given
        val (title, type, performer) =repository.remotelySearchableEvent
        val (types) = getSearchFilters()

        val expectedRemoteEvents = repository.remoteEvents.map {
            uiEventsMapper.mapToView(it)
        }
        val expectedViewState = SearchViewState(
            noSearchQuery = false,
            searchResults = expectedRemoteEvents,
            typeFilterValues = Event(types),
            searchingRemotely = false,
            noRemoteResults = false
        )

        //When
        viewModel.onEvent(SearchEvent.PrepareForSearch)
        viewModel.onEvent(SearchEvent.TypeValueSelected(type))
        viewModel.onEvent(SearchEvent.QueryInput(title))

        //Then
        val viewState = viewModel.state.value

        assertThat(viewState).isEqualTo(expectedViewState)
    }

}