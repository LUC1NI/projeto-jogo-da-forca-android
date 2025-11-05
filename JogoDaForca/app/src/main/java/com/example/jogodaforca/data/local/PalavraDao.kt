package com.example.jogodaforca.data.local

import androidx.room.*
import com.example.jogodaforca.data.local.model.PalavraEntidade

@Dao
interface PalavraDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirTodas(palavras: List<PalavraEntidade>)


    @Query("SELECT * FROM palavras_cache")
    suspend fun obterTodasPalavras(): List<PalavraEntidade>


    @Query("DELETE FROM palavras_cache")
    suspend fun limparCache()
}