package com.example.jogodaforca.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.jogodaforca.data.local.BancoDeDadosApp
import com.example.jogodaforca.data.remote.FonteDadosFirebase
import com.example.jogodaforca.data.repository.RepositorioJogoImpl
import com.example.jogodaforca.presentation.screens.TelaLogin
import com.example.jogodaforca.presentation.viewmodel.FabricaViewModel
import com.example.jogodaforca.presentation.screens.TelaAreaAdmin
import com.example.jogodaforca.presentation.screens.TelaAreaJogador
import com.example.jogodaforca.presentation.screens.TelaJogo
import com.example.jogodaforca.presentation.viewmodel.JogoViewModel

@Composable
fun GraficoNavegacaoApp() {

    val contexto = LocalContext.current.applicationContext

    val fabricaViewModel = remember {

        val bancoDeDados = Room.databaseBuilder(
            contexto,
            BancoDeDadosApp::class.java,
            "jogo_da_forca.db"
        ).build()


        val fonteDadosFirebase = FonteDadosFirebase()


        val repositorio = RepositorioJogoImpl(
            pontuacaoDao = bancoDeDados.pontuacaoDao(),
            palavraDao = bancoDeDados.palavraDao(),
            fonteDadosFirebase = fonteDadosFirebase
        )


        FabricaViewModel(repositorio)
    }



    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = Rota.Login.rota 
    ) {

        composable(route = Rota.Login.rota) {

            TelaLogin(navController = navController)
        }


        composable(
            route = Rota.AreaJogador.rota,

            arguments = listOf(navArgument("nomeUsuario") { type = NavType.StringType })
        ) { backStackEntry ->

            val nomeUsuario = backStackEntry.arguments?.getString("nomeUsuario")?: "Jogador"

            TelaAreaJogador(
                navControllerGlobal = navController,
                fabricaViewModel = fabricaViewModel,
                nomeJogador = nomeUsuario
            )
        }


        composable(route = Rota.AreaAdmin.rota) {

            TelaAreaAdmin(
                navControllerGlobal = navController,
                fabricaViewModel = fabricaViewModel
            )
        }
        composable(route = Rota.Jogo.rota) {
            val viewModel: JogoViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(factory = fabricaViewModel)

            TelaJogo(viewModel = viewModel, nomeJogador = "Jogador")
        }
    }
}