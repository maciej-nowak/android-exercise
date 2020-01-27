package pl.maciejnowak.exercise.ui.fragment


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
import pl.maciejnowak.exercise.ui.viewmodel.model.Result
import pl.maciejnowak.exercise.ui.adapter.TopArticlesAdapter
import pl.maciejnowak.exercise.database.Database
import pl.maciejnowak.exercise.database.model.TopArticle
import pl.maciejnowak.exercise.network.Network
import pl.maciejnowak.exercise.ui.repository.ArticleRepository
import pl.maciejnowak.exercise.ui.viewmodel.TopArticlesViewModel
import pl.maciejnowak.exercise.ui.viewmodel.TopArticlesViewModelFactory

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
            TopArticlesViewModelFactory(ArticleRepository(Network.fandomService, Database.articleDao))
        ).get(TopArticlesViewModel::class.java)
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner, Observer { render(it) })
    }

    private fun render(result: Result<List<TopArticle>>) {
        when(result) {
            is pl.maciejnowak.exercise.Resource.Result.Success -> { result.data?.let { render(it) } }
            is pl.maciejnowak.exercise.Resource.Result.Loading -> { renderLoading(true) }
            is pl.maciejnowak.exercise.Resource.Result.Error -> { renderError(true) }
        }
    }

    private fun render(items: List<TopArticle>) {
        renderError(false)
        renderLoading(false)
        adapter.update(items)
        setItemsVisibility()
    }

    private fun renderLoading(isLoading: Boolean) {
        if(isLoading) {
            empty_container.visibility = View.GONE
            error_container.visibility = View.GONE
            progressbar_container.visibility = View.VISIBLE
        } else {
            progressbar_container.visibility = View.GONE
        }
    }

    private fun renderError(error: Boolean) {
        if(error) {
            progressbar_container.visibility = View.GONE
            empty_container.visibility = View.GONE
            error_container.visibility = View.VISIBLE
        } else {
            error_container.visibility = View.GONE
        }
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

    companion object {
        @JvmStatic
        fun newInstance() = TopArticlesFragment()
    }
}
