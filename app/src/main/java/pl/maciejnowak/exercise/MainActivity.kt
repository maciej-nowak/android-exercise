package pl.maciejnowak.exercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import pl.maciejnowak.exercise.adapter.MainPagerAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTabs()
    }

    private fun setTabs() {
        view_pager.apply {
            adapter = MainPagerAdapter(supportFragmentManager)
            offscreenPageLimit = 2
        }
        tab_layout.apply {
            setupWithViewPager(view_pager)
            getTabAt(0)?.select()
        }
    }
}
