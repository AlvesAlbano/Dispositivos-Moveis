package com.example.appespacoculturalkotlin.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdapterViewPager(fragmentActivity: FragmentActivity, var arr: ArrayList<Fragment>) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return arr[position]
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}
