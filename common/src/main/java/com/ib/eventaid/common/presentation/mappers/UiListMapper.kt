package com.ib.eventaid.common.presentation.mappers


interface UiListMapper<E, V> {

    fun mapToView(input: E): List<V>
}
