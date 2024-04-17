package com.ubaya.hobbyapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ubaya.hobbyapp.R
import com.ubaya.hobbyapp.databinding.FragmentNewsDetailBinding
import com.ubaya.hobbyapp.viewmodel.DetailViewModel
import com.ubaya.hobbyapp.viewmodel.ListViewModel

class NewsDetailFragment : Fragment() {
    private lateinit var binding: FragmentNewsDetailBinding
    private lateinit var viewModel: DetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =ViewModelProvider(this).get(DetailViewModel::class.java)
        if (arguments != null)
        {
            val id = NewsDetailFragmentArgs.fromBundle(requireArguments()).id
            viewModel.fetch(id)
        }
        observeViewModel()
    }

    fun observeViewModel(){
        viewModel.newsLD.observe(viewLifecycleOwner, Observer {
            with(binding){
                MainActivity.load_pict(requireView(), it.images.toString(), imgPict)
                var current_idx = 0
                txtTitle.text =it.title
                txtWriter.text = "@${it.writer}"
                val content_per_par = it.content?.split("\n")
                Log.d("cekdta", content_per_par.toString())
                val par_size =content_per_par?.size

                txtContent.text = content_per_par?.get(current_idx)
                currentPage(current_idx, par_size!!.toInt())

                btnNext.setOnClickListener{
                    current_idx++
                    txtContent.text = content_per_par[current_idx]
                    currentPage(current_idx, par_size.toInt())
                }

                btnPrev.setOnClickListener {
                    current_idx--
                    txtContent.text = content_per_par[current_idx]
                    currentPage(current_idx, par_size.toInt())
                }
            }
        })
    }

    fun currentPage(current_idx: Int, par_size: Int){
        with(binding){
            when(current_idx){
                0 -> {
                    btnPrev.isEnabled = false
                    btnNext.isEnabled = true
                }
                par_size.minus(1) -> {
                    btnPrev.isEnabled = true
                    btnNext.isEnabled = false
                }
                else -> {
                    btnPrev.isEnabled = true
                    btnNext.isEnabled = true
                }
            }
        }
    }
}