package com.example.jogodaforca.presentation.viewmodel

enum class EstadoJogo {
    CARREGANDO,
    JOGANDO,
    VITORIA,
    DERROTA,
    ERRO
}


data class JogoEstado(
    val palavraParaAdivinhar: String = "",
    val palavraExibida: String = "",
    val letrasUsadas: Set<Char> = emptySet(),
    val tentativasRestantes: Int = 6,
    val pontuacao: Int = 0,
    val estadoDoJogo: EstadoJogo = EstadoJogo.CARREGANDO,
    val mensagemErro: String? = null
)