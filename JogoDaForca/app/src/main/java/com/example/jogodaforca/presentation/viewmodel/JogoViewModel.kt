package com.example.jogodaforca.presentation.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jogodaforca.data.repository.RepositorioJogo
import com.example.jogodaforca.data.repository.ResultadoDados
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class JogoViewModel(
    private val repositorio: RepositorioJogo
) : ViewModel() {

    private val _estadoUi = MutableStateFlow(JogoEstado())
    val estadoUi = _estadoUi.asStateFlow()



    fun carregarNovaPalavra(categoria: String) {
        _estadoUi.value = JogoEstado(estadoDoJogo = EstadoJogo.CARREGANDO)

        viewModelScope.launch {
            when (val resultado = repositorio.obterPalavrasParaJogo(categoria)) {

                is ResultadoDados.Sucesso -> {
                    val palavra = resultado.dados.random().palavra.uppercase()
                    _estadoUi.value = JogoEstado(
                        palavraParaAdivinhar = palavra,
                        palavraExibida = "_".repeat(palavra.length),
                        estadoDoJogo = EstadoJogo.JOGANDO,
                        tentativasRestantes = 6
                    )
                }
                is ResultadoDados.Erro -> {
                    _estadoUi.value = JogoEstado(
                        mensagemErro = resultado.mensagem,
                        estadoDoJogo = EstadoJogo.ERRO
                    )
                }
            }
        }
    }



    fun processarPalpite(letra: Char) {
        val estadoAtual = _estadoUi.value
        if (estadoAtual.estadoDoJogo!= EstadoJogo.JOGANDO) return

        val palpite = letra.uppercaseChar()
        if (palpite in estadoAtual.letrasUsadas) return

        val novasLetrasUsadas = estadoAtual.letrasUsadas + palpite


        val palpiteCorreto = estadoAtual.palavraParaAdivinhar.contains(palpite)

        val novaPalavraExibida = estadoAtual.palavraParaAdivinhar.map { charDaPalavra ->
            if (charDaPalavra in novasLetrasUsadas) {
                charDaPalavra
            } else {
                '_'
            }
        }.joinToString(separator = "")

        val novasTentativas = if (palpiteCorreto) estadoAtual.tentativasRestantes else estadoAtual.tentativasRestantes - 1
        val novaPontuacao = if (palpiteCorreto) estadoAtual.pontuacao + 10 else estadoAtual.pontuacao - 5


        val novoEstadoJogo = when {
            novaPalavraExibida == estadoAtual.palavraParaAdivinhar -> EstadoJogo.VITORIA
            novasTentativas <= 0 -> EstadoJogo.DERROTA
            else -> EstadoJogo.JOGANDO
        }

        _estadoUi.update {
            it.copy(
                palavraExibida = novaPalavraExibida,
                letrasUsadas = novasLetrasUsadas,
                tentativasRestantes = novasTentativas,
                pontuacao = novaPontuacao,
                estadoDoJogo = novoEstadoJogo
            )
        }
    }


    fun salvarPontuacaoFinal(nomeJogador: String) {
        if (_estadoUi.value.estadoDoJogo!= EstadoJogo.VITORIA) return

        viewModelScope.launch {
            repositorio.salvarPontuacao(
                nomeJogador = nomeJogador,
                pontuacao = _estadoUi.value.pontuacao
            )
        }
    }
}