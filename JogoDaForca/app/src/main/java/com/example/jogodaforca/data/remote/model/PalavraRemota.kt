package com.example.jogodaforca.data.remote.model

import com.google.firebase.firestore.DocumentId

data class PalavraRemota(
    @get:DocumentId
    val id: String? = null,
    val palavra: String = "",
    val categoria: String = ""
)