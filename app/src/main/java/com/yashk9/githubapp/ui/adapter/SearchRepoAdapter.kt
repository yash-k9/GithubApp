package com.yashk9.githubapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yashk9.githubapp.R
import com.yashk9.githubapp.databinding.RepoListItemBinding
import com.yashk9.githubapp.model.Repo

class SearchRepoAdapter: RecyclerView.Adapter<SearchRepoAdapter.SearchRepoViewHolder>() {
    private val diffUtil = object : DiffUtil.ItemCallback<Repo>(){
        override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem.id == newItem.id
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    class SearchRepoViewHolder(val binding: RepoListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRepoViewHolder {
        val binding = RepoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchRepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchRepoViewHolder, position: Int) {
        val curr = differ.currentList[position]
        with(holder.binding){
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

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}