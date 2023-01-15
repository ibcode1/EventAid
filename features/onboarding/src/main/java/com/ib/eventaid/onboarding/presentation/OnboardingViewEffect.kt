
package com.ib.eventaid.onboarding.presentation

sealed class OnboardingViewEffect {
  object NavigateToEventsNearYou : OnboardingViewEffect()
}