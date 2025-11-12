package com.example.jogodaforca.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jogodaforca.presentation.navigation.Rota
import com.example.jogodaforca.presentation.viewmodel.FabricaViewModel
import androidx.compose.material.icons.filled.List


private data class ItemBarraNavegacao(
    val rota: String,
    val icone: ImageVector,
    val nome: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAreaJogador(
    navControllerGlobal: NavController,
    fabricaViewModel: FabricaViewModel,
    nomeJogador: String,
    categoria: String
) {

    val navControllerAninhado = rememberNavController()


    val itensDaBarra = listOf(
        ItemBarraNavegacao(Rota.Jogo.rota, Icons.Default.PlayArrow, "Jogar"),
        ItemBarraNavegacao(Rota.Ranking.rota, Icons.Default.Star, "Ranking"),
        ItemBarraNavegacao(Rota.Configuracoes.rota, Icons.Default.Settings, "Ajustes")
    )


    var rotaSelecionada by rememberSaveable { mutableStateOf(Rota.Jogo.rota) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Olá, $nomeJogador!") },
                actions = {
                    IconButton(onClick = {
                        navControllerGlobal.navigate(
                            Rota.EscolhaCategoria.criarRota(nomeJogador)
                        ) {
                            popUpTo(Rota.Login.rota)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Mudar Categoria"
                        )
                    }

                    IconButton(onClick = {
                        navControllerGlobal.navigate(Rota.Login.rota) {
                            popUpTo(Rota.Login.rota) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Sair")
                    }
                }
            )
            },
        bottomBar = {
            NavigationBar {
                itensDaBarra.forEach { item ->
                    NavigationBarItem(
                        selected = (rotaSelecionada == item.rota),
                        onClick = {
                            rotaSelecionada = item.rota
                            navControllerAninhado.navigate(item.rota) {

                                popUpTo(navControllerAninhado.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(item.icone, contentDescription = item.nome) },
                        label = { Text(item.nome) }
                    )
                }
            }
        }
    ) { paddingValues ->



        NavHost(
            navController = navControllerAninhado,
            startDestination = Rota.Jogo.rota,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(route = Rota.Jogo.rota) {
                TelaJogo(
                    viewModel = viewModel(factory = fabricaViewModel),
                    nomeJogador = nomeJogador,
                    categoria = categoria
                )
            }

            composable(route = Rota.Ranking.rota) {
                TelaRanking(
                    viewModel = viewModel(factory = fabricaViewModel)
                )
            }

            composable(route = Rota.Configuracoes.rota) {
                TelaConfiguracoes()
            }
        }
    }
}






