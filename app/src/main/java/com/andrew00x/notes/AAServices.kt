package com.andrew00x.notes

import com.activeandroid.Model
import com.activeandroid.query.Delete
import com.activeandroid.query.Select

abstract class BaseService<T : Model> {
    open fun save(obj: T): T {
        obj.save()
        return obj
    }

    fun delete(obj: T) {
        obj.delete()
    }

    fun retrieveAll(): Iterable<T> {
        return Select().from(myType()).execute()
    }

    fun deleteAll() {
        Delete().from(myType()).execute<T>()
    }

    protected abstract fun myType(): Class<T>
}

class AATodoService : BaseService<Todo>(), TodoService {
    override fun myType(): Class<Todo> {
        return Todo::class.java
    }

    override fun find(title: String): Iterable<Todo> {
        return Select().from(Todo::class.java).where("title like ?", "%$title%").execute()
    }
}

class AAPurchaseService : BaseService<Purchase>(), PurchaseService {
    override fun myType(): Class<Purchase> {
        return Purchase::class.java
    }
}