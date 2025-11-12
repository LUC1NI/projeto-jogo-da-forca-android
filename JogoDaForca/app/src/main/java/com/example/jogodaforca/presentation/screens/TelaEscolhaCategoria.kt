package com.example.jogodaforca.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jogodaforca.presentation.navigation.Rota
import com.example.jogodaforca.presentation.viewmodel.CategoriaViewModel
import com.example.jogodaforca.presentation.viewmodel.FabricaViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaEscolhaCategoria(
    navController: NavController,
    fabricaViewModel: FabricaViewModel,
    nomeUsuario: String
) {
    val viewModel: CategoriaViewModel = viewModel(factory = fabricaViewModel)
    val estado by viewModel.estadoUi.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Olá, $nomeUsuario!") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Escolha uma categoria para jogar:",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

            if (estado.estaCarregando) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
            } else if (estado.mensagemErro!= null) {
                Text(
                    text = "Erro: ${estado.mensagemErro}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(estado.categorias) { categoria ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    navController.navigate(
                                        Rota.AreaJogador.criarRota(
                                            nomeUsuario = nomeUsuario,
                                            categoria = categoria
                                        )
                                    ) {
                                        popUpTo(Rota.Login.rota) { inclusive = true }
                                    }
                                }
                        ) {
                            Text(
                                text = categoria,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}