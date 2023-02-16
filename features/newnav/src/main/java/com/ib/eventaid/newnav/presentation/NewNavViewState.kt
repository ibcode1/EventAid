package com.ib.eventaid.newnav.presentation

import androidx.annotation.StringRes
import com.ib.eventaid.newnav.R

data class NewNavViewState(
    val postcode: String = "",
    val distance: String = "",
    @StringRes val postcodeError: Int = R.string.no_error,
    @StringRes val distanceError: Int = R.string.no_error
) {
    val submitButtonActive: Boolean
        get() {
            return postcode.isNotEmpty() &&
                    distance.isNotEmpty() &&
                    postcodeError == R.string.no_error &&
                    distanceError == R.string.no_error
        }
}