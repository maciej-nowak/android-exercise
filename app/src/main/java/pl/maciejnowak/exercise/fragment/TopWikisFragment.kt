package pl.maciejnowak.exercise.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_top_wikis.*

import pl.maciejnowak.exercise.R
import pl.maciejnowak.exercise.adapter.TopWikisAdapter
import pl.maciejnowak.exercise.model.TopWiki

class TopWikisFragment : Fragment() {

    private val adapter: TopWikisAdapter by lazy { TopWikisAdapter(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_wikis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        mockData()
    }

    private fun setRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = this@TopWikisFragment.adapter
        }
    }

    private fun mockData() {
        val url = "https://avatars2.githubusercontent.com/u/1171011?s=200&v=4"
        val list = mutableListOf<TopWiki>().apply {
            add(TopWiki("Title 1", url, 100))
            add(TopWiki("Title 2", url, 200))
            add(TopWiki("Title 3", url, 300))
            add(TopWiki("Title 4", url, 400))
            add(TopWiki("Title 5", url, 500))
        }
        adapter.update(list)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TopWikisFragment()
    }
}
