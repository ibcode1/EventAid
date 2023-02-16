package com.ib.eventaid.common.presentation.mappers

interface UiMapper<E, V> {
    fun mapToView(input: E): V
}