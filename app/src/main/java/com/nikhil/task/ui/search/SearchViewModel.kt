package com.nikhil.task.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.task.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
): ViewModel() {

    val searchLiveData get() = searchRepository.searchData


    fun getSearchQueryResults(query: String) {
        viewModelScope.launch {
            searchRepository.getSearchResults(query)
        }
    }
}