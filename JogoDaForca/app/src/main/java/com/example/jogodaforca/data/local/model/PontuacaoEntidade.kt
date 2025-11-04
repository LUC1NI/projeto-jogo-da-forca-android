package com.example.jogodaforca.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pontuacoes")
data class PontuacaoEntidade(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nomeJogador: String,
    val pontuacao: Int,
    val timestamp: Long = System.currentTimeMillis()
)