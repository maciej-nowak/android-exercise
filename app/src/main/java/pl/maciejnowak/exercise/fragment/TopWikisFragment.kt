package pl.maciejnowak.exercise.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import pl.maciejnowak.exercise.R

class TopWikisFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_wikis, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TopWikisFragment()
    }
}
