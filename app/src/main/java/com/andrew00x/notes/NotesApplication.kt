package com.andrew00x.notes

class NotesApplication : com.activeandroid.app.Application() {
    val component: NotesComponent by lazy {
        DaggerNotesComponent.builder().notesModule(NotesModule()).build()
    }
}