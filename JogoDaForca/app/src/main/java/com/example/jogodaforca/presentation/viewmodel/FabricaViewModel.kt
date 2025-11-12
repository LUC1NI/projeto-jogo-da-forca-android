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
            modelClass.isAssignableFrom(JogoViewModel::class.java) -> {
                return JogoViewModel(repositorio) as T
            }

            modelClass.isAssignableFrom(RankingViewModel::class.java) -> {
                return RankingViewModel(repositorio) as T
            }

            modelClass.isAssignableFrom(AdminViewModel::class.java) -> {
                return AdminViewModel(repositorio) as T
            }

            modelClass.isAssignableFrom(CategoriaViewModel::class.java) -> {
                return CategoriaViewModel(repositorio) as T
            }
        }

        throw IllegalArgumentException("ViewModel Class Desconhecida: ${modelClass.name}")
    }
}