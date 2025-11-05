package com.example.jogodaforca.presentation.viewmodel

import com.example.jogodaforca.data.local.model.PontuacaoEntidade

data class RankingEstado(
    val pontuacoes: List<PontuacaoEntidade> = emptyList(),
    val estaCarregando: Boolean = true,
    val erro: String? = null
)