package com.ib.eventaid.eventsnearyou.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ib.eventaid.common.presentation.model.Event
import com.ib.eventaid.common.presentation.model.EventsAdapter
import com.ib.eventaid.eventsnearyou.R
import com.ib.eventaid.eventsnearyou.databinding.FragmentEventsNearYouBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventsNearYouFragment : Fragment() {

  companion object {
    private const val ITEMS_PER_ROW = 2
  }

  private val viewModel: EventsNearYouFragmentViewModel by viewModels()
  private val binding get() = _binding!!

  private var _binding: FragmentEventsNearYouBinding? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = FragmentEventsNearYouBinding.inflate(inflater, container, false)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupUI()
    requestInitialEventsList()
  }

  private fun setupUI() {
    val adapter = createAdapter()
    setupRecyclerView(adapter)
    subscribeToViewStateUpdates(adapter)
  }

  private fun createAdapter(): EventsAdapter {
    return EventsAdapter().apply {
      setOnEventClickListener{ eventId ->
        val action = EventsNearYouFragmentDirections.actionEventsNearYouToDetails(eventId)
        findNavController().navigate(action)
      }
    }
  }

  private fun setupRecyclerView(eventsNearYouAdapter: EventsAdapter) {
    binding.eventsRecyclerView.apply {
      adapter = eventsNearYouAdapter
      layoutManager = GridLayoutManager(requireContext(), ITEMS_PER_ROW)
      setHasFixedSize(true)
      addOnScrollListener(createInfiniteScrollListener(layoutManager as GridLayoutManager))
    }
  }

  private fun createInfiniteScrollListener(
    layoutManager: GridLayoutManager
  ): RecyclerView.OnScrollListener {
    return object : InfiniteScrollListener(
      layoutManager,
        EventsNearYouFragmentViewModel.UI_PAGE_SIZE
    ) {
      override fun loadMoreItems() { requestMoreEvents() }
      override fun isLoading(): Boolean = viewModel.isLoadingMoreEvents
      override fun isLastPage(): Boolean = viewModel.isLastPage
    }
  }

  private fun requestMoreEvents() {
    viewModel.onEvent(EventsNearYouEvent.RequestMoreEvents)
  }

  private fun subscribeToViewStateUpdates(adapter: EventsAdapter) {
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.state.collect {
          updateScreenState(it, adapter)
        }
      }
    }
  }

  private fun updateScreenState(state: EventsNearYouViewState, adapter: EventsAdapter) {
    binding.progressBar.isVisible = state.loading
    adapter.submitList(state.events)
    handleNoMoreEventsNearby(state.noMoreEventsNearby)
    handleFailures(state.failure)
  }

  private fun handleNoMoreEventsNearby(noMoreEventsNearby: Boolean) {
    // Show a warning message and a prompt for the user to try a different
    // distance or postcode
  }

  private fun handleFailures(failure: Event<Throwable>?) {
    val unhandledFailure = failure?.getContentIfNotHandled() ?: return

    val fallbackMessage = getString(R.string.an_error_occurred)
    val snackbarMessage = if (unhandledFailure.message.isNullOrEmpty()) {
      fallbackMessage
    }
    else {
      unhandledFailure.message!!
    }

    if (snackbarMessage.isNotEmpty()) {
      Snackbar.make(requireView(), snackbarMessage, Snackbar.LENGTH_SHORT).show()
    }
  }

  private fun requestInitialEventsList() {
    viewModel.onEvent(EventsNearYouEvent.RequestInitialEventsList)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
