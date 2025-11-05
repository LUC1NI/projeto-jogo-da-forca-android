package com.example.jogodaforca.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jogodaforca.data.repository.RepositorioJogo


class FabricaViewModel(
    private val repositorio: RepositorioJogo
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        when {

        }
        
        throw IllegalArgumentException("ViewModel Class Desconhecida: ${modelClass.name}") // [4]
    }
}