package com.yashk9.githubapp.ui.search

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yashk9.githubapp.R
import com.yashk9.githubapp.databinding.FragmentSearchBinding
import com.yashk9.githubapp.model.Repo
import com.yashk9.githubapp.ui.adapter.SearchRepoAdapter
import com.yashk9.githubapp.ui.viewmodel.RepoViewModel
import com.yashk9.githubapp.util.hide
import com.yashk9.githubapp.util.hideKeyboard
import com.yashk9.githubapp.util.show
import com.yashk9.githubapp.util.viewState.ViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.Job

@ExperimentalCoroutinesApi
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter:SearchRepoAdapter
    var job: Job? = null
    val viewModel: RepoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    private fun initView() {
        binding.emptyText.show()
        adapter = SearchRepoAdapter()
        binding.searchRecycler.adapter = adapter
        binding.searchRecycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    //Observe State
    private fun observeData() {
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
           repeatOnLifecycle(Lifecycle.State.STARTED){
               viewModel.uiState.collect {
                   when(it){
                       is ViewState.Empty -> showEmptyMessage()
                       is ViewState.Error -> showErrorMessage()
                       is ViewState.Loading -> showLoadingMessage()
                       is ViewState.Success -> showList(it.data)
                   }
               }
           }
       }
    }

    @ExperimentalCoroutinesApi
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_view_bar, menu)
        val searchItem = menu.findItem(R.id.search_view_item)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search Repositories"
        searchView.setIconifiedByDefault(false)
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                //cancel the current running job if exists
                job?.cancel()
                val job = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    query?.let {
                        viewModel.queryRepo(it)
                        binding.root.hideKeyboard()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    /***************** Helper Functions *****************/

    private fun showList(data: List<Repo>) {
        with(binding){
            searchProgress.hide()
            searchRecycler.show()
            emptyText.hide()
        }
        adapter.differ.submitList(data)
    }

    private fun showLoadingMessage() {
        with(binding){
            searchProgress.show()
            searchRecycler.hide()
            emptyText.hide()
        }
    }

    private fun showErrorMessage() {
        with(binding){
            searchProgress.hide()
            searchRecycler.hide()
            emptyText.show()
        }
    }

    private fun showEmptyMessage() {
        with(binding){
            searchProgress.hide()
            searchRecycler.hide()
            emptyText.show()
        }
    }
}