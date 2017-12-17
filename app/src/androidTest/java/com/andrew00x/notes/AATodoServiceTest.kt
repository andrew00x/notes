package com.andrew00x.notes

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.activeandroid.query.Select
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class AATodoServiceTest {
    companion object {
        lateinit var dbHelper: SQLiteOpenHelper
        lateinit var service: AATodoService

        @BeforeClass
        @JvmStatic
        fun setUp() {
            val context = InstrumentationRegistry.getTargetContext()
            dbHelper = object : SQLiteOpenHelper(context, "notes.db", null, 1) {
                override fun onCreate(db: SQLiteDatabase?) {
                }

                override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
                }
            }
            service = AATodoService()
        }
    }

    @After
    fun tearDown() {
        dbHelper.writableDatabase.execSQL("delete from todo")
    }

    @Test
    fun savesTodo() {
        val todo = Todo("test save")

        service.save(todo)

        val result = Select().from(Todo::class.java).where("title = ?", "test save").execute<Todo>()
        assertEquals(1, result.size)
        assertEquals(todo, result[0])
    }

    @Test
    fun findsTodoByTitle() {
        val todo = Todo("test find", Date(), Period.DAY)
        todo.save()

        val result = service.find("test find")
        assertEquals(1, result.count())
        assertEquals(todo, result.single())
    }

    @Test
    fun findsTodoByPartOfTitle() {
        val timestamp = Date()
        val todo = Todo("test find by part of title", timestamp, Period.DAY)
        todo.save()

        val result = service.find("part of title")
        assertEquals(1, result.count())
        assertEquals(todo, result.single())
    }

    @Test
    fun deletesTodo() {
        val todo = Todo("test delete")
        val id = todo.save()

        var result = Select().from(Todo::class.java).where("id = ?", id).count()
        assertEquals(1, result)

        service.delete(todo)

        result = Select().from(Todo::class.java).where("id = ?", id).count()
        assertEquals(0, result)
    }

    @Test
    fun deletesAllTodos() {
        repeat(7, {
            Todo("test delete $it").save()
        })

        var result = Select().from(Todo::class.java).count()
        assertEquals(7, result)

        service.deleteAll()

        result = Select().from(Todo::class.java).count()
        assertEquals(0, result)
    }

    @Test
    fun retrievesAllTodos() {
        val todos = mutableListOf<Todo>()
        repeat(7, {
            val todo = Todo("test retrieve $it")
            todo.save()
            todos.add(todo)
        })

        val result = service.retrieveAll().toList()

        assertEquals(todos.size, result.count())
        assertTrue(todos.containsAll(result))
    }
}