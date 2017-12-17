package com.andrew00x.notes.ui

data class ListItem<out T>(val data: T, var selected: Boolean = false)

interface ItemSelectListener<in T> {
    fun onSelectionChanged(item: ListItem<T>)
}

interface ItemClickListener<in T> {
    fun onItemClick(item: ListItem<T>)
}
