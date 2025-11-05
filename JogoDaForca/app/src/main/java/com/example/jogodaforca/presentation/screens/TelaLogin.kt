package com.example.jogodaforca.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jogodaforca.presentation.navigation.Rota


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaLogin(navController: NavController) {
    var nomeUsuario by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Jogo da Forca", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nomeUsuario,
            onValueChange = { nomeUsuario = it },
            label = { Text("Digite seu nome (ou 'admin')") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val nomeLimpo = nomeUsuario.trim()

            if (nomeLimpo.lowercase() == "admin") {

                navController.navigate(Rota.AreaAdmin.rota) {

                    popUpTo(Rota.Login.rota) { inclusive = true }
                }
            } else if (nomeLimpo.isNotBlank()) {

                navController.navigate(Rota.AreaJogador.criarRota(nomeLimpo)) {

                    popUpTo(Rota.Login.rota) { inclusive = true }
                }
            }
        }) {
            Text("Entrar")
        }
    }
}