
package com.ib.eventaid.search.presentation

sealed class SearchEvent {
  object RequestMoreEvents: SearchEvent()
  object PrepareForSearch : SearchEvent()
  data class QueryInput(val input: String): SearchEvent()
  data class TypeValueSelected(val type: String): SearchEvent()
}
