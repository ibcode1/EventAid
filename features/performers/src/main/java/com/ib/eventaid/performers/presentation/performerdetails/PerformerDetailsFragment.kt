package com.ib.eventaid.performers.presentation.performerdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ib.eventaid.common.presentation.InfiniteScrollListeners
import com.ib.eventaid.common.presentation.UIPerformerEvents
import com.ib.eventaid.common.presentation.model.PerformerEventsAdapter
import com.ib.eventaid.common.utils.setImage
import com.ib.eventaid.performers.databinding.FragmentPerformerDetailsBinding
import com.ib.eventaid.performers.presentation.performerdetails.model.UIPerformerDetailed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PerformerDetailsFragment : Fragment() {

    companion object {
        const val PERFORMER_ID = "id"
        const val EVENT_ID = "eventId"
    }

    private val binding get() = _binding!!
    private var _binding: FragmentPerformerDetailsBinding? = null

    private val viewModel: PerformerDetailsFragmentViewModel by viewModels()

    private var performerId: Int? = null
    private var eventId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performerId = requireArguments().getInt(PERFORMER_ID)
        eventId = requireArguments().getInt(EVENT_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerformerDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val event = PerformerDetailsEvent.LoadPerformerDetails(performerId!!)
        viewModel.handleEvent(event)
        setupUi()
    }

    private fun subscribeToStateUpdates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.perfState.collect { state ->
                    when (state) {
                        is PerformerDetailsViewState.Loading -> {
                            displayLoading()
                        }
                        is PerformerDetailsViewState.Failure -> {
                            displayError()
                        }
                        is PerformerDetailsViewState.PerformerDetails -> {
                            displayPerformer(state.performer)
                        }
                    }
                }
            }
        }
    }

    private fun displayPerformer(performer:UIPerformerDetailed) {
        binding.title.text = performer.name
        binding.numberOfEvents.text = performer.numUpcomingEvents.toString()
        binding.image.setImage(performer.image)
    }

    private fun createInfiniteScrollListener(
        layoutManager: LinearLayoutManager
    ): RecyclerView.OnScrollListener {
        return object : InfiniteScrollListeners(
            layoutManager,
            PerformerDetailsFragmentViewModel.UI_PAGE_SIZE
        ) {
            override fun loadMoreItems() { requestMorePerformerEvents() }
            override fun isLoading(): Boolean = viewModel.isLoadingMoreEvents
            override fun isLastPage(): Boolean = viewModel.isLastPage
        }
    }

    private fun requestMorePerformerEvents() {
        viewModel.handleEvent(PerformerDetailsEvent.RequestMoreEvents(performerId!!))
    }

    private fun subscribeToViewStateUpdates(adapter: PerformerEventsAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    displayPerformerDetails(it,adapter)
                }
            }
        }
    }

    private fun displayPerformerDetails(state: PerformerEventsViewState,adapter: PerformerEventsAdapter) {
        adapter.submitList(state.uiPerformerEvents)
        binding.performerRecyclerView.layoutManager=LinearLayoutManager(requireContext()
            ,LinearLayoutManager.VERTICAL,false)
        binding.performerRecyclerView.adapter = adapter

    }

    private fun setupUi() {
        val adapter = createAdapter()
        setupRecyclerView(adapter)
        subscribeToViewStateUpdates(PerformerEventsAdapter())
        subscribeToStateUpdates()
    }

    private fun createAdapter(): PerformerEventsAdapter {
        return PerformerEventsAdapter()
    }

    private fun setupRecyclerView(performerEventsAdapter: PerformerEventsAdapter) {
        binding.performerRecyclerView.apply {
            adapter = performerEventsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            addOnScrollListener(createInfiniteScrollListener(layoutManager as LinearLayoutManager))
        }
    }

    private fun displayError() {
        //binding.group.isVisible = false
        Snackbar.make(
            requireView(),
            com.ib.eventaid.common.R.string.an_error_occurred,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun displayLoading() {
        //binding.group.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}