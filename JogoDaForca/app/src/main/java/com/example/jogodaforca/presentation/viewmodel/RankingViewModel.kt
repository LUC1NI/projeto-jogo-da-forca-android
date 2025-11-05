package com.example.jogodaforca.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jogodaforca.data.repository.RepositorioJogo
import kotlinx.coroutines.flow.*


class RankingViewModel(repositorio: RepositorioJogo) : ViewModel() {

    val estadoUi: StateFlow<RankingEstado> = repositorio.fluxoRanking
        .map { listaPontuacoes ->

            val listaOrdenada = listaPontuacoes.sortedByDescending { it.pontuacao }
            RankingEstado(pontuacoes = listaOrdenada, estaCarregando = false)
        }
        .catch { excecao ->
            emit(RankingEstado(estaCarregando = false, erro = excecao.message))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = RankingEstado(estaCarregando = true)
        )
}