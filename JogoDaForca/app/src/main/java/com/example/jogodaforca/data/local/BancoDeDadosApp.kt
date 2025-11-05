package com.example.jogodaforca.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jogodaforca.data.local.model.PalavraEntidade
import com.example.jogodaforca.data.local.model.PontuacaoEntidade


@Database(
    entities = [PontuacaoEntidade::class, PalavraEntidade::class],
    version = 1,
    exportSchema = false
)
abstract class BancoDeDadosApp : RoomDatabase() {

    abstract fun pontuacaoDao(): PontuacaoDao

    abstract fun palavraDao(): PalavraDao
}