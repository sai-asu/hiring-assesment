package com.example.fetchexercise.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchexercise.data.model.HiringModel
import com.example.fetchexercise.data.network.ApiRepository
import com.example.fetchexercise.presentation.model.Group
import com.example.fetchexercise.presentation.model.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val apiRepository: ApiRepository): ViewModel() {

    private val _dataFlow = MutableStateFlow<UIState<List<Group>>>(UIState.Loading())
    val dataFlow: StateFlow<UIState<List<Group>>> = _dataFlow

    fun loadData() {
        viewModelScope.launch {
            apiRepository.getHiringList()
                .catch {exception ->
                    _dataFlow.value = UIState.Error(exception.message)
                }
                .collect{
                    _dataFlow.value = UIState.Success(it.covertToGroupData())
                }
        }
    }

    private fun List<HiringModel>.covertToGroupData() : List<Group> {
        return this
            .filter { !it.name.isNullOrBlank() }
            .sortedWith(compareBy({ it.listId }, { it.name }))
            .groupBy { it.listId }
            .map { Group(it.key, it.value, false) }
    }

}