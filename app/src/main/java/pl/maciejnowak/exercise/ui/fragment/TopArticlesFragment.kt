package pl.maciejnowak.exercise.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_top_wikis.*
import kotlinx.android.synthetic.main.layout_empty.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_progress_bar.*

import pl.maciejnowak.exercise.R
import pl.maciejnowak.exercise.ui.adapter.TopArticlesAdapter
import pl.maciejnowak.database.Database
import pl.maciejnowak.database.model.TopArticle
import pl.maciejnowak.network.Network
import pl.maciejnowak.repositories.mapper.TopArticleMapper
import pl.maciejnowak.repositories.ArticleRepository
import pl.maciejnowak.exercise.ui.viewmodel.TopArticlesViewModel
import pl.maciejnowak.exercise.ui.viewmodel.TopArticlesViewModelFactory
import pl.maciejnowak.repositories.model.TopArticlesResult

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
        swipe_refresh.setOnRefreshListener { viewModel.loadTopArticles(true) }
    }

    private fun setRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = this@TopArticlesFragment.adapter
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this,
            TopArticlesViewModelFactory(ArticleRepository(Network.fandomService, Database.articleDao, TopArticleMapper()))
        ).get(TopArticlesViewModel::class.java)
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner, Observer { render(it) })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { renderLoading(it) })
    }

    private fun render(result: TopArticlesResult) {
        when(result) {
            is TopArticlesResult.Success -> { renderSuccess(result.list) }
            is TopArticlesResult.Error -> { renderError() }
            is TopArticlesResult.ErrorRefresh -> { renderErrorRefresh() }
        }
    }

    private fun renderSuccess(items: List<TopArticle>) {
        error_container.visibility = View.GONE
        swipe_refresh.visibility = View.VISIBLE
        adapter.update(items)
        setItemsVisibility()
    }

    private fun renderLoading(isLoading: Boolean) {
        if(isLoading) {
            error_container.visibility = View.GONE
            if(viewModel.result.value is TopArticlesResult.Success) {
                swipe_refresh.isRefreshing = true
            } else {
                empty_container.visibility = View.GONE
                progressbar_container.visibility = View.VISIBLE
            }
        } else {
            progressbar_container.visibility = View.GONE
            swipe_refresh.isRefreshing = false
        }
    }

    private fun renderError() {
        swipe_refresh.visibility = View.GONE
        empty_container.visibility = View.GONE
        error_container.visibility = View.VISIBLE
    }

    private fun renderErrorRefresh() {
        showMessage(R.string.refresh_data_failed)
    }

    private fun setItemsVisibility() {
        adapter.run {
            if(itemCount > 0) {
                empty_container.visibility = View.GONE
                recycler_view.visibility = View.VISIBLE
            } else {
                empty_container.visibility = View.VISIBLE
                recycler_view.visibility = View.GONE
            }
        }
    }

    private fun showMessage(resource: Int) {
        Toast.makeText(context, resource, Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = TopArticlesFragment()
    }
}
