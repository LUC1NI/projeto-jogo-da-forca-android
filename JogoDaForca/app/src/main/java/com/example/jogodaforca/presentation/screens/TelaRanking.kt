package com.example.jogodaforca.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jogodaforca.data.local.model.PontuacaoEntidade
import com.example.jogodaforca.presentation.viewmodel.RankingViewModel


@Composable
fun TelaRanking(
    viewModel: RankingViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val estado by viewModel.estadoUi.collectAsStateWithLifecycle(lifecycleOwner = lifecycleOwner)

    Column(
        modifier = Modifier
            .fillMaxSize()git 
            .padding(16.dp)
    ) {
        if (estado.estaCarregando) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (estado.erro!= null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Erro ao carregar ranking: ${estado.erro}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else if (estado.pontuacoes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Nenhuma pontuação registrada ainda.")
            }
        } else {
            Text(
                text = "Ranking de Jogadores",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(estado.pontuacoes) { pontuacao ->
                    ItemRanking(pontuacao = pontuacao)
                    Divider()
                }
            }
        }
    }
}


@Composable
private fun ItemRanking(pontuacao: PontuacaoEntidade) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = pontuacao.nomeJogador,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${pontuacao.pontuacao} pontos",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}