package pl.maciejnowak.exercise.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MainPagerAdapter(manager: FragmentManager)
    : FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = mutableListOf<()->Fragment>()
    private val titles = mutableListOf<String>()

    override fun getItem(position: Int): Fragment {
        return fragments[position].invoke()
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    fun addPage(fragment: () -> Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }
}