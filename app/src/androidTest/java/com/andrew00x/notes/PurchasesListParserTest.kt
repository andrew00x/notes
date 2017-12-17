package com.andrew00x.notes

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PurchasesListParserTest {
    private val parser: PurchasesListParser = PurchasesListParser()

    @Test
    fun parsesPurchaseStringSeparatedByCommas() {
        val str = "bread,milk, meat"
        val expected = listOf("bread", "milk", "meat").map { Purchase(it) }
        assertEquals(expected, parser.parse(str))
    }

    @Test
    fun parsesPurchaseStringSeparatedByNewLines() {
        val str = "bread\nmilk\nmeat"
        val expected = listOf("bread", "milk", "meat").map { Purchase(it) }
        assertEquals(expected, parser.parse(str))
    }

    @Test
    fun parsesPurchaseStringSeparatedByNewLinesAndComma() {
        val str = "bread\nmilk,\nmeat,tomatoes"
        val expected = listOf("bread", "milk", "meat", "tomatoes").map { Purchase(it) }
        assertEquals(expected, parser.parse(str))
    }

    @Test
    fun parsesPurchaseStringSeparatedByCommasAndNewLinesAndContainedDetails() {
        val str = "bread:1\nmilk :1l,\nmeat:2 kg,  tomatoes:  1.5 kg"
        val expected = listOf(
                Purchase("bread", "1"),
                Purchase("milk", "1l"),
                Purchase("meat", "2 kg"),
                Purchase("tomatoes", "1.5 kg")
        )
        assertEquals(expected, parser.parse(str))
    }
}