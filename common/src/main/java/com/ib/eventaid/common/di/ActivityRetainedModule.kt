package com.ib.eventaid.common.di

import com.ib.eventaid.common.data.SeatGeekEventRepository
import com.ib.eventaid.common.data.SeatGeekPerformerRepository
import com.ib.eventaid.common.domain.repositories.EventRepository
import com.ib.eventaid.common.domain.repositories.PerformerRepository
import com.ib.eventaid.common.utils.CoroutineDispatchersProviders
import com.ib.eventaid.common.utils.DispatchersProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.reactivex.disposables.CompositeDisposable

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRetainedModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindEventRepository(repository: SeatGeekEventRepository): EventRepository

    @Binds
    abstract fun bindDispatchersProvider(dispatchersProviders: CoroutineDispatchersProviders): DispatchersProvider

    @Binds
    @ActivityRetainedScoped
    abstract fun bindPerformerRepository(repository: SeatGeekPerformerRepository):PerformerRepository

    companion object {
        @Provides
        fun provideCompositeDisposable() = CompositeDisposable()
    }
}