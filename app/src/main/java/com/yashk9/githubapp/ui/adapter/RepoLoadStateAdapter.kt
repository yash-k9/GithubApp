package com.yashk9.githubapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yashk9.githubapp.databinding.LayoutLoadingStateBinding

//LoadStateAdapter to add the state at the footer for Paging Data
class RepoLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<RepoLoadStateAdapter.RepoLoadStateViewHolder>() {
    class RepoLoadStateViewHolder(val binding: LayoutLoadingStateBinding, retry: () -> Unit): RecyclerView.ViewHolder(binding.root){
        init {
            binding.retryButton.setOnClickListener {
                retry.invoke()
            }
        }
    }

    override fun onBindViewHolder(holder: RepoLoadStateViewHolder, loadState: LoadState) {
        val binding = holder.binding
        if(loadState is LoadState.Error){
           binding.errorMsg.text = loadState.error.message
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState !is LoadState.Loading
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RepoLoadStateViewHolder {
        val binding = LayoutLoadingStateBinding.inflate(LayoutInflater.from(parent.context))
        return RepoLoadStateViewHolder(binding, retry = retry)
    }

}
