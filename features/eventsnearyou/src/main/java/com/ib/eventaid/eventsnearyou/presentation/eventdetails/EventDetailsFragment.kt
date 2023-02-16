package com.ib.eventaid.eventsnearyou.presentation.eventdetails

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.*
import androidx.annotation.RawRes
import androidx.core.net.toUri
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.google.android.material.snackbar.Snackbar
import com.ib.eventaid.common.presentation.model.EventPerformerAdapter
import com.ib.eventaid.common.utils.setImage
import com.ib.eventaid.eventsnearyou.R
import com.ib.eventaid.eventsnearyou.databinding.FragmentDetailsBinding
import com.ib.eventaid.eventsnearyou.presentation.eventdetails.model.UIEventDetailed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@AndroidEntryPoint
class EventDetailsFragment : Fragment() {

    companion object {
        const val EVENT_ID = "id"
    }

    private val binding get() = _binding!!
    private var _binding: FragmentDetailsBinding? = null

    private val viewModel: EventDetailsFragmentViewModel by viewModels()
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
                        is EventDetailsViewState.Loading -> {
                            displayLoading()
                        }
                        is EventDetailsViewState.Failure -> {
                            displayError()
                        }
                        is EventDetailsViewState.EventDetails -> {
                            displayEventDetails(state.event)
                        }
                    }
                }
            }
        }
    }


    private fun displayEventDetails(eventDetails: UIEventDetailed) {

        stopAnimation()
        eventPerformerAdapter.submitList(eventDetails.performer)
        binding.performersList.layoutManager = LinearLayoutManager(requireContext()
            ,LinearLayoutManager.VERTICAL, false)
        binding.performersList.adapter = eventPerformerAdapter

        binding.scrollView.isVisible = true
        binding.title.text = eventDetails.title
        binding.venueName.text = eventDetails.venue
        binding.venueAddress.text = eventDetails.address
        binding.venueState.text = eventDetails.city
        binding.venueCapacity.text = if (eventDetails.capacity.toString() == "0") {
            "N/A"
        } else {
            eventDetails.capacity.toString()
        }

        binding.dateTime.text = if (eventDetails.date.toString() < LocalDateTime.now().toString()) {
            "Completed on ${eventDetails.date.toString().replace("T", " ")}"
        } else {
            eventDetails.date.toString().replace("T", " ")
        }.toString()

        binding.ticket.text = eventDetails.price
        binding.image.setImage(eventDetails.image)
        binding.backdrop.setImage(eventDetails.image)
        binding.ratingValue.text = eventDetails.score.toString()
        binding.eventTaxonomies.text = eventDetails.taxonomy.toString()
            .replace("[", "")
            .replace("]", "")
            .replace("_", " ")
            .replace("_", " ")
    }

    private fun startAnimation(@RawRes animationRes: Int) {
        binding.loader.apply {
            isVisible = true
            setMinFrame(50)
            setMaxFrame(112)
            speed = 1.5f
            setAnimation(animationRes)
            playAnimation()
        }
        binding.loader.addValueCallback(
            KeyPath("icon_circle", "**"),
            LottieProperty.COLOR_FILTER
        ) {
            PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun displayError() {
        startAnimation(R.raw.error_data)
        binding.scrollView.isVisible = false
        Snackbar.make(
            requireView(),
            R.string.an_error_occurred,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun displayLoading() {
        startAnimation(R.raw.data_2)
        binding.scrollView.isVisible = false
    }

    private fun stopAnimation() {
        binding.loader.apply {
            cancelAnimation()
            isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

