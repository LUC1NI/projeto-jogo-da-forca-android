package com.example.jogodaforca.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jogodaforca.data.repository.RepositorioJogo
import com.example.jogodaforca.data.repository.ResultadoDados
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CategoriaViewModel(
    private val repositorio: RepositorioJogo
) : ViewModel() {

    private val _estadoUi = MutableStateFlow(CategoriaEstado())
    val estadoUi = _estadoUi.asStateFlow()

    init {
        carregarCategorias()
    }

    fun carregarCategorias() {
        _estadoUi.update { it.copy(estaCarregando = true, mensagemErro = null) }

        viewModelScope.launch {
            when (val resultado = repositorio.obterCategoriasDisponiveis()) {
                is ResultadoDados.Sucesso -> {
                    _estadoUi.update {
                        it.copy(
                            estaCarregando = false,
                            categorias = resultado.dados
                        )
                    }
                }
                is ResultadoDados.Erro -> {
                    _estadoUi.update {
                        it.copy(
                            estaCarregando = false,
                            mensagemErro = resultado.mensagem
                        )
                    }
                }
            }
        }
    }
}