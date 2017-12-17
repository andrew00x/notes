package com.andrew00x.notes

interface TodoService {
    fun save(todo: Todo): Todo

    fun delete(todo: Todo)

    fun retrieveAll(): Iterable<Todo>

    fun find(title: String): Iterable<Todo>

    fun deleteAll()
}