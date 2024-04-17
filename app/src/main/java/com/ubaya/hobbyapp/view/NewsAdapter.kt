package com.ubaya.hobbyapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.hobbyapp.databinding.FragmentNewsBinding
import com.ubaya.hobbyapp.databinding.NewsListItemBinding
import com.ubaya.hobbyapp.model.News

class NewsAdapter(val newsList:ArrayList<News>)
    :RecyclerView.Adapter<NewsAdapter.NewsViewHolder>()
{
    class NewsViewHolder(var binding: NewsListItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        with(holder.binding)
        {
            MainActivity.load_pict(holder.itemView, newsList[position].images.toString(), imageView)
            txtTitle.text = newsList[position].title
            txtWriter.text = "@${newsList[position].writer}"
            txtContent.text = newsList[position].content

            btnRead.setOnClickListener{
                val action = NewsFragmentDirections.actionNewsDetailFragment(newsList[position].id.toString().toInt())
                Navigation.findNavController(it).navigate(action)
            }
        }
    }

    fun updateNewsList(newNewsList:ArrayList<News>){
        newsList.clear()
        newsList.addAll(newNewsList)
        notifyDataSetChanged()
    }
}