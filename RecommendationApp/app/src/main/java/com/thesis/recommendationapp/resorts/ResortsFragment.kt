package com.thesis.recommendationapp.resorts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thesis.recommendationapp.R

class ResortsFragment : Fragment() {

    companion object {
        fun newInstance() = ResortsFragment()
    }

    private lateinit var viewModel: ResortsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ResortsViewModel::class.java)

        return inflater.inflate(R.layout.resorts_fragment, container, false)
    }

}