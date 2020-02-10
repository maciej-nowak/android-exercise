package pl.maciejnowak.exercise.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_top_wikis.*
import kotlinx.android.synthetic.main.layout_empty.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_progress_bar.*
import org.koin.android.viewmodel.ext.android.viewModel

import pl.maciejnowak.exercise.R
import pl.maciejnowak.exercise.ui.adapter.TopWikisAdapter
import pl.maciejnowak.commonobjects.entities.TopWiki
import pl.maciejnowak.exercise.ui.viewmodel.TopWikisViewModel
import pl.maciejnowak.repositories.model.TopWikisResult

class TopWikisFragment : Fragment() {

    private val viewModel: TopWikisViewModel by viewModel()
    private val adapter: TopWikisAdapter by lazy { TopWikisAdapter(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_wikis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setRecyclerView()
        error_button.setOnClickListener { viewModel.loadTopWikis() }
        swipe_refresh.setOnRefreshListener { viewModel.loadTopWikis(true) }
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner, Observer { render(it) })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { renderLoading(it) })
    }

    private fun render(result: TopWikisResult) {
        when(result) {
            is TopWikisResult.Success -> { renderSuccess(result.list) }
            is TopWikisResult.Error -> { renderError() }
            is TopWikisResult.ErrorRefresh -> { renderErrorRefresh() }
        }
    }

    private fun renderLoading(isLoading: Boolean) {
        if(isLoading) {
            if(viewModel.isSuccess()) {
                swipe_refresh.isRefreshing = true
            } else {
                error_container.visibility = View.GONE
                progressbar_container.visibility = View.VISIBLE
            }
        } else {
            progressbar_container.visibility = View.GONE
            swipe_refresh.isRefreshing = false
        }
    }

    private fun renderSuccess(items: List<TopWiki>) {
        error_container.visibility = View.GONE
        swipe_refresh.visibility = View.VISIBLE
        adapter.update(items)
        setItemsVisibility()
    }

    private fun renderError() {
        swipe_refresh.visibility = View.GONE
        empty_container.visibility = View.GONE
        error_container.visibility = View.VISIBLE
    }

    private fun renderErrorRefresh() {
        showMessage(R.string.refresh_data_failed)
    }

    private fun setRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = this@TopWikisFragment.adapter
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

    private fun showMessage(resource: Int) {
        Toast.makeText(context, resource, Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = TopWikisFragment()
    }
}
