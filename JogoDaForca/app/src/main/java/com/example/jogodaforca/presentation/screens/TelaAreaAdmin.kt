package com.example.jogodaforca.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jogodaforca.data.remote.model.PalavraRemota
import com.example.jogodaforca.presentation.navigation.Rota
import com.example.jogodaforca.presentation.viewmodel.AdminViewModel
import com.example.jogodaforca.presentation.viewmodel.FabricaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAreaAdmin(
    navControllerGlobal: NavController,
    fabricaViewModel: FabricaViewModel
) {
    val viewModel: AdminViewModel = viewModel(factory = fabricaViewModel)
    val estado by viewModel.estadoUi.collectAsStateWithLifecycle()

    var mostrarDialogo by remember { mutableStateOf(false) }
    var novaPalavra by remember { mutableStateOf("") }
    var novaCategoria by remember { mutableStateOf("") }

    BackHandler {
        navControllerGlobal.navigate(Rota.Login.rota) {
            popUpTo(Rota.AreaAdmin.rota) { inclusive = true }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin - Gerenciar Palavras") },
                actions = {
                    IconButton(onClick = {
                        navControllerGlobal.navigate(Rota.Login.rota) {
                            popUpTo(Rota.AreaAdmin.rota) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, "Sair")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                novaPalavra = ""
                novaCategoria = ""
                mostrarDialogo = true
            }) {
                Icon(Icons.Default.Add, "Adicionar Palavra")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (estado.estaCarregando) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            if (estado.mensagemErro!= null) {
                Text(
                    text = "Erro: ${estado.mensagemErro}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(estado.palavras) { palavra ->
                    ItemPalavraAdmin(
                        palavra = palavra,
                        onDeletarClick = {
                            palavra.id?.let { id ->
                                viewModel.deletarPalavra(id)
                            }
                        }
                    )
                    Divider()
                }
            }
        }
    }


    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Adicionar Nova Palavra") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = novaPalavra,
                        onValueChange = { novaPalavra = it.uppercase() },
                        label = { Text("Palavra") }
                    )
                    OutlinedTextField(
                        value = novaCategoria,
                        onValueChange = { novaCategoria = it.uppercase() },
                        label = { Text("Categoria") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.adicionarPalavra(novaPalavra, novaCategoria)
                        mostrarDialogo = false
                    },
                    enabled = novaPalavra.isNotBlank() && novaCategoria.isNotBlank()
                ) {
                    Text("Adicionar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}


@Composable
private fun ItemPalavraAdmin(
    palavra: PalavraRemota,
    onDeletarClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = palavra.palavra, style = MaterialTheme.typography.titleMedium)
            Text(text = "Categoria: ${palavra.categoria}", style = MaterialTheme.typography.bodySmall)
        }
        IconButton(onClick = onDeletarClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Deletar Palavra",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}