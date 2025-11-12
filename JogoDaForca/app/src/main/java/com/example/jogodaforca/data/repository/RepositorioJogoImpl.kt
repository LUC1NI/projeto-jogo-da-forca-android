package com.example.jogodaforca.data.repository

import com.example.jogodaforca.data.local.PalavraDao
import com.example.jogodaforca.data.local.PontuacaoDao
import com.example.jogodaforca.data.local.model.PalavraEntidade
import com.example.jogodaforca.data.local.model.PontuacaoEntidade
import com.example.jogodaforca.data.remote.FonteDadosFirebase
import com.example.jogodaforca.data.remote.model.PalavraRemota
import kotlinx.coroutines.flow.Flow

class RepositorioJogoImpl(
    private val pontuacaoDao: PontuacaoDao,
    private val palavraDao: PalavraDao,
    private val fonteDadosFirebase: FonteDadosFirebase
) : RepositorioJogo {


    override val fluxoRanking: Flow<List<PontuacaoEntidade>> =
        pontuacaoDao.obterTodasPontuacoes()

    override suspend fun salvarPontuacao(nomeJogador: String, pontuacao: Int) {
        val novaPontuacao = PontuacaoEntidade(nomeJogador = nomeJogador, pontuacao = pontuacao)
        pontuacaoDao.inserirPontuacao(novaPontuacao)
    }

    override suspend fun deletarPontuacao(pontuacao: PontuacaoEntidade) {
        pontuacaoDao.deletarPontuacao(pontuacao)
    }


    override suspend fun obterPalavrasParaJogo(categoria: String): ResultadoDados<List<PalavraEntidade>> {
        when (val resultadoCache = assegurarCacheDePalavras()) {
            is ResultadoDados.Erro -> return resultadoCache
            is ResultadoDados.Sucesso -> {
                return try {
                    val palavrasEmCache = palavraDao.obterTodasPalavras()
                    val palavrasFiltradas = palavrasEmCache.filter { it.categoria.equals(categoria, ignoreCase = true) }

                    if (palavrasFiltradas.isEmpty()) {
                        ResultadoDados.Erro("Nenhuma palavra encontrada para a categoria '$categoria'.")
                    } else {
                        ResultadoDados.Sucesso(palavrasFiltradas)
                    }
                } catch (e: Exception) {
                    ResultadoDados.Erro("Falha ao ler palavras do cache: ${e.message}")
                }
            }
        }
    }


    override suspend fun obterPalavrasAdmin(): ResultadoDados<List<PalavraRemota>> {
        return try {
            val palavras = fonteDadosFirebase.obterTodasPalavras()
            ResultadoDados.Sucesso(palavras)
        } catch (e: Exception) {
            ResultadoDados.Erro(e.message?: "Erro desconhecido ao obter palavras")
        }
    }

    override suspend fun adicionarPalavraAdmin(palavra: PalavraRemota): ResultadoDados<Unit> {
        return try {
            fonteDadosFirebase.adicionarPalavra(palavra)
            ResultadoDados.Sucesso(Unit)
        } catch (e: Exception) {
            ResultadoDados.Erro(e.message?: "Erro desconhecido ao adicionar palavra")
        }
    }

    override suspend fun deletarPalavraAdmin(idPalavra: String): ResultadoDados<Unit> {
        return try {
            fonteDadosFirebase.deletarPalavra(idPalavra)
            ResultadoDados.Sucesso(Unit)
        } catch (e: Exception) {
            ResultadoDados.Erro(e.message?: "Erro desconhecido ao deletar palavra")
        }
    }
    private suspend fun assegurarCacheDePalavras(): ResultadoDados<Unit> {
        return try {
            val palavrasEmCache = palavraDao.obterTodasPalavras()
            if (palavrasEmCache.isEmpty()) {
                val palavrasRemotas = fonteDadosFirebase.obterTodasPalavras()

                val palavrasParaCache = palavrasRemotas.map { palavraRemota ->
                    PalavraEntidade(
                        palavra = palavraRemota.palavra,
                        categoria = palavraRemota.categoria
                    )
                }
                palavraDao.inserirTodas(palavrasParaCache)
            }
            ResultadoDados.Sucesso(Unit)
        } catch (e: Exception) {
            ResultadoDados.Erro("Falha ao assegurar cache de palavras: ${e.message}")
        }
    }

    override suspend fun obterCategoriasDisponiveis(): ResultadoDados<List<String>> {
        when (val resultadoCache = assegurarCacheDePalavras()) {
            is ResultadoDados.Erro -> return resultadoCache
            is ResultadoDados.Sucesso -> {
                return try {
                    val palavrasEmCache = palavraDao.obterTodasPalavras()

                    val categorias = palavrasEmCache
                        .map { it.categoria.uppercase() }
                        .toSet()
                        .sorted()

                    ResultadoDados.Sucesso(categorias)
                } catch (e: Exception) {
                    ResultadoDados.Erro("Falha ao ler categorias do cache: ${e.message}")
                }
            }
        }
    }
}