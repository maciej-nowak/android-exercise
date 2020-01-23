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
import kotlinx.android.synthetic.main.layout_empty.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_progress_bar.*

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
        error_button.setOnClickListener { viewModel.loadTopArticles() }
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
        viewModel.getIsLoading().observe(viewLifecycleOwner, Observer { renderLoading(it) })
        viewModel.getError().observe(viewLifecycleOwner, Observer { renderError(it) })
    }

    private fun render(items: List<TopArticle>) {
        adapter.update(items)
        setItemsVisibility()
    }

    private fun renderLoading(isLoading: Boolean) {
        if(isLoading) {
            error_container.visibility = View.GONE
            progress_bar_container.visibility = View.VISIBLE
        } else {
            progress_bar_container.visibility = View.GONE
        }
    }

    private fun renderError(error: Boolean) {
        if(error) {
            error_container.visibility = View.VISIBLE
        } else {
            error_container.visibility = View.GONE
        }
    }

    private fun setItemsVisibility() {
        adapter.run {
            if(itemCount > 0) {
                empty_layout.visibility = View.GONE
                recycler_view.visibility = View.VISIBLE
            } else {
                empty_layout.visibility = View.VISIBLE
                recycler_view.visibility = View.GONE
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = TopArticlesFragment()
    }
}
