package com.ib.eventaid.eventsnearyou.presentation.eventdetails

import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ib.eventaid.common.presentation.UIEventPerformer
import com.ib.eventaid.common.presentation.UIPerformer
import com.ib.eventaid.common.presentation.model.EventPerformerAdapter
import com.ib.eventaid.common.utils.setImage
import com.ib.eventaid.eventsnearyou.R
import com.ib.eventaid.eventsnearyou.databinding.FragmentDetailsBinding
import com.ib.eventaid.eventsnearyou.presentation.eventdetails.model.UIEventDetailed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventDetailsFragment : Fragment() {

    companion object {
        const val EVENT_ID = "id"
    }

    private val binding get() = _binding!!
    private var _binding: FragmentDetailsBinding? = null

    private val viewModel: EventDetailsFragmentViewModel by viewModels()
    private var eventId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eventId = requireArguments().getInt(EVENT_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addShareMenu()
        binding.share.setOnClickListener {
            navigateToSharing()
        }
        subscribeToStateUpdates()
        val event = EventDetailsEvent.LoadEventDetails(eventId!!)
        viewModel.handleEvent(event)
        setupUI()
    }

    private fun addShareMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_share, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return if (menuItem.itemId == R.id.share) {
                    navigateToSharing()
                    true
                } else {
                    false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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
//                        is EventDetailsViewState.Loading -> {
//                            displayLoading()
//                        }
//                        is EventDetailsViewState.Failure -> {
//                            displayError()
//                        }
                        is EventDetailsViewState.EventDetails -> {
                            displayEventDetails(state.event)
                        }
                        else -> {}
                    }
                }
            }
        }
    }


    private fun displayEventDetails(eventDetails: UIEventDetailed) {
        //binding.group.isVisible = true
        binding.title.text = eventDetails.title
        binding.description.text = eventDetails.description
        binding.image.setImage(eventDetails.image)
//        binding.averagePrice.text = getText(eventDetails.averagePrice)
//        binding.highestPrice.text = getText(eventDetails.highestPrice)
//        binding.lowestPrice.text = getText(eventDetails.lowestPrice)

    }

    private fun setupUI() {
        val adapter = createAdapter()
        setupRecyclerView(adapter)
    }

    private fun createAdapter(): EventPerformerAdapter {
        return EventPerformerAdapter()
    }

    private fun setupRecyclerView(eventPerformerAdapter: EventPerformerAdapter) {
        binding.performersList.apply {
            adapter = eventPerformerAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun displayError() {
        //binding.group.isVisible = false
        Snackbar.make(requireView(), R.string.an_error_occurred, Snackbar.LENGTH_SHORT).show()
    }

    private fun displayLoading() {
        //binding.group.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

