package com.andrew00x.notes

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NotesModule {
    @Singleton
    @Provides
    fun todoService(): TodoService = AATodoService()

    @Singleton
    @Provides
    fun purchaseService(): PurchaseService = AAPurchaseService()

    @Singleton
    @Provides
    fun purchasesParser(): PurchasesListParser = PurchasesListParser()
}