package com.andrew00x.notes

class PurchasesListParser {

    fun parse(str: String): List<Purchase> {
        return str.splitToSequence("\r\n", "\n", "\r", ",")
                .filter { !it.isEmpty() }
                .map { createPurchase(it.trim()) }
                .toList()
    }

    private fun createPurchase(str: String): Purchase {
        val delimiterPosition = str.indexOf(':')
        if (delimiterPosition > 0 && delimiterPosition < str.length - 1) {
             return Purchase(str.substring(0, delimiterPosition).trim(), str.substring(delimiterPosition + 1).trim())
        }
        return Purchase(str)
    }
}