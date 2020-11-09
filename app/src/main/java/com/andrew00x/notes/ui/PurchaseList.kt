package com.andrew00x.notes.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.andrew00x.notes.R
import com.andrew00x.notes.Purchase

class PurchaseList(
        context: Context,
        objects: List<ListItem<Purchase>>,
        private val itemSelectListener: ItemSelectListener<Purchase>?,
        private val itemClickListener: ItemClickListener<Purchase>? = null
) : ArrayAdapter<ListItem<Purchase>>(context, 0, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowViewHolder: PurchaseItemHolder
        val rowView: View
        if (convertView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.item_row_purchase, null)
            rowViewHolder = createViewHolder(rowView)
            rowView.tag = rowViewHolder
        } else {
            rowView = convertView
            rowViewHolder = rowView.tag as PurchaseItemHolder
        }
        val item = getItem(position)
        rowViewHolder.checkbox.tag = item
        rowViewHolder.title.tag = item
        setupRowView(item, rowViewHolder)
        return rowView
    }

    private fun createViewHolder(rowView: View): PurchaseItemHolder {
        val rowViewHolder = PurchaseItemHolder(
                rowView.findViewById(R.id.purchase_selector),
                rowView.findViewById(R.id.purchase_title),
                rowView.findViewById(R.id.purchase_details)
        )
        rowViewHolder.checkbox.setOnCheckedChangeListener { checkBoxView, isChecked ->
            val item = checkBoxView.tag as ListItem<Purchase>
            item.selected = isChecked
            itemSelectListener?.onSelectionChanged(item)
        }
        rowViewHolder.title.setOnClickListener { textView: View ->
            val item = textView.tag as ListItem<Purchase>
            itemClickListener?.onItemClick(item)
        }
        return rowViewHolder
    }

    private fun setupRowView(item: ListItem<Purchase>, rowViewHolder: PurchaseItemHolder) {
        rowViewHolder.checkbox.isChecked = item.selected
        rowViewHolder.title.text = item.data.title
        rowViewHolder.details.text = item.data.details
    }
}

data class PurchaseItemHolder(val checkbox: CheckBox, val title: TextView, val details: TextView)
