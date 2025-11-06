package com.example.jogodaforca.presentation.navigation


sealed class Rota(val rota: String) {
    object Login : Rota("login")

    object AreaJogador : Rota("area_jogador/{nomeUsuario}") {
        fun criarRota(nomeUsuario: String) = "area_jogador/$nomeUsuario"
    }

    object AreaAdmin : Rota("area_admin")


    object Jogo : Rota("jogo/{nomeJogador}") {
        fun criarRota(nomeJogador: String) = "jogo/$nomeJogador"
    }

    object Ranking : Rota("ranking")

    object Configuracoes : Rota("configuracoes")

    object EdicaoPalavra : Rota("edicao_palavra")
}