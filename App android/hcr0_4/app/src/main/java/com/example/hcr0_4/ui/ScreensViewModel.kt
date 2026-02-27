package com.example.hcr0_4.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ScreensViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ScreensUiState())
    val uiState: StateFlow<ScreensUiState> = _uiState
}