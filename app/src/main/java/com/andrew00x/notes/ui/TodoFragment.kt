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

private const val ADD_TODO = 1
private const val UPDATE_TODO = 2

class TodoFragment : Fragment() {
    lateinit var service: TodoService
        @Inject set
    private lateinit var items: MutableList<ListItem<Todo>>
    private lateinit var itemsAdapter: ArrayAdapter<ListItem<Todo>>
    private lateinit var doneButton: View
    private lateinit var deleteButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as NotesApplication).component.injectInto(this)
        items = service.retrieveAll().map { ListItem(it) }.toMutableList()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_todo, container, false)!!

        val addButton = view.findViewById<View>(R.id.add_todo)
        addButton.setOnClickListener { _: View -> openAddTodoDialog() }

        doneButton = view.findViewById<View>(R.id.done_todo)
        doneButton.setOnClickListener { _: View -> makeDoneSelectedItems() }
        doneButton.isEnabled = false

        deleteButton = view.findViewById<View>(R.id.delete_todo)
        deleteButton.setOnClickListener { _: View -> deleteSelectedItems() }
        deleteButton.isEnabled = false

        val listView = view.findViewById<ListView>(R.id.todo_list)
        val itemsAdapter = TodoList(
                activity,
                items,
                object : ItemSelectListener<Todo> {
                    override fun onSelectionChanged(item: ListItem<Todo>) {
                        val countSelected = items.count { it.selected }
                        doneButton.isEnabled = countSelected > 0
                        deleteButton.isEnabled = countSelected > 0
                    }
                },
                object : ItemClickListener<Todo> {
                    override fun onItemClick(item: ListItem<Todo>) {
                        openUpdateTodoDialog(item.data)
                    }
                }
        )
        this.itemsAdapter = itemsAdapter
        sortItems()
        listView.adapter = itemsAdapter
        return view
    }

    private fun openAddTodoDialog() {
        val addTodoDialog = TodoDialog.newInstance(Todo())
        addTodoDialog.setTargetFragment(this, ADD_TODO)
        addTodoDialog.show(fragmentManager, "todo_dialog")
    }

    private fun openUpdateTodoDialog(todo: Todo) {
        val updateTodoDialog = TodoDialog.newInstance(todo)
        updateTodoDialog.setTargetFragment(this, UPDATE_TODO)
        updateTodoDialog.show(fragmentManager, "todo_dialog")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ADD_TODO-> {
                    val todo = data?.getParcelableExtra<Todo>(TODO_ARG)
                    if (todo != null) {
                        service.save(todo)
                        items.add(ListItem(todo))
                        sortItems()
                    }
                }
                UPDATE_TODO -> {
                    val todo = data?.getParcelableExtra<Todo>(TODO_ARG)
                    if (todo != null) {
                        service.save(todo)
                        sortItems()
                    }
                }
            }
        }
    }

    private fun makeDoneSelectedItems() {
        val (forRemove, forUpdate) = items.filter({ it.selected }).partition { it.data.period == Period.NONE }

        forRemove.forEach { service.delete(it.data) }
        items.removeAll(forRemove)

        forUpdate.forEach {
            it.selected = false
            val todo = it.data
            val time = todo.timestamp
            todo.timestamp = todo.period.calculateNext(time)
            service.save(todo)
        }
        doneButton.isEnabled = false
        deleteButton.isEnabled = false
        sortItems()
    }

    private fun deleteSelectedItems() {
        val selected = items.filter { it.selected }
        selected.forEach({ service.delete(it.data) })
        items.removeAll(selected)
        doneButton.isEnabled = false
        deleteButton.isEnabled = false
        sortItems()
    }

    private fun sortItems() {
        itemsAdapter.sort { o1, o2 -> o1.data.timestamp.compareTo(o2.data.timestamp) }
    }
}