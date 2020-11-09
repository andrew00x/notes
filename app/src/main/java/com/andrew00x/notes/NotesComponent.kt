package com.andrew00x.notes

import com.andrew00x.notes.ui.PurchaseFragment
import com.andrew00x.notes.ui.TodoFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NotesModule::class])
interface NotesComponent {
    fun todoService(): TodoService
    fun purchaseService(): PurchaseService
    fun injectInto(todo: TodoFragment)
    fun injectInto(purchases: PurchaseFragment)
}