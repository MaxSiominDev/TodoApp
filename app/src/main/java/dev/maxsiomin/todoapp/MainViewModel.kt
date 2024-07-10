package dev.maxsiomin.todoapp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.todoapp.core.domain.usecase.GetTokenUseCase
import javax.inject.Inject

/** Viewmodel for [MainActivity] */
@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
) : ViewModel() {

    fun isAuthenticated() = getTokenUseCase() != null

}