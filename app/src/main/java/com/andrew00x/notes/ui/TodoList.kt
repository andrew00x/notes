package com.andrew00x.notes.ui

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.andrew00x.notes.R
import com.andrew00x.notes.Todo
import java.util.concurrent.TimeUnit

private const val WARN_THRESHOLD = 3
private const val URGENT_THRESHOLD = 1

class TodoList(
        context: Context,
        objects: List<ListItem<Todo>>,
        private val itemSelectListener: ItemSelectListener<Todo>? = null,
        private val itemClickListener: ItemClickListener<Todo>? = null
) : ArrayAdapter<ListItem<Todo>>(context, 0, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowViewHolder: TodoViewHolder
        val rowView: View
        if (convertView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.item_row_todo,null)
            rowViewHolder = createViewHolder(rowView)
            rowView.tag = rowViewHolder
        } else {
            rowView = convertView
            rowViewHolder = rowView.tag as TodoViewHolder
        }
        val item = getItem(position)
        rowViewHolder.checkbox.tag = item
        rowViewHolder.title.tag = item
        setupRowView(item, rowViewHolder)
        return rowView
    }

    private fun createViewHolder(rowView: View): TodoViewHolder {
        val rowViewHolder = TodoViewHolder(
                rowView.findViewById(R.id.todo_selected),
                rowView.findViewById(R.id.todo_title),
                rowView.findViewById(R.id.todo_date)
        )
        rowViewHolder.checkbox.setOnCheckedChangeListener { checkBoxView, isChecked ->
            val item = checkBoxView.tag as ListItem<Todo>
            item.selected = isChecked
            itemSelectListener?.onSelectionChanged(item)
        }
        rowViewHolder.title.setOnClickListener { textView: View ->
            val item = textView.tag as ListItem<Todo>
            itemClickListener?.onItemClick(item)
        }
        return rowViewHolder
    }

    private fun setupRowView(list: ListItem<Todo>, rowViewHolder: TodoViewHolder) {
        rowViewHolder.checkbox.isChecked = list.selected

        val todo = list.data
        rowViewHolder.title.text = todo.title
        rowViewHolder.date.text = DateFormat.format("dd.MM.yyyy HH:mm", todo.timestamp)

        val timeLeft = daysToDueDate(todo)
        when {
            timeLeft <= URGENT_THRESHOLD -> rowViewHolder.date.setBackgroundResource(R.drawable.todo_date_urgent_backgroud)
            timeLeft <= WARN_THRESHOLD -> rowViewHolder.date.setBackgroundResource(R.drawable.todo_date_warn_backgroud)
            else -> rowViewHolder.date.setBackgroundResource(0)
        }
    }

    private fun daysToDueDate(todo: Todo): Int {
        return TimeUnit.MILLISECONDS.toDays(todo.timestamp.time - System.currentTimeMillis()).toInt()
    }
}

data class TodoViewHolder(val checkbox: CheckBox, val title: TextView, val date: TextView)