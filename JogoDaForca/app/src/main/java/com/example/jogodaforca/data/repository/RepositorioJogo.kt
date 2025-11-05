package com.example.jogodaforca.data.repository // Pacote em inglês

import com.example.jogodaforca.data.local.model.PalavraEntidade
import com.example.jogodaforca.data.local.model.PontuacaoEntidade
import com.example.jogodaforca.data.remote.model.PalavraRemota
import kotlinx.coroutines.flow.Flow


sealed class ResultadoDados<out T> {
    data class Sucesso<T>(val dados: T) : ResultadoDados<T>()
    data class Erro(val mensagem: String) : ResultadoDados<Nothing>()
}

interface RepositorioJogo {


    val fluxoRanking: Flow<List<PontuacaoEntidade>>


    suspend fun salvarPontuacao(nomeJogador: String, pontuacao: Int)


    suspend fun deletarPontuacao(pontuacao: PontuacaoEntidade)


    suspend fun obterPalavrasParaJogo(): ResultadoDados<List<PalavraEntidade>>


    suspend fun obterPalavrasAdmin(): ResultadoDados<List<PalavraRemota>>


    suspend fun adicionarPalavraAdmin(palavra: PalavraRemota): ResultadoDados<Unit>


    suspend fun deletarPalavraAdmin(idPalavra: String): ResultadoDados<Unit>
}