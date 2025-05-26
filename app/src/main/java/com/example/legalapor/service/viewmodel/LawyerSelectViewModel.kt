package com.example.legalapor.service.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.legalapor.models.LawyerModel
import com.example.legalapor.service.repository.LawyerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class LawyerSelectViewModel : ViewModel() {
    private val repository = LawyerRepository()

    private val _lawyers = MutableStateFlow<List<LawyerModel>>(emptyList())
    open val lawyers: StateFlow<List<LawyerModel>> = _lawyers

    init {
        fetchLawyers()
    }

    private fun fetchLawyers() {
        viewModelScope.launch {
            val result = repository.getLawyers()
            _lawyers.value = result
        }

    }
        fun onLawyerClicked(lawyer: LawyerModel) {
            println("Lawyer clicked: ${lawyer.name}")
        }
}