package com.ib.eventaid.performers.presentation

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
import com.ib.eventaid.common.presentation.model.PerformersAdapter
import com.ib.eventaid.performers.databinding.FragmentPerformersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PerformerFragment : Fragment() {

    companion object {
        private const val ITEMS_PER_ROW = 2
    }

    private val viewModel: PerformersFragmentViewModel by viewModels()
    private val binding get() = _binding!!

    private var _binding: FragmentPerformersBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerformersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        val adapter = createAdapter()
        setupRecyclerView(adapter)
        subscribeToViewStateUpdates(adapter)
    }

    private fun createAdapter(): PerformersAdapter {
        return PerformersAdapter().apply {
            setOnPerformersClickListener{performerId ->
                val action = PerformerFragmentDirections.actionPerformersToDetails(performerId)
                findNavController().navigate(action)
            }
        }
    }

    private fun setupRecyclerView(eventPerformerAdapter: PerformersAdapter) {
        binding.performersRecyclerView.apply {
            adapter = eventPerformerAdapter
            layoutManager = GridLayoutManager(
                requireContext(), ITEMS_PER_ROW)
            setHasFixedSize(true)
            addOnScrollListener(createInfiniteScrollListener(layoutManager as GridLayoutManager))
        }
    }

    private fun createInfiniteScrollListener(
        layoutManager: GridLayoutManager
    ): RecyclerView.OnScrollListener {
        return object : InfiniteScrollListener(
            layoutManager,
            PerformersFragmentViewModel.UI_PAGE_SIZE
        ) {
            override fun loadMoreItems() {
                requestMorePerformers()
            }

            override fun isLoading(): Boolean = viewModel.isLoadingMorePerformers
            override fun isLastPage(): Boolean = viewModel.isLastPage
        }
    }

    private fun requestMorePerformers() {
        viewModel.onEvent(PerformersEvents.RequestMorePerformers)
    }

    private fun subscribeToViewStateUpdates(adapter: PerformersAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    updateScreenState(it, adapter)
                }
            }
        }
    }

    private fun updateScreenState(state: PerformersViewState, adapter: PerformersAdapter) {
        binding.progressBar.isVisible = state.loading
        adapter.submitList(state.performers)
        handleNoMorePerformers(state.noMorePerformers)
        handleFailures(state.failure)
    }

    private fun handleNoMorePerformers(noMorePerformers: Boolean) {
        ////ddddddddd
    }

    private fun handleFailures(failure: Event<Throwable>?) {
        val unhandleFailure = failure?.getContentIfNotHandled() ?: return
        val fallbackMessage = getString(com.ib.eventaid.common.R.string.an_error_occurred)

        val snackbarMessage = if (unhandleFailure.message.isNullOrEmpty()) {
            fallbackMessage
        } else {
            unhandleFailure.message!!
        }

        if (snackbarMessage.isNotEmpty()) {
            Snackbar.make(requireView(), snackbarMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}