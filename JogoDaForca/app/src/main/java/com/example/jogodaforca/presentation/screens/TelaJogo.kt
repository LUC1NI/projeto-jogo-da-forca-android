package com.example.jogodaforca.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jogodaforca.presentation.viewmodel.EstadoJogo
import com.example.jogodaforca.presentation.viewmodel.JogoViewModel


@Composable
fun TelaJogo(
    viewModel: JogoViewModel,
    nomeJogador: String
) {
    val estado by viewModel.estadoUi.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        DesenhoForca(tentativasRestantes = estado.tentativasRestantes)

        PalavraOculta(palavra = estado.palavraExibida)

        Teclado(
            letrasUsadas = estado.letrasUsadas,
            habilitado = estado.estadoDoJogo == EstadoJogo.JOGANDO,
            onLetraClick = { letra ->
                viewModel.processarPalpite(letra) // [2]
            }
        )

        if (estado.estadoDoJogo == EstadoJogo.ERRO) {
            Text(
                text = estado.mensagemErro?: "Ocorreu um erro desconhecido",
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }

        if (estado.estadoDoJogo == EstadoJogo.VITORIA) {
            AlertDialog(
                onDismissRequest = { /* Não pode fechar */ },
                title = { Text("Você Venceu!") },
                text = { Text("Pontuação: ${estado.pontuacao}") },
                confirmButton = {
                    Button(onClick = {
                        viewModel.salvarPontuacaoFinal(nomeJogador)
                        viewModel.carregarNovaPalavra()
                    }) {
                        Text("Salvar e Jogar Novamente")
                    }
                }
            )
        }

        if (estado.estadoDoJogo == EstadoJogo.DERROTA) {
            AlertDialog(
                onDismissRequest = {  },
                title = { Text("Você Perdeu!") },
                text = { Text("A palavra era: ${estado.palavraParaAdivinhar}") },
                confirmButton = {
                    Button(onClick = {
                        viewModel.carregarNovaPalavra()
                    }) {
                        Text("Jogar Novamente")
                    }
                }
            )
        }
    }
}


@Composable
private fun DesenhoForca(tentativasRestantes: Int) {
    Canvas(
        modifier = Modifier
            .size(width = 200.dp, height = 250.dp)
            .padding(top = 16.dp)
    ) {
        val larguraLinha = 8f
        val cor = Color.Black

        drawLine(cor, Offset(x = 20f, y = size.height), Offset(x = size.width - 20f, y = size.height), larguraLinha)
        drawLine(cor, Offset(x = 60f, y = size.height), Offset(x = 60f, y = 20f), larguraLinha)
        drawLine(cor, Offset(x = 60f, y = 20f), Offset(x = 150f, y = 20f), larguraLinha)
        drawLine(cor, Offset(x = 150f, y = 20f), Offset(x = 150f, y = 70f), larguraLinha / 2)


        if (tentativasRestantes <= 5) {
            drawCircle(
                color = cor,
                radius = 25f,
                center = Offset(x = 150f, y = 95f),
                style = Stroke(width = larguraLinha)
            )
        }
        if (tentativasRestantes <= 4) {
            drawLine(cor, Offset(x = 150f, y = 120f), Offset(x = 150f, y = 180f), larguraLinha)
        }
        if (tentativasRestantes <= 3) {
            drawLine(cor, Offset(x = 150f, y = 130f), Offset(x = 120f, y = 160f), larguraLinha)
        }
        if (tentativasRestantes <= 2) {
            drawLine(cor, Offset(x = 150f, y = 130f), Offset(x = 180f, y = 160f), larguraLinha)
        }
        if (tentativasRestantes <= 1) {
            drawLine(cor, Offset(x = 150f, y = 180f), Offset(x = 120f, y = 220f), larguraLinha)
        }
        if (tentativasRestantes <= 0) {
            drawLine(cor, Offset(x = 150f, y = 180f), Offset(x = 180f, y = 220f), larguraLinha)
        }
    }
}


@Composable
private fun PalavraOculta(palavra: String) {
    Text(
        text = palavra.replace("", " ").trim(),
        fontSize = 32.sp,
        letterSpacing = 4.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(vertical = 24.dp)
    )
}


@Composable
private fun Teclado(
    letrasUsadas: Set<Char>,
    habilitado: Boolean,
    onLetraClick: (Char) -> Unit
) {
    val alfabeto = ('A'..'Z').toList()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 40.dp),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(alfabeto) { letra ->
            Button(
                onClick = { onLetraClick(letra) },
                enabled = (letra!in letrasUsadas) && habilitado,
                modifier = Modifier.size(44.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = letra.toString(), fontSize = 18.sp)
            }
        }
    }
}