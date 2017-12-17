package com.andrew00x.notes.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.andrew00x.notes.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pager = findViewById<ViewPager>(R.id.container)
        pager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return when(position) {
                    0 -> TodoFragment()
                    1 -> PurchaseFragment()
                    else -> throw IllegalArgumentException("Invalid position: $position")
                }
            }

            override fun getCount(): Int {
                return 2
            }

            override fun getPageTitle(position: Int): CharSequence {
                return when(position) {
                    0 -> resources.getString(R.string.todo_tab_title)
                    1 -> resources.getString(R.string.purchase_tab_title)
                    else -> throw IllegalArgumentException("Invalid position: $position")
                }
            }
        }
    }
}
