package com.example.jogodaforca.presentation.viewmodel


data class CategoriaEstado(
    val categorias: List<String> = emptyList(),
    val estaCarregando: Boolean = true,
    val mensagemErro: String? = null
)