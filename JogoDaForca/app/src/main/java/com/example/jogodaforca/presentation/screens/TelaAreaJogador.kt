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
    nomeJogador: String
) {

    val navControllerAninhado = rememberNavController() // [2]


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

                        navControllerGlobal.navigate(Rota.Login.rota) {
                            popUpTo(Rota.AreaJogador.rota) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Sair")
                    }
                }
            )
        },
        bottomBar = {
            // Requisito: "Bottom Navigation Bar"
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

            composable(rota = Rota.Jogo.rota) {
                TelaJogo(

                    viewModel = viewModel(factory = fabricaViewModel),
                    nomeJogador = nomeJogador
                )
            }


            composable(rota = Rota.Ranking.rota) {
                TelaRanking(

                    viewModel = viewModel(factory = fabricaViewModel)
                )
            }


            composable(rota = Rota.Configuracoes.rota) {
                TelaConfiguracoes()
            }
        }
    }
}

}


