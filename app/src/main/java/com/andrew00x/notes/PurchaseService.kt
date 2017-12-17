package com.andrew00x.notes

interface PurchaseService {
    fun save(purchase: Purchase): Purchase

    fun delete(purchase: Purchase)

    fun retrieveAll(): Iterable<Purchase>

    fun deleteAll()
}
