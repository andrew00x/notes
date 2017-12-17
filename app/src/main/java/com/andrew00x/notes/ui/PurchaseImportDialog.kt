package com.andrew00x.notes.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.widget.EditText
import com.andrew00x.notes.R

const val PURCHASE_IMPORT_TEXT = "purchase_import_text"

class PurchaseImportDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val purchaseDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_purchase_import, null)
        val text = purchaseDialogView.findViewById<EditText>(R.id.purchase_import_text)

        return AlertDialog.Builder(activity, R.style.AppTheme_Dialog_Alert)
                .setView(purchaseDialogView)
                .setPositiveButton(R.string.ok, { _, _ ->
                    val intent = Intent()
                    intent.putExtra(PURCHASE_IMPORT_TEXT, text.text.toString())
                    targetFragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
                })
                .setNegativeButton(R.string.cancel, { _, _ -> })
                .create()
    }
}