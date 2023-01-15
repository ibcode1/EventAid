
package com.ib.eventaid.common.di

import com.ib.eventaid.common.data.FakeRepository
import com.ib.eventaid.common.di.ActivityRetainedModule
import com.ib.eventaid.common.domain.repositories.EventRepository
import com.ib.eventaid.common.utils.CoroutineDispatchersProviders
import com.ib.eventaid.common.utils.DispatchersProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.testing.TestInstallIn
import io.reactivex.disposables.CompositeDisposable

@Module
@TestInstallIn(
  components = [ActivityRetainedComponent::class],
  replaces = [ActivityRetainedModule::class]
)
abstract class TestActivityRetainedModule {

  @Binds
  @ActivityRetainedScoped
  abstract fun bindAnimalRepository(repository: FakeRepository): EventRepository

  @Binds
  abstract fun bindDispatchersProvider(
    dispatchersProvider: CoroutineDispatchersProviders
  ): DispatchersProvider

  companion object {
    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()
  }
}
