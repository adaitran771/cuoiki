package com.example.apiproject.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.apiproject.CanceledFragment
import com.example.apiproject.DeliveredFragment
import com.example.apiproject.PendingFragment
import com.example.apiproject.PickupFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        PendingFragment(),
        PickupFragment(),
        DeliveredFragment(),
        CanceledFragment()

    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
