package com.ib.eventaid.newnav.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.ib.eventaid.newnav.R
import com.ib.eventaid.newnav.databinding.FragmentNavChangeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewNavFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding: FragmentNavChangeBinding? = null

    private val viewModel by viewModels<NewNavFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNavChangeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewStateUpdates()
        observeViewEffects()
    }

    private fun setupUI() {
        setupPostcodeTextField()
        setupDistanceTextField()
        listenToSubmitButton()
    }

    private fun setupPostcodeTextField() {
        binding.postcodeInputText.doAfterTextChanged {
            viewModel.onEvent(NewNavEvent.PostcodeChanged(it!!.toString()))
        }
    }

    private fun setupDistanceTextField() {
        binding.maxDistanceInputText.doAfterTextChanged {
            viewModel.onEvent(NewNavEvent.DistanceChanged(it!!.toString()))
        }
    }

    private fun listenToSubmitButton() {
        binding.submitButton.setOnClickListener {
            viewModel.onEvent(NewNavEvent.SubmitButtonClicked)
        }
    }

    private fun observeViewStateUpdates() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect { render(it) }
        }
    }

    private fun render(state: NewNavViewState) {
        with (binding) {
            postcodeTextInputLayout.error = resources.getString(state.postcodeError)
            maxDistanceTextInputLayout.error = resources.getString(state.distanceError)
            submitButton.isEnabled = state.submitButtonActive
        }
    }

    private fun observeViewEffects() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.viewEffects.collect { reactTo(it) }
        }
    }

    private fun reactTo(effect: NewNavViewEffect) {
        when (effect) {
            is NewNavViewEffect.NavigateToEventsNearYou -> navigateToEventsNearYou()
        }
    }

    private fun navigateToEventsNearYou(){
        val deepLink = NavDeepLinkRequest.Builder
            .fromUri("eventaid://eventsnearyou".toUri())
            .build()

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_newnav, true)
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .build()

        findNavController().navigate(deepLink, navOptions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}