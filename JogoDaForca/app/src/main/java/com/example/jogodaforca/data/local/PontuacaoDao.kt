package com.example.jogodaforca.data.local

import androidx.room.*
import com.example.jogodaforca.data.local.model.PontuacaoEntidade
import kotlinx.coroutines.flow.Flow

@Dao
interface PontuacaoDao {

    @Query("SELECT * FROM pontuacoes ORDER BY pontuacao DESC")
    fun obterTodasPontuacoes(): Flow<List<PontuacaoEntidade>> //


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirPontuacao(pontuacao: PontuacaoEntidade) //

    @Update
    suspend fun atualizarPontuacao(pontuacao: PontuacaoEntidade)

    @Delete
    suspend fun deletarPontuacao(pontuacao: PontuacaoEntidade)
}