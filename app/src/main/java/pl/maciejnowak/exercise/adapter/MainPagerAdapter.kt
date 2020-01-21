package pl.maciejnowak.exercise.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import pl.maciejnowak.exercise.fragment.TopArticlesFragment
import pl.maciejnowak.exercise.fragment.TopWikisFragment

class MainPagerAdapter(manager: FragmentManager)
    : FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> TopWikisFragment.newInstance()
            1 -> TopArticlesFragment.newInstance()
            else -> TopWikisFragment.newInstance() //put some default, should never happen
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "Wikis"
            1 -> "Articles"
            else -> null
        }
    }
}