package pl.maciejnowak.exercise.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_top_wikis.*

import pl.maciejnowak.exercise.R
import pl.maciejnowak.exercise.adapter.TopArticlesAdapter
import pl.maciejnowak.exercise.interactor.TopArticlesInteractorImpl
import pl.maciejnowak.exercise.model.TopArticle
import pl.maciejnowak.exercise.viewmodel.TopArticlesViewModel
import pl.maciejnowak.exercise.viewmodel.TopArticlesViewModelFactory

class TopArticlesFragment : Fragment() {

    private lateinit var viewModel: TopArticlesViewModel
    private val adapter: TopArticlesAdapter by lazy { TopArticlesAdapter(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = this@TopArticlesFragment.adapter
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this,
            TopArticlesViewModelFactory(TopArticlesInteractorImpl())
        ).get(TopArticlesViewModel::class.java)
        if(viewModel.getItems().value.isNullOrEmpty()) {
            viewModel.loadTopArticles()
        }
    }

    private fun observeViewModel() {
        viewModel.getItems().observe(viewLifecycleOwner, Observer { render(it) })
        //TODO add more observers
    }

    private fun render(items: List<TopArticle>) {
        adapter.update(items)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TopArticlesFragment()
    }
}
