package com.ib.eventaid.search.presentation.searchdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ib.eventaid.common.databinding.FragmentsDetailsBinding
import com.ib.eventaid.common.presentation.model.EventPerformerAdapter
import com.ib.eventaid.common.utils.setImage
import com.ib.eventaid.search.R
import com.ib.eventaid.search.databinding.SearchFragmentDetailsBinding
import com.ib.eventaid.search.presentation.searchdetails.model.UISearchedEventDetailed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@AndroidEntryPoint
class SearchDetailsFragment : Fragment() {

    companion object {
        const val EVENT_ID = "id"
    }

    private val binding get() = _binding!!
    private var _binding: SearchFragmentDetailsBinding? = null

    private val viewModel: SearchDetailViewModel by viewModels()
    private var eventId: Int? = null

    private var eventPerformerAdapter = EventPerformerAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eventId = requireArguments().getInt(EVENT_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.share.setOnClickListener {
            navigateToSharing()
        }
        subscribeToStateUpdates()
        val event = SearchDetailsEvent.LoadEventDetails(eventId!!)
        viewModel.handleEvent(event)
    }

    private fun navigateToSharing() {
        val eventId = requireArguments().getInt(EVENT_ID)

        val deepLink = NavDeepLinkRequest.Builder
            .fromUri("eventaid://sharing/$eventId".toUri())
            .build()

        findNavController().navigate(deepLink)
    }

    private fun subscribeToStateUpdates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is SearchDetailsViewState.EventDetails -> {
                            displayEventDetails(state.event)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun displayEventDetails(searchDetails: UISearchedEventDetailed) {
        binding.title.text = searchDetails.title
        binding.image.setImage(searchDetails.image)
        eventPerformerAdapter.submitList(searchDetails.performer)
        binding.performersList.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.performersList.adapter = eventPerformerAdapter

        binding.scrollView.isVisible = true
        binding.title.text = searchDetails.title
        binding.venueName.text = searchDetails.venue
        binding.venueAddress.text = searchDetails.address
        binding.venueState.text = searchDetails.city
        binding.venueCapacity.text = if (searchDetails.capacity.toString() == "0") {
            "N/A"
        } else {
            searchDetails.capacity.toString()
        }

        binding.dateTime.text =
            if (searchDetails.date.toString() < LocalDateTime.now().toString()) {
                "Completed on ${searchDetails.date.toString().replace("T", " ")}"
            } else {
                searchDetails.date.toString().replace("T", " ")
            }.toString()

        binding.ticket.text = searchDetails.price
        binding.image.setImage(searchDetails.image)
        binding.backdrop.setImage(searchDetails.image)
        binding.ratingValue.text = searchDetails.score.toString()
        binding.eventTaxonomies.text = searchDetails.taxonomy.toString()
            .replace("[", "")
            .replace("]", "")
            .replace("_", " ")
            .replace("_", " ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

