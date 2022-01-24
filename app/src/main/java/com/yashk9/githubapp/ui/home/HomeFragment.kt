package com.yashk9.githubapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.yashk9.githubapp.R
import com.yashk9.githubapp.databinding.FragmentHomeBinding
import com.yashk9.githubapp.ui.adapter.RepoAdapter
import com.yashk9.githubapp.ui.adapter.RepoLoadStateAdapter
import com.yashk9.githubapp.ui.viewmodel.RepoViewModel
import com.yashk9.githubapp.util.show
import kotlinx.coroutines.flow.collect

@ExperimentalPagingApi
class HomeFragment : Fragment(){
    private lateinit var binding: FragmentHomeBinding
    val viewModel: RepoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    private fun initViews() {
        //Set Paging Adapter
        val adapter = RepoAdapter()
        binding.recycler.adapter = adapter.withLoadStateFooter(
            footer = RepoLoadStateAdapter { adapter.retry() }
        )
        binding.recycler.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        lifecycleScope.launchWhenCreated {
            viewModel.offlineCache.collect {
                adapter.submitData(it)
                binding.swipeLayout.isRefreshing = false
            }
        }


        adapter.addLoadStateListener { loadState ->
            binding.recycler.isVisible =
                loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
            binding.retry.isVisible =
                loadState.mediator?.refresh is LoadState.Error && adapter.itemCount == 0
        }

        binding.swipeLayout.setOnRefreshListener {
            adapter.refresh()
        }

        binding.retry.setOnClickListener {
            adapter.retry()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.search -> {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showEmptyList(isEmpty: Boolean) {
        if(isEmpty){
            with(binding){
                recycler.isVisible = false
                progressBar.isVisible = false
                retry.isVisible = true
            }
        }else{
            with(binding){
                recycler.isVisible = true
                progressBar.isVisible = true
                retry.isVisible = false
            }
        }

    }

}