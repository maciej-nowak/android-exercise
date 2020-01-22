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
import pl.maciejnowak.exercise.adapter.TopWikisAdapter
import pl.maciejnowak.exercise.interactor.TopWikisInteractorImpl
import pl.maciejnowak.exercise.model.TopWiki
import pl.maciejnowak.exercise.viewmodel.TopWikisViewModel
import pl.maciejnowak.exercise.viewmodel.TopWikisViewModelFactory

class TopWikisFragment : Fragment() {

    private lateinit var viewModel: TopWikisViewModel
    private val adapter: TopWikisAdapter by lazy { TopWikisAdapter(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_wikis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        observeViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this,
            TopWikisViewModelFactory(TopWikisInteractorImpl())
        ).get(TopWikisViewModel::class.java)
        if(viewModel.getItems().value.isNullOrEmpty()) {
            viewModel.loadTopWikis()
        }
    }

    private fun observeViewModel() {
        viewModel.getItems().observe(viewLifecycleOwner, Observer { render(it) })
        viewModel.getIsLoading().observe(viewLifecycleOwner, Observer { renderLoading(it) })
        viewModel.getError().observe(viewLifecycleOwner, Observer { renderError(it) })
    }

    private fun render(items: List<TopWiki>) {
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

    private fun setRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = this@TopWikisFragment.adapter
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
        fun newInstance() = TopWikisFragment()
    }
}
