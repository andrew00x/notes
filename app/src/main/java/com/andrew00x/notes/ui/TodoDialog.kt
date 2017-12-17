package com.andrew00x.notes.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.widget.*
import com.andrew00x.notes.Period
import com.andrew00x.notes.R
import com.andrew00x.notes.Todo
import java.util.*

const val TODO_ARG = "todo"

class TodoDialog : DialogFragment() {

    companion object {
        fun newInstance(todo: Todo): TodoDialog {
            val dialog = TodoDialog()
            val arguments = Bundle()
            arguments.putParcelable(TODO_ARG, todo)
            dialog.arguments = arguments
            return dialog
        }
    }

    private lateinit var todo: Todo

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        todo = arguments.getParcelable(TODO_ARG)

        val todoDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_todo_item,null)

        val todoText = todoDialogView.findViewById<EditText>(R.id.todo_text)

        val todoPeriod = todoDialogView.findViewById<Spinner>(R.id.todo_period)
        val todoPeriodAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, Period.values())
        todoPeriodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        todoPeriod.adapter = todoPeriodAdapter

        val todoDate = todoDialogView.findViewById<DatePicker>(R.id.todo_date)

        val todoTime = todoDialogView.findViewById<TimePicker>(R.id.todo_time)
        todoTime.setIs24HourView(true)

        updateDialog(todoText, todoPeriod, todoDate, todoTime)

        return AlertDialog.Builder(activity, R.style.AppTheme_Dialog_Alert)
                .setView(todoDialogView)
                .setPositiveButton(R.string.ok) { _, _ ->
                    updateTodo(todoText, todoPeriod, todoDate, todoTime)
                    val intent = Intent()
                    intent.putExtra(TODO_ARG, todo)
                    targetFragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .create()
    }

    private fun updateDialog(todoText: EditText, todoPeriod: Spinner, todoDate: DatePicker, todoTime: TimePicker) {
        todoText.setText(todo.title, TextView.BufferType.EDITABLE)
        todoPeriod.setSelection(todo.period.ordinal)
        val calendar = Calendar.getInstance()
        calendar.time = todo.timestamp
        todoDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        todoTime.hour = calendar.get(Calendar.HOUR_OF_DAY)
        todoTime.minute = calendar.get(Calendar.HOUR_OF_DAY)
        todoTime.hour = calendar.get(Calendar.MINUTE)
    }

    private fun updateTodo(todoText: EditText, todoPeriod: Spinner, todoDate: DatePicker, todoTime: TimePicker) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, todoDate.year)
        calendar.set(Calendar.MONTH, todoDate.month)
        calendar.set(Calendar.DAY_OF_MONTH, todoDate.dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, todoTime.hour)
        calendar.set(Calendar.MINUTE, todoTime.minute)
        todo.title = todoText.text.toString()
        todo.period = Period.valueOf((todoPeriod.selectedView as TextView).text.toString())
        todo.timestamp = calendar.time
    }
}