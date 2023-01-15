package com.ib.eventaid.search.presentation

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ib.eventaid.RxImmediateSchedulerRule
import com.ib.eventaid.TestCoroutineRule
import com.ib.eventaid.common.data.FakeRepository
import com.ib.eventaid.common.data.di.ApiModule
import com.ib.eventaid.common.data.di.CacheModule
import com.ib.eventaid.common.data.di.PreferencesModule
import com.ib.eventaid.common.di.ActivityRetainedModule
import com.ib.eventaid.common.domain.repositories.EventRepository
import com.ib.eventaid.common.utils.CoroutineDispatchersProviders
import com.ib.eventaid.common.utils.DispatchersProvider
import com.ib.eventaid.search.R
import com.ib.eventaid.search.launchFragmentInHiltContainer
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(ApiModule::class, PreferencesModule::class, CacheModule::class, ActivityRetainedModule::class)

class SearchFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

    @BindValue
    val dispatcher: DispatchersProvider = CoroutineDispatchersProviders()

    @BindValue
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    @BindValue
    val repository: EventRepository = FakeRepository()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun searchFragment_testSearch_success() {
        //Given
        val nameToSearch = (repository as FakeRepository).remotelySearchableEvent.title
        launchFragmentInHiltContainer<SearchFragment>()

        // When
        with(onView(withId(R.id.search))) {
            perform(click())
            perform(typeSearchViewText(nameToSearch))
        }
        //Then
        with(onView(withId(R.id.searchRecyclerView))) {
            check(matches(childCountIs(1)))
            check(matches(hasDescendant(withText(nameToSearch))))
        }
    }

    private fun typeSearchViewText(text: String): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Type in SearchView"
            }

            override fun getConstraints(): Matcher<View> {
                return allOf(
                    isDisplayed(),
                    isAssignableFrom(SearchView::class.java)
                )
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as SearchView).setQuery(text, false)
            }
        }
    }

    private fun childCountIs(expectedChildCount: Int): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("RecyclerView with item count: $expectedChildCount")
            }

            override fun matchesSafely(item: RecyclerView?): Boolean {
                return item?.adapter?.itemCount == expectedChildCount
            }
        }
    }
}
