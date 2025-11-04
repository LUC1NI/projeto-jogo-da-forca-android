package com.example.jogodaforca.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "palavras_cache")
data class PalavraEntidade(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val palavra: String,
    val categoria: String
)