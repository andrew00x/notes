package com.andrew00x.notes.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import com.andrew00x.notes.Purchase
import com.andrew00x.notes.R

const val PURCHASE_ARG = "purchase"

class PurchaseDialog : DialogFragment() {
    companion object {
        fun newInstance(purchase: Purchase): PurchaseDialog {
            val dialog = PurchaseDialog()
            val arguments = Bundle()
            arguments.putParcelable(PURCHASE_ARG, purchase)
            dialog.arguments = arguments
            return dialog
        }
    }

    private lateinit var purchase: Purchase

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        purchase = arguments.getParcelable(PURCHASE_ARG)

        val purchaseDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_purchase_item, null)
        val title = purchaseDialogView.findViewById<EditText>(R.id.purchase_title)
        val details = purchaseDialogView.findViewById<EditText>(R.id.purchase_details)
        updateDialog(title, details)

        return AlertDialog.Builder(activity)
                .setView(purchaseDialogView)
                .setPositiveButton(R.string.ok, {_, _ ->
                    updatePurchase(title, details)
                    val intent = Intent()
                    intent.putExtra(PURCHASE_ARG, purchase)
                    targetFragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
                })
                .setNegativeButton(R.string.cancel, {_, _ ->})
                .create()
    }

    private fun updateDialog(title: TextView, details: TextView) {
        title.text = purchase.title
        details.text = purchase.details
    }

    private fun updatePurchase(title: TextView, details: TextView) {
        purchase.title = title.text.toString()
        purchase.details = details.text.toString()
    }
}