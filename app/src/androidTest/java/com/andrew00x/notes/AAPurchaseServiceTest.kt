package com.andrew00x.notes

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.activeandroid.query.Select
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AAPurchaseServiceTest {
    companion object {
        lateinit var dbHelper: SQLiteOpenHelper
        lateinit var service: AAPurchaseService

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
            service = AAPurchaseService()
        }
    }

    @After
    fun tearDown() {
        dbHelper.writableDatabase.execSQL("delete from purchase")
    }

    @Test
    fun savesPurchase() {
        val purchase = Purchase("bread")
        service.save(purchase)
        val result = Select().from(Purchase::class.java).where("title = ?", "bread").execute<Purchase>()
        assertEquals(1, result.size)
        assertEquals(purchase, result[0])
    }

    @Test
    fun savesPurchaseWithDetails() {
        val purchase = Purchase("meat", "2 kg")

        service.save(purchase)

        val purchaseResult = Select().from(Purchase::class.java).where("title = ? and details = ?", "meat", "2 kg").execute<Purchase>()
        assertEquals(1, purchaseResult.size)
        assertEquals(purchase, purchaseResult[0])
    }

    @Test
    fun deletesPurchase() {
        val purchase = Purchase("milk")
        val id = purchase.save()

        service.delete(purchase)

        val result = Select().from(Purchase::class.java).where("id = ?", id).count()
        assertEquals(0, result)
    }

    @Test
    fun deletesAllPurchases() {
        val purchases = listOf(
                Purchase("iPhone"),
                Purchase("iPad"),
                Purchase("MacBook Pro"))
        purchases.forEach({ it.save() })

        var purchaseResult = Select().from(Purchase::class.java).count()
        assertEquals(3, purchaseResult)

        service.deleteAll()

        purchaseResult = Select().from(Purchase::class.java).count()
        assertEquals(0, purchaseResult)
    }

    @Test
    fun retrievesAllPurchases() {
        val purchases = listOf(
                Purchase("iPhone"),
                Purchase("iPad"),
                Purchase("MacBook Pro")).sortedBy { it.title }
        purchases.forEach({ it.save() })

        val result = service.retrieveAll().toList().sortedBy { it.title }

        assertEquals(purchases, result)
    }
}