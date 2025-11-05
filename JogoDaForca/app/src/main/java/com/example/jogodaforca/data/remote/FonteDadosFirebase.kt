package com.example.jogodaforca.data.remote
import com.example.jogodaforca.data.remote.model.PalavraRemota
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FonteDadosFirebase {

    private val db = FirebaseFirestore.getInstance()

    private val colecaoPalavras = db.collection("palavras")


    suspend fun obterTodasPalavras(): List<PalavraRemota> {
        return try {
            colecaoPalavras.get().await().toObjects(PalavraRemota::class.java)
        } catch (e: Exception) {
            throw e
        }
    }


    suspend fun adicionarPalavra(palavra: PalavraRemota) {
        try {
            val dadosPalavra = mapOf(
                "palavra" to palavra.palavra,
                "categoria" to palavra.categoria
            )
            colecaoPalavras.add(dadosPalavra).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun deletarPalavra(idPalavra: String) {
        try {
            colecaoPalavras.document(idPalavra).delete().await()
        } catch (e: Exception) {
            throw e
        }
    }

}