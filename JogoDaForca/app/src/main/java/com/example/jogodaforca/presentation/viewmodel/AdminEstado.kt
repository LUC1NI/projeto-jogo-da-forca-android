package com.example.jogodaforca.presentation.viewmodel

import com.example.jogodaforca.data.remote.model.PalavraRemota

data class AdminEstado(
    val palavras: List<PalavraRemota> = emptyList(),
    val estaCarregando: Boolean = false,
    val mensagemErro: String? = null
)