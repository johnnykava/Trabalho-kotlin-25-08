package com.example.atividadeavaliativa2508

import android.R.attr.text
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.atividadeavaliativa2508.ui.theme.AtividadeAvaliativa2508Theme

data class NotaItem(
    val id : Long,
    val titulo: String,
    val concluido: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            nota()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun notaPreview(){
    nota()
}

@Composable
fun nota(){
    var texto by remember() { mutableStateOf("") }
    val context = LocalContext.current
    var notas = remember { mutableStateListOf<NotaItem>()}

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally){

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Bloco de Notas:", fontSize = 20.sp)

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(value = texto,
                onValueChange = {texto = it},
                singleLine = true,
                label = {Text(text = "Nova Nota")},
                modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                val titulo = texto.trim()

                if(titulo.isNotEmpty()){
                    for(item in notas){
                        if(item.titulo.lowercase() == titulo.lowercase()){
                            Toast.makeText(context, "Nota já adicionada", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                    }

                    val id = System.currentTimeMillis()

                    val nota = NotaItem(id, titulo)

                    notas.add(nota)

                    texto = ""
                }
            }) {
                Text(text = "Adicionar")
            }
        } // Fim da row

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)) {
                items(notas){
                    notaAtual ->

                    notaDaLista(
                        item = notaAtual,

                        onDelete = {
                            notas.remove(notaAtual)
                        },

                        onToggle = {
                            val index = notas.indexOfFirst { it.id == notaAtual.id }

                            if(index != -1){
                                val produtoAtual = notas[index]
                                notas[index]= produtoAtual.copy(concluido = !notaAtual.concluido)
                            }
                        }
                    )
                }
        } // fim da Lazy column

        Button(modifier = Modifier.fillMaxWidth(),
            enabled = notas.isNotEmpty(),
            onClick = {
                notas.clear()
                texto = ""
            }) {
            Text(text = "Apagar Tudo", fontSize = 25.sp)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun notaDaLista(item: NotaItem, onDelete: () -> Unit, onToggle: () -> Unit){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
        .combinedClickable(
            onClick = onToggle,
            onLongClick = onDelete
        )) {
        val textoRiscado = if(item.concluido) TextDecoration.LineThrough else TextDecoration.None

        val corTexto = if(item.concluido) Color.Gray else Color.Black

        Text(text = item.titulo, fontSize = 18.sp, textDecoration = textoRiscado, color = corTexto)
    }
}