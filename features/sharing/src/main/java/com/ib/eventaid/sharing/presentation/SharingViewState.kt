
package com.ib.eventaid.sharing.presentation

import com.ib.eventaid.sharing.presentation.model.UIEventToShare


data class SharingViewState(
    val eventToShare: UIEventToShare = UIEventToShare(image = "", defaultMessage = "")
)
