package com.juan.quienquieresermillonario.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.juan.quienquieresermillonario.R
import com.juan.quienquieresermillonario.nav.Rutas
import com.juan.quienquieresermillonario.servicios.Pregunta
import com.juan.quienquieresermillonario.ui.theme.baby_Blue
import com.juan.quienquieresermillonario.ui.theme.blue_Grotto
import com.juan.quienquieresermillonario.ui.theme.danger
import com.juan.quienquieresermillonario.ui.theme.good
import com.juan.quienquieresermillonario.ui.theme.orange
import com.juan.quienquieresermillonario.ui.theme.purple_700
import com.juan.quienquieresermillonario.ui.theme.white
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class PantallaPreguntas : ViewModel() {
    var iniciarJuego = mutableStateOf(false)
    var dialogoAbierto = mutableStateOf(false)
    var esCorrecta = mutableStateOf(false)
    var estaPulsado = mutableStateOf(false)
    var preguntasAcertadas = 0
    var preguntasFalladas = 0
    var indice = mutableIntStateOf(0)
    var textoRespuesta = mutableStateOf("")
    var numeroPreguntas = 0
    var numeroClicks = 0
    var indiceImagenes = mutableIntStateOf(0)

    @Composable
    fun Inicio(
        listaPreguntas: List<Pregunta>,
        listaImagenes: List<Painter>,
        navController: NavController
    ) {
        SeleccionaNumeroPreguntas(this, listaPreguntas, navController)
        if (iniciarJuego.value) {
            PantallaPregunta(listaPreguntas, listaImagenes, this, navController)
        }
    }
}
/*TODO Genera una interfaz para cuando la orientación del teléfono sea lateral (landscape) para el componente de juego, e implementa las funciones necesarias para que al girar la pantalla no pierdas información.*/
/*TODO meter los sonidos del juego*/

@Composable
private fun SeleccionaNumeroPreguntas(
    miViewModel: PantallaPreguntas,
    listaPreguntas: List<Pregunta>,
    navController: NavController
) {
    var numeroPreguntas by remember { mutableStateOf("") }
    var dialogoAbierto by remember { mutableStateOf(true) }
    val totalPreguntas = listaPreguntas.size
    val context = LocalContext.current
    if (dialogoAbierto) {
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        AlertDialog(
            onDismissRequest = {
                navController.popBackStack()
            },
            title = {
                Text(
                    "Selecciona el número de preguntas: ",
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column {
                    Text("Número máximo de preguntas: $totalPreguntas")
                    TextField(
                        value = numeroPreguntas,
                        onValueChange = { numeroPreguntas = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        numeroPreguntas = listaPreguntas.size.toString()
                    },
                    colors = ButtonDefaults.buttonColors(blue_Grotto)
                ) {
                    Text("Max")
                }
                Button(
                    onClick = {
                        val numPreguntas = numeroPreguntas.toInt()
                        if (numPreguntas in 1..totalPreguntas) {
                            miViewModel.numeroPreguntas = numPreguntas
                            miViewModel.iniciarJuego.value = true
                            dialogoAbierto = false
                        } else {
                            if (numPreguntas > totalPreguntas) {
                                val text = "Número demasiado alto."
                                val duration = Toast.LENGTH_SHORT
                                Toast.makeText(context, text, duration).show()
                            } else if (numPreguntas < 0) {
                                val text = "No me vas a pillar con un número negativo."
                                val duration = Toast.LENGTH_SHORT
                                Toast.makeText(context, text, duration).show()
                            } else if (numPreguntas == 0) {
                                val text = "No puedo mostrar cero preguntas."
                                val duration = Toast.LENGTH_SHORT
                                Toast.makeText(context, text, duration).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(blue_Grotto)
                ) {
                    Text("Empezar")
                }
            }
        )
    }
}

@Composable
private fun PantallaPregunta(
    listaPreguntas: List<Pregunta>,
    listaImagenes: List<Painter>,
    miViewModel: PantallaPreguntas,
    navController: NavController
) {
    val imagen = listaImagenes[miViewModel.indiceImagenes.intValue]
    Image(
        painter = painterResource(id = R.drawable.fondo),
        contentDescription = "Imagen de fondo",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.size(100.dp))
        Image(
            painter = imagen,
            contentDescription = "sobera",
            Modifier
                .padding(10.dp)
                .size(200.dp)
        )
        TarjetaTextoPregunta(listaPreguntas[miViewModel.indice.intValue].pregunta)
        BotonesRespuesta(listaPreguntas[miViewModel.indice.intValue], miViewModel)
        BotonesNavegacion(miViewModel)
        Spacer(modifier = Modifier.size(50.dp))
        MuestraMensajeRespuesta(
            listaPreguntas[miViewModel.indice.intValue],
            miViewModel,
            navController
        )
    }
}

@Composable
fun TarjetaTextoPregunta(texto: String) {
    Card(
        border = BorderStroke(2.dp, white),
        modifier = Modifier
            .padding(15.dp, 10.dp)
            .height(50.dp)
    ) {
        Surface(color = purple_700) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = texto,
                    color = white,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun BotonesRespuesta(pregunta: Pregunta, miViewModel: PantallaPreguntas) {
    Row {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .padding(14.dp, 0.dp)
        ) {
            Boton(pregunta.opcionA, pregunta.respuesta, miViewModel)
            Boton(pregunta.opcionC, pregunta.respuesta, miViewModel)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .padding(14.dp, 0.dp)
        ) {
            Boton(pregunta.opcionB, pregunta.respuesta, miViewModel)
            Boton(pregunta.opcionD, pregunta.respuesta, miViewModel)
        }
    }
}

@Composable
private fun BotonesNavegacion(miViewModel: PantallaPreguntas) {
    val context = LocalContext.current
    Row(Modifier.padding(0.dp, 50.dp, 0.dp, 0.dp)) {
        Button(
            onClick = {
                miViewModel.numeroClicks++
                if (!miViewModel.estaPulsado.value) {
                    if (miViewModel.indice.intValue != 0) {
                        miViewModel.indice.intValue = miViewModel.indice.intValue - 1
                        miViewModel.indiceImagenes.intValue = Random.nextInt(0, 10)
                    } else {
                        val text = "No hay preguntas previas."
                        val duration = Toast.LENGTH_SHORT
                        Toast.makeText(context, text, duration).show()
                    }
                }
            },
            border = BorderStroke(1.dp, white),
            colors = ButtonDefaults.buttonColors(baby_Blue),
            modifier = Modifier.padding(20.dp, 0.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Botón volver"
            )
        }
        Button(
            onClick = {
                if (!miViewModel.estaPulsado.value) {
                    miViewModel.numeroClicks++
                    miViewModel.indice.intValue =
                        Random.nextInt(0, miViewModel.numeroPreguntas)
                    miViewModel.indiceImagenes.intValue = Random.nextInt(0, 10)
                }
            },
            border = BorderStroke(1.dp, white),
            colors = ButtonDefaults.buttonColors(baby_Blue),
            modifier = Modifier.padding(20.dp, 0.dp)
        ) {
            Text(text = "Aleatoria")
        }
        Button(
            onClick = {
                miViewModel.numeroClicks++
                if (!miViewModel.estaPulsado.value) {
                    if (miViewModel.indice.intValue < miViewModel.numeroPreguntas - 1) {
                        miViewModel.indice.intValue = miViewModel.indice.intValue + 1
                        miViewModel.indiceImagenes.intValue = Random.nextInt(0, 10)
                    } else {
                        val text = "No hay más preguntas."
                        val duration = Toast.LENGTH_SHORT
                        Toast.makeText(context, text, duration).show()
                    }

                }
            },
            border = BorderStroke(1.dp, white),
            colors = ButtonDefaults.buttonColors(baby_Blue),
            modifier = Modifier.padding(20.dp, 0.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Botón siguiente"
            )
        }
    }
}

@Composable
private fun Boton(
    texto: String,
    respuesta: String,
    miViewModel: PantallaPreguntas
) {
    val funcionDelay = rememberCoroutineScope()
    var color by remember { mutableStateOf(blue_Grotto) }
    ElevatedButton(
        onClick = {
            miViewModel.numeroClicks++
            if (!miViewModel.estaPulsado.value) {
                miViewModel.estaPulsado.value = true
                color = orange
                funcionDelay.launch(Dispatchers.Main) {
                    delay(3500)
                    color = compruebaRespuesta(
                        texto,
                        respuesta,
                        miViewModel
                    )
                    miViewModel.dialogoAbierto.value = true
                    delay(1000)
                    color = blue_Grotto
                }
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(color),
        border = BorderStroke(1.dp, white)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 3.dp)
        ) {
            Text(
                text = texto,
                color = white,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MuestraMensajeRespuesta(
    pregunta: Pregunta,
    miViewModel: PantallaPreguntas,
    navController: NavController
) {
    if (miViewModel.dialogoAbierto.value) {
        if (miViewModel.esCorrecta.value) {
            miViewModel.textoRespuesta.value =
                "CORRECTA!!! la respuesta es la ${pregunta.respuesta}"
            miViewModel.preguntasAcertadas += 1
        } else {
            miViewModel.textoRespuesta.value =
                "Noooo, la respuesta correcta es la ${pregunta.respuesta}"
            miViewModel.preguntasFalladas += 1
        }
        AlertDialog(
            onDismissRequest = {
                miViewModel.numeroClicks++
                onClickDialogo(miViewModel, navController)
            },
            title = { Text("Y la respuesta es...") },
            text = { Text(miViewModel.textoRespuesta.value) },
            confirmButton = {
                Button(
                    onClick = {
                        miViewModel.numeroClicks++
                        onClickDialogo(miViewModel, navController)
                    },
                    colors = ButtonDefaults.buttonColors(blue_Grotto)
                ) {
                    Text("Siguiente")
                }
            }
        )
    }
}

private fun onClickDialogo(miViewModel: PantallaPreguntas, navController: NavController) {
    if (miViewModel.indice.intValue < miViewModel.numeroPreguntas - 1) miViewModel.indice.intValue =
        miViewModel.indice.intValue + 1 else muestraEstadistica(navController)
    miViewModel.dialogoAbierto.value = false
    miViewModel.esCorrecta.value = false
    miViewModel.estaPulsado.value = false
    miViewModel.indiceImagenes.intValue = Random.nextInt(0, 10)
}

private fun compruebaRespuesta(
    texto: String,
    respuesta: String,
    miViewModel: PantallaPreguntas
): Color {
    if (texto.contains(respuesta)) {
        miViewModel.esCorrecta.value = true
        return good
    } else {
        return danger
    }
}

private fun muestraEstadistica(navController: NavController) {
    navController.navigate(Rutas.Estadisticas.ruta)
}
