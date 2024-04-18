package com.ubaya.hobbyapp.view

import android.os.Bundle
import android.util.Log
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
    private lateinit var binding: FragmentNewsBinding
    private lateinit var viewModel: ListViewModel
    private val newsAdapter = NewsAdapter(arrayListOf())

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

        binding.refreshLayout.setOnRefreshListener {
            binding.recView.visibility = View.GONE
            binding.txtError.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            viewModel.refresh()
            binding.refreshLayout.isRefreshing = false
        }

        observeViewModel()
    }

    fun observeViewModel(){
        viewModel.newsLD.observe(viewLifecycleOwner, Observer {
            newsAdapter.updateNewsList(it)
        })

        viewModel.newsLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                binding.txtError.visibility = View.VISIBLE
            }
            else
            {
                binding.txtError.visibility = View.GONE
            }
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                binding.recView.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }
            else
            {
                binding.recView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        })
    }
}