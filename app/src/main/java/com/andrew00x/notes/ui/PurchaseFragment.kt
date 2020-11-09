package com.andrew00x.notes.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.andrew00x.notes.*
import javax.inject.Inject

private const val ADD_PURCHASE = 1
private const val UPDATE_PURCHASE = 2
private const val IMPORT_PURCHASES = 3

class PurchaseFragment : Fragment() {
    lateinit var service: PurchaseService
        @Inject set
    lateinit var purchasesParser: PurchasesListParser
        @Inject set
    private lateinit var items: MutableList<ListItem<Purchase>>
    private lateinit var itemsAdapter: ArrayAdapter<ListItem<Purchase>>
    private lateinit var deleteButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as NotesApplication).component.injectInto(this)
        items = service.retrieveAll().map { ListItem(it, it.done) }.toMutableList()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_purchases, container, false)!!

        val addButton = view.findViewById<View>(R.id.add_purchase)
        addButton.setOnClickListener { openAddPurchaseDialog() }

        deleteButton = view.findViewById(R.id.delete_purchase)
        deleteButton.setOnClickListener { deleteSelectedItems() }
        deleteButton.isEnabled = false

        val deleteAll = view.findViewById<View>(R.id.clear_all_purchases)
        deleteAll.setOnClickListener { deleteAllItems() }

        val importBtn = view.findViewById<View>(R.id.import_purchases)
        importBtn.setOnClickListener { openImportPurchasesDialog() }

        val listView = view.findViewById<ListView>(R.id.purchase_list)
        itemsAdapter = PurchaseList(
                activity,
                items,
                object : ItemSelectListener<Purchase> {
                    override fun onSelectionChanged(item: ListItem<Purchase>) {
                        val purchase = item.data
                        purchase.done = item.selected
                        service.save(purchase)
                        deleteButton.isEnabled = items.count { it.selected } > 0
                    }
                },
                object : ItemClickListener<Purchase> {
                    override fun onItemClick(item: ListItem<Purchase>) {
                        openUpdatePurchaseDialog(item.data)
                    }
                }
        )
        listView.adapter = itemsAdapter
        return view
    }

    private fun openAddPurchaseDialog() {
        val dialog = PurchaseDialog.newInstance(Purchase())
        dialog.setTargetFragment(this, ADD_PURCHASE)
        dialog.show(fragmentManager, "purchase_dialog")
    }

    private fun openUpdatePurchaseDialog(purchase: Purchase) {
        val updateTodoDialog = PurchaseDialog.newInstance(purchase)
        updateTodoDialog.setTargetFragment(this, UPDATE_PURCHASE)
        updateTodoDialog.show(fragmentManager, "purchase_dialog")
    }

    private fun openImportPurchasesDialog() {
        val dialog = PurchaseImportDialog()
        dialog.setTargetFragment(this, IMPORT_PURCHASES)
        dialog.show(fragmentManager, "purchase_import_dialog")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ADD_PURCHASE -> {
                    val purchase = data?.getParcelableExtra<Purchase>(PURCHASE_ARG)
                    if (purchase != null) {
                        service.save(purchase)
                        itemsAdapter.add(ListItem(purchase))
                    }
                }
                UPDATE_PURCHASE -> {
                    val purchase = data?.getParcelableExtra<Purchase>(PURCHASE_ARG)
                    if (purchase != null) {
                        service.save(purchase)
                    }
                }
                IMPORT_PURCHASES -> {
                    val purchasesText = data?.getStringExtra(PURCHASE_IMPORT_TEXT)
                    if (purchasesText != null) {
                        val purchases = purchasesParser.parse(purchasesText)
                        purchases.forEach { service.save(it) }
                        itemsAdapter.addAll(purchases.map { ListItem(it) })
                    }
                }
            }
        }
    }

    private fun deleteSelectedItems() {
        val selected = items.filter { it.selected }
        selected.forEach { service.delete(it.data) }
        selected.forEach { itemsAdapter.remove(it) }
        deleteButton.isEnabled = false
    }

    private fun deleteAllItems() {
        service.deleteAll()
        itemsAdapter.clear()
        deleteButton.isEnabled = false
    }
}