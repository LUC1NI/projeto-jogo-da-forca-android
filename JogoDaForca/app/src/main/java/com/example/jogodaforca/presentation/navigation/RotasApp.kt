package com.example.jogodaforca.presentation.navigation // Pacote em inglês


sealed class Rota(val rota: String) {
    object Login : Rota("login")


    object EscolhaCategoria : Rota("escolha_categoria/{nomeUsuario}") {
        fun criarRota(nomeUsuario: String) = "escolha_categoria/$nomeUsuario"
    }

    object AreaJogador : Rota("area_jogador/{nomeUsuario}/{categoria}") {
        fun criarRota(nomeUsuario: String, categoria: String) =
            "area_jogador/$nomeUsuario/$categoria"
    }

    object AreaAdmin : Rota("area_admin")


    object Jogo : Rota("jogo")

    object Ranking : Rota("ranking")

    object Configuracoes : Rota("configuracoes")
}