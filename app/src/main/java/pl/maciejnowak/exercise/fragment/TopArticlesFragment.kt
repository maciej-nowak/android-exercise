package pl.maciejnowak.exercise.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_top_wikis.*

import pl.maciejnowak.exercise.R
import pl.maciejnowak.exercise.adapter.TopArticlesAdapter
import pl.maciejnowak.exercise.model.TopArticle

class TopArticlesFragment : Fragment() {

    private val adapter: TopArticlesAdapter by lazy { TopArticlesAdapter(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        mockData()
    }

    private fun setRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = this@TopArticlesFragment.adapter
        }
    }

    private fun mockData() {
        val list = mutableListOf<TopArticle>().apply {
            add(TopArticle("Title 1", "User 1", 1578618000000))
            add(TopArticle("Title 2", "User 2", 1578704400000))
            add(TopArticle("Title 3", "User 3", 1578790800000))
            add(TopArticle("Title 4", "User 4", 1578877200000))
            add(TopArticle("Title 5", "User 5", 1578963600000))
        }
        adapter.update(list)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TopArticlesFragment()
    }
}
