package com.example.legalapor.service.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.legalapor.service.repository.LawyerRepository
import com.example.legalapor.models.LawyerModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BerandaViewModel : ViewModel() {
    private val repository = LawyerRepository()

    private val _lawyers = MutableStateFlow<List<LawyerModel>>(emptyList())
    val lawyers: StateFlow<List<LawyerModel>> = _lawyers

    init {
        fetchLawyers()
    }

    private fun fetchLawyers() {
        viewModelScope.launch {
            val result = repository.getLawyers()
            _lawyers.value = result
        }
    }
}

