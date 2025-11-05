package com.example.jogodaforca.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jogodaforca.data.remote.model.PalavraRemota
import com.example.jogodaforca.data.repository.RepositorioJogo
import com.example.jogodaforca.data.repository.ResultadoDados
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repositorio: RepositorioJogo
) : ViewModel() {

    private val _estadoUi = MutableStateFlow(AdminEstado())
    val estadoUi = _estadoUi.asStateFlow()

    init {
        carregarPalavras()
    }
    fun carregarPalavras() {
        _estadoUi.update { it.copy(estaCarregando = true, mensagemErro = null) }

        viewModelScope.launch {
            when (val resultado = repositorio.obterPalavrasAdmin()) {
                is ResultadoDados.Sucesso -> {
                    _estadoUi.update {
                        it.copy(palavras = resultado.dados, estaCarregando = false)
                    }
                }
                is ResultadoDados.Erro -> {
                    _estadoUi.update {
                        it.copy(mensagemErro = resultado.mensagem, estaCarregando = false)
                    }
                }
            }
        }
    }
    fun adicionarPalavra(palavra: String, categoria: String) {
        viewModelScope.launch {
            _estadoUi.update { it.copy(estaCarregando = true) }
            val novaPalavra = PalavraRemota(palavra = palavra, categoria = categoria)

            when (repositorio.adicionarPalavraAdmin(novaPalavra)) {
                is ResultadoDados.Sucesso -> {
                    carregarPalavras()
                }
                is ResultadoDados.Erro -> {
                    _estadoUi.update {
                        it.copy(mensagemErro = "Falha ao adicionar palavra", estaCarregando = false)
                    }
                }
            }
        }
    }

    fun deletarPalavra(idPalavra: String) {
        viewModelScope.launch {
            _estadoUi.update { it.copy(estaCarregando = true) }

            when (repositorio.deletarPalavraAdmin(idPalavra)) {
                is ResultadoDados.Sucesso -> {
                    carregarPalavras()
                }
                is ResultadoDados.Erro -> {
                    _estadoUi.update {
                        it.copy(mensagemErro = "Falha ao deletar palavra", estaCarregando = false)
                    }
                }
            }
        }
    }
}