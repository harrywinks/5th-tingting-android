package com.tingting.ver01.Matching

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TeamInfoPagerAdapter : FragmentPagerAdapter {

    private val list: ArrayList<FirstPagerFragment> = ArrayList();


    constructor(fragmentManager: FragmentManager) : super(fragmentManager) {

    }

    /*override fun getPageTitle(position: Int): CharSequence? {
        return list[position].title();
    }*/

    override fun getItem(position: Int): Fragment {
        return list.get(position)
    }

    override fun getCount(): Int {
        return list.size
    }

    fun addItem(info : FirstPagerFragment){
        list.add(info)
    }

}