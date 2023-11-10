package com.juan.quienquieresermillonario.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.juan.quienquieresermillonario.R
import com.juan.quienquieresermillonario.ui.theme.baby_Blue
import com.juan.quienquieresermillonario.ui.theme.blue_Grotto
import com.juan.quienquieresermillonario.ui.theme.orange
import com.juan.quienquieresermillonario.ui.theme.white

class AddPreguntas : ViewModel() {
    @Composable
    fun Inicio(navController: NavController) {
        EscribeArchivo(navController)
    }
}

@Composable
private fun EscribeArchivo(navController: NavController) {
    val context = LocalContext.current
    Image(
        painter = painterResource(id = R.drawable.fondo),
        contentDescription = "Imagen de fondo",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(20.dp)
            .background(color = orange, shape = RoundedCornerShape(20.dp))
            .border(width = 3.dp, color = white, shape = RoundedCornerShape(20.dp))

    ) {
        val pregunta = remember { mutableStateOf("") }
        val opcionA = remember { mutableStateOf("A: ") }
        val opcionB = remember { mutableStateOf("B: ") }
        val opcionC = remember { mutableStateOf("C: ") }
        val opcionD = remember { mutableStateOf("D: ") }
        val respuesta = remember { mutableStateOf("") }
        val opciones = listOf(opcionA, opcionB, opcionC, opcionD)
        val expanded = remember { mutableStateOf(false) }

        CampoDeTexto(texto = pregunta, descripcion = "Introduce la pregunta: ", 65)
        CampoDeTexto(texto = opcionA, descripcion = "Introduce la primera opción: ", 13)
        CampoDeTexto(texto = opcionB, descripcion = "Introduce la segunda opción: ", 13)
        CampoDeTexto(texto = opcionC, descripcion = "Introduce la tercera opción: ", 13)
        CampoDeTexto(texto = opcionD, descripcion = "Introduce la cuarta opción: ", 13)

        TextButton(
            modifier = Modifier.padding(20.dp),
            border = BorderStroke(1.dp, white),
            colors = ButtonDefaults.buttonColors(baby_Blue),
            onClick = { expanded.value = true }) {
            Text(
                color = white,
                textAlign = TextAlign.Center,
                text = respuesta.value.ifEmpty { "Pulsa para seleccionar la respuesta correcta." }
            )
            DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion.value) },
                        onClick = {
                            respuesta.value = opcion.value
                            expanded.value = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                verificaYAlmacenaPreguntas(
                    navController,
                    context,
                    pregunta.value,
                    opcionA.value,
                    opcionB.value,
                    opcionC.value,
                    opcionD.value,
                    respuesta.value
                )
            },
            border = BorderStroke(1.dp, white),
            colors = ButtonDefaults.buttonColors(blue_Grotto),
        ) {
            Text("Guardar")
        }
        Button(
            onClick = { navController.popBackStack() },
            border = BorderStroke(1.dp, white),
            colors = ButtonDefaults.buttonColors(blue_Grotto),
        ) {
            Text(text = "Volver")
        }
    }
}

@Composable
private fun CampoDeTexto(texto: MutableState<String>, descripcion: String, maximo: Int) {
    Column {
        TextField(
            value = texto.value,
            onValueChange = { nuevoTexto ->
                if (nuevoTexto.length <= maximo) {
                    texto.value = nuevoTexto
                }
            },
            label = { Text(descripcion) },
            shape = RoundedCornerShape(7.dp),
            modifier = Modifier
                .border(width = 2.dp, color = white, shape = RoundedCornerShape(7.dp))
                .width(300.dp)
        )
        Text("${texto.value.length} / $maximo")
    }
}


