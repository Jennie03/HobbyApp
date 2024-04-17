package com.ubaya.hobbyapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubaya.hobbyapp.R
import com.ubaya.hobbyapp.databinding.FragmentNewsBinding
import com.ubaya.hobbyapp.viewmodel.ListViewModel

class NewsFragment : Fragment() {
    private lateinit var viewModel: ListViewModel
    private val newsAdapter = NewsAdapter(arrayListOf())
    private lateinit var binding: FragmentNewsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.refresh()

        binding.recView.layoutManager = LinearLayoutManager(context)
        binding.recView.adapter = newsAdapter

        observeViewModel()
    }

    fun observeViewModel(){
        viewModel.newsLD.observe(viewLifecycleOwner, Observer {
            newsAdapter.updateNewsList(it)
        })

        viewModel.newsLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                binding.txtError?.visibility = View.VISIBLE
            }
            else
            {
                binding.txtError?.visibility = View.GONE
            }
        })
    }
}