package pl.maciejnowak.exercise.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import pl.maciejnowak.exercise.R
import pl.maciejnowak.exercise.ui.adapter.MainPagerAdapter
import pl.maciejnowak.exercise.ui.fragment.TopArticlesFragment
import pl.maciejnowak.exercise.ui.fragment.TopWikisFragment

class MainActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setPagerAdapter()
        setTabs()
    }

    private fun setPagerAdapter() {
        pagerAdapter = MainPagerAdapter(supportFragmentManager).apply {
            addPage( { TopWikisFragment.newInstance() }, getString(R.string.wikis))
            addPage( { TopArticlesFragment.newInstance() }, getString(R.string.articles))
        }
    }

    private fun setTabs() {
        view_pager.apply {
            adapter = pagerAdapter
            offscreenPageLimit = 2
        }
        tab_layout.apply {
            setupWithViewPager(view_pager)
            getTabAt(0)?.select()
        }
    }
}
