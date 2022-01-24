package com.yashk9.githubapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yashk9.githubapp.R
import com.yashk9.githubapp.databinding.RepoListItemBinding
import com.yashk9.githubapp.model.Repo

//Adapter for the HomeFragment - Handles data from Paging
class RepoAdapter: PagingDataAdapter<Repo, RecyclerView.ViewHolder>(diffUtil){
    class RepoViewHolder(val binding: RepoListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RepoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as RepoViewHolder).binding
        val curr = getItem(position)
        with(holder.binding){
            curr?.let{ curr ->
                user.text = curr.owner.login
                repo.text = curr.name
                description.text = curr.description
                language.text = curr.language
                stars.text = "${curr.stargazers_count}"
                Glide.with(holder.itemView.context)
                    .load(curr.owner.avatar_url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imageView)
            }

        }
    }

    companion object{
        private val diffUtil = object : DiffUtil.ItemCallback<Repo>(){
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

}