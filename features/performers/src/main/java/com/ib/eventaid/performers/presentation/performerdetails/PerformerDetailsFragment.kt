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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ib.eventaid.common.presentation.model.PerformersAdapter
import com.ib.eventaid.common.utils.setImage
import com.ib.eventaid.performers.R
import com.ib.eventaid.performers.databinding.FragmentPerformerDetailsBinding
import com.ib.eventaid.performers.presentation.performerdetails.model.UIPerformerDetailed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PerformerDetailsFragment:Fragment() {

    companion object{
        const val PERFORMER_ID = "id"
    }

    private val binding get() = _binding!!
    private var _binding:FragmentPerformerDetailsBinding? = null

    private val viewModel:PerformerDetailsFragmentViewModel by viewModels()

    private var performerId:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performerId = requireArguments().getInt(PERFORMER_ID)
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

        subscribeToStateUpdates()
        val event = PerformerDetailsEvent.LoadPerformerDetails(performerId!!)
        viewModel.handleEvent(event)
        setupUi()
    }

    private fun subscribeToStateUpdates() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.state.collect { state ->
                    when(state) {
                        is PerformerDetailsViewState.Loading -> { displayLoading() }
                        is PerformerDetailsViewState.Failure ->{displayError()}
                        is PerformerDetailsViewState.PerformerDetails ->{displayPerformerDetails(state.performer)}
                    }
                }
            }
        }
    }

    private fun displayPerformerDetails(performer: UIPerformerDetailed) {
        binding.title.text = performer.name
        binding.numberOfEvents.text = performer.numUpcomingEvents.toString()
        binding.image.setImage(performer.image)
        setupUi()
    }

    private fun setupUi(){
        val adapter = createAdapter()
        setupRecyclerView(adapter)
    }

    private fun createAdapter():PerformersAdapter{
        return PerformersAdapter()
    }

    private fun setupRecyclerView(performersAdapter: PerformersAdapter){
        binding.performerRecyclerView.apply {
            adapter = performersAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
        }
    }

    private fun displayError() {
        //binding.group.isVisible = false
        Snackbar.make(requireView(), com.ib.eventaid.common.R.string.an_error_occurred, Snackbar.LENGTH_SHORT).show()
    }

    private fun displayLoading() {
        //binding.group.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}