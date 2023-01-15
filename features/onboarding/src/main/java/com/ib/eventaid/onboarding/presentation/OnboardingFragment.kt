
package com.ib.eventaid.onboarding.presentation

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
import com.ib.eventaid.onboarding.R
import com.ib.eventaid.onboarding.databinding.FragmentOnboardingBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment: Fragment() {


  private val binding get() = _binding!!
  private var _binding: FragmentOnboardingBinding? = null

  private val viewModel by viewModels<OnboardingFragmentViewModel>()

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {
    _binding = FragmentOnboardingBinding.inflate(inflater, container, false)

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
      viewModel.onEvent(OnboardingEvent.PostcodeChanged(it!!.toString()))
    }
  }

  private fun setupDistanceTextField() {
    binding.maxDistanceInputText.doAfterTextChanged {
      viewModel.onEvent(OnboardingEvent.DistanceChanged(it!!.toString()))
    }
  }

  private fun listenToSubmitButton() {
    binding.onboardingSubmitButton.setOnClickListener {
      viewModel.onEvent(OnboardingEvent.SubmitButtonClicked)
    }
  }

  private fun observeViewStateUpdates() {
    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
      viewModel.viewState.collect { render(it) }
    }
  }

  private fun render(state: OnboardingViewState) {
    with (binding) {
      postcodeTextInputLayout.error = resources.getString(state.postcodeError)
      maxDistanceTextInputLayout.error = resources.getString(state.distanceError)
      onboardingSubmitButton.isEnabled = state.submitButtonActive
    }
  }

  private fun observeViewEffects() {
    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
      viewModel.viewEffects.collect { reactTo(it) }
    }
  }

  private fun reactTo(effect: OnboardingViewEffect) {
    when (effect) {
      is OnboardingViewEffect.NavigateToEventsNearYou -> navigateToEventsNearYou()
    }
  }

  private fun navigateToEventsNearYou() {
    val deepLink = NavDeepLinkRequest.Builder
        .fromUri("eventaid://eventsnearyou".toUri())
        .build()

    val navOptions = NavOptions.Builder()
        .setPopUpTo(R.id.nav_onboarding, true)
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