package com.ib.eventaid

import androidx.annotation.NavigationRes

sealed class MainActivityViewEffect {
  data class SetStartDestination(@NavigationRes val destination: Int) : MainActivityViewEffect()
}
