package com.juan.quienquieresermillonario.ui

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.juan.quienquieresermillonario.R
import com.juan.quienquieresermillonario.servicios.Pregunta
import com.juan.quienquieresermillonario.ui.theme.blue_Grotto
import com.juan.quienquieresermillonario.ui.theme.danger
import com.juan.quienquieresermillonario.ui.theme.good
import com.juan.quienquieresermillonario.ui.theme.orange
import com.juan.quienquieresermillonario.ui.theme.purple40
import com.juan.quienquieresermillonario.ui.theme.white
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class PantallaCompetitivo : ViewModel() {
    var muestraMensajeFinal = mutableStateOf(false)
    var rondaPerdida = mutableStateOf(false)
    var laRespuestaEra = mutableStateOf("")
    var dialogoAbierto = mutableStateOf(false)
    var esCorrecta = mutableStateOf(false)
    var estaPulsado = mutableStateOf(false)
    var comodinLlamada = mutableStateOf(false)
    var comodinLlamadaDisponible = mutableStateOf(true)
    var comodin50 = mutableStateOf(false)
    var desactivaResaltarOpcionesComodin50 = mutableStateOf(false)
    var comodin50Disponible = mutableStateOf(true)
    var resaltarOpcionA = mutableStateOf(false)
    var resaltarOpcionB = mutableStateOf(false)
    var resaltarOpcionC = mutableStateOf(false)
    var resaltarOpcionD = mutableStateOf(false)
    var comodinPublico = mutableStateOf(false)
    var comodinPublicoDisponible = mutableStateOf(true)
    var indice = mutableIntStateOf(0)
    var numeroPreguntas = mutableIntStateOf(0)
    var indiceImagenes = mutableIntStateOf(0)

    @Composable
    fun Inicio(
        listaPreguntas: List<Pregunta>,
        listaImagenes: List<Painter>,
        navController: NavController
    ) {
        JuegoCompetitivo(listaPreguntas, listaImagenes, this, navController)
    }
}

@Composable
fun JuegoCompetitivo(
    listaPreguntas: List<Pregunta>,
    listaImagenes: List<Painter>,
    miViewModel: PantallaCompetitivo,
    navController: NavController
) {
    val imagen = listaImagenes[miViewModel.indiceImagenes.intValue]
    val context = LocalContext.current
    val abuelaPaqui = MediaPlayer.create(context, R.raw.grabacion)

    miViewModel.numeroPreguntas.intValue = listaPreguntas.size
    Image(
        painter = painterResource(id = R.drawable.fondo),
        contentDescription = "Descripción de la imagen",
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
        MuestraMensajeRespuesta(listaPreguntas[miViewModel.indice.intValue], miViewModel)
        MuestraComodinLlamada(
            "Comodín de la llamada",
            listaPreguntas[miViewModel.indice.intValue].respuesta,
            miViewModel,
            abuelaPaqui
        )
        MuestraComodin50(
            listaPreguntas[miViewModel.indice.intValue],
            miViewModel
        )
        MuestraComodinPublico(
            "Comodín del público",
            miViewModel,
            listaPreguntas[miViewModel.indice.intValue]
        )
        BotonesComodin(miViewModel)
        Spacer(modifier = Modifier.size(50.dp))
        if (miViewModel.muestraMensajeFinal.value) FinalDeRonda(navController)
        if (miViewModel.rondaPerdida.value) RondaPerdida(miViewModel, navController)
    }
}

@Composable
private fun BotonesRespuesta(pregunta: Pregunta, miViewModel: PantallaCompetitivo) {
    Row {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .padding(14.dp, 0.dp)
        ) {
            Boton(pregunta.opcionA, pregunta, miViewModel)
            Boton(pregunta.opcionC, pregunta, miViewModel)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .padding(14.dp, 0.dp)
        ) {
            Boton(pregunta.opcionB, pregunta, miViewModel)
            Boton(pregunta.opcionD, pregunta, miViewModel)
        }
    }
}

@Composable
private fun Boton(
    texto: String,
    pregunta: Pregunta,
    miViewModel: PantallaCompetitivo
) {
    val funcionDelay = rememberCoroutineScope()
    var color by remember { mutableStateOf(blue_Grotto) }
    var colorTexto = white

    if (miViewModel.desactivaResaltarOpcionesComodin50.value) {
        when (texto) {
            pregunta.opcionA -> if (miViewModel.resaltarOpcionA.value) colorTexto = blue_Grotto
            pregunta.opcionB -> if (miViewModel.resaltarOpcionB.value) colorTexto = blue_Grotto
            pregunta.opcionC -> if (miViewModel.resaltarOpcionC.value) colorTexto = blue_Grotto
            pregunta.opcionD -> if (miViewModel.resaltarOpcionD.value) colorTexto = blue_Grotto
        }
    }
    ElevatedButton(
        onClick = {
            miViewModel.desactivaResaltarOpcionesComodin50.value = false
            if (!miViewModel.estaPulsado.value) {
                miViewModel.estaPulsado.value = true
                miViewModel.comodin50.value = false

                color = orange
                funcionDelay.launch(Dispatchers.Main) {
                    delay(3500)
                    color = compruebaRespuesta(
                        texto,
                        pregunta.respuesta,
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
                color = colorTexto,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
private fun MuestraMensajeRespuesta(pregunta: Pregunta, miViewModel: PantallaCompetitivo) {
    if (miViewModel.dialogoAbierto.value) {
        if (miViewModel.esCorrecta.value) {
            AlertDialog(
                onDismissRequest = {
                    onClickDialogo(miViewModel)
                },
                title = { Text("Y la respuesta es...") },
                text = { Text("CORRECTA!!! la respuesta era la ${pregunta.respuesta}") },
                confirmButton = {
                    Button(
                        onClick = {
                            onClickDialogo(miViewModel)
                        },
                        colors = ButtonDefaults.buttonColors(blue_Grotto)
                    ) {
                        Text("Siguiente")
                    }
                }
            )
        } else {
            miViewModel.laRespuestaEra.value = pregunta.respuesta
            miViewModel.rondaPerdida.value = true
        }
    }
}

@Composable
private fun BotonesComodin(miViewModel: PantallaCompetitivo) {
    Row(Modifier.padding(0.dp, 50.dp, 0.dp, 0.dp)) {
        Comodin("Comodín de la llamada", painterResource(id = R.drawable.call), miViewModel)
        Comodin("Comodín del 50%", painterResource(id = R.drawable.half), miViewModel)
        Comodin("Comodín del público", painterResource(id = R.drawable.groups), miViewModel)
    }
}

@Composable
private fun Comodin(comodin: String, imagen: Painter, miViewModel: PantallaCompetitivo) {
    Button(
        onClick = {
            when (comodin) {
                "Comodín de la llamada" -> miViewModel.comodinLlamada.value = true
                "Comodín del 50%" -> miViewModel.comodin50.value = true
                "Comodín del público" -> miViewModel.comodinPublico.value = true
            }
        },
        border = BorderStroke(1.dp, white),
        colors = ButtonDefaults.buttonColors(purple40),
        modifier = Modifier.padding(20.dp, 0.dp)
    ) {
        Image(imagen, contentDescription = comodin)
    }
}

@Composable
private fun MuestraComodinLlamada(
    comodin: String,
    respuesta: String,
    miViewModel: PantallaCompetitivo,
    abuelaPaqui: MediaPlayer
) {
    if (miViewModel.comodinLlamadaDisponible.value) {
        if (miViewModel.comodinLlamada.value) {
            AlertDialog(
                onDismissRequest = {
                    miViewModel.comodinLlamada.value = false
                    miViewModel.comodinLlamadaDisponible.value = false
                },
                title = { Text(comodin) },
                text = {
                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.comodindelallamada),
                            contentDescription = ""
                        )
                        Text("\nMe dice el sobrino de Mari Pili que la respuesta es la $respuesta, que el sabe mucho, estudió en Jaén con el hijo de Herminia...")
                        abuelaPaqui.start()
                    }

                },
                confirmButton = {
                    Button(
                        onClick = {
                            miViewModel.comodinLlamada.value = false
                            miViewModel.comodinLlamadaDisponible.value = false
                        },
                        colors = ButtonDefaults.buttonColors(blue_Grotto)
                    ) {
                        Text("Responder")
                    }
                }
            )
        }
    }
}

@Composable
private fun MuestraComodin50(
    pregunta: Pregunta,
    miViewModel: PantallaCompetitivo
) {

    if (miViewModel.comodin50Disponible.value) {
        if (miViewModel.comodin50.value) {
            miViewModel.desactivaResaltarOpcionesComodin50.value = true
            miViewModel.comodin50Disponible.value = false
            opcionIncorrecta(pregunta, miViewModel)
            opcionIncorrecta(pregunta, miViewModel)
        }
    }
}

private fun opcionIncorrecta(pregunta: Pregunta, miViewModel: PantallaCompetitivo) {
    var check = true
    while (check) {
        when (Random.nextInt(1, 5)) {
            1 -> if (pregunta.respuesta != pregunta.opcionA) {
                if (!miViewModel.resaltarOpcionA.value) {
                    miViewModel.resaltarOpcionA.value = true
                    check = false
                }
            }

            2 -> if (pregunta.respuesta != pregunta.opcionB) {
                if (!miViewModel.resaltarOpcionB.value) {
                    miViewModel.resaltarOpcionB.value = true
                    check = false
                }
            }

            3 -> if (pregunta.respuesta != pregunta.opcionC) {
                if (!miViewModel.resaltarOpcionC.value) {
                    miViewModel.resaltarOpcionC.value = true
                    check = false
                }
            }

            4 -> if (pregunta.respuesta != pregunta.opcionD) {
                if (!miViewModel.resaltarOpcionD.value) {
                    miViewModel.resaltarOpcionD.value = true
                    check = false
                }
            }
        }
    }
}

@Composable
private fun MuestraComodinPublico(
    comodin: String,
    miViewModel: PantallaCompetitivo,
    pregunta: Pregunta
) {
    if (miViewModel.comodinPublicoDisponible.value) {
        if (miViewModel.comodinPublico.value) {
            val respuestaPublico = respuestaComodinPublico(pregunta)
            AlertDialog(
                onDismissRequest = {
                    miViewModel.comodinPublico.value = false
                    miViewModel.comodinPublicoDisponible.value = false
                },
                title = { Text(comodin) },
                text = {
                    Column {
                        Text(respuestaPublico)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            miViewModel.comodinPublico.value = false
                            miViewModel.comodinPublicoDisponible.value = false
                        },
                        colors = ButtonDefaults.buttonColors(blue_Grotto)
                    ) {
                        Text("Responder")
                    }
                }
            )
        }
    }
}

private fun respuestaComodinPublico(pregunta: Pregunta): String {
    var respuesta = ""
    var linea1 = pregunta.opcionA
    var linea2 = pregunta.opcionB
    var linea3 = pregunta.opcionC
    var linea4 = pregunta.opcionD
    when (pregunta.respuesta) {
        pregunta.opcionA -> {
            linea1 = "------ 54% ${pregunta.opcionA}\n"; respuesta =
                linea1 + restoRespuestaComodinPublico(linea2, linea3, linea4)
        }

        pregunta.opcionB -> {
            linea2 = "------ 54% ${pregunta.opcionB}\n"; respuesta =
                linea2 + restoRespuestaComodinPublico(linea1, linea3, linea4)
        }

        pregunta.opcionC -> {
            linea3 = "------ 54% ${pregunta.opcionC}\n"; respuesta =
                linea3 + restoRespuestaComodinPublico(linea1, linea2, linea4)
        }

        pregunta.opcionD -> {
            linea4 = "------ 54% ${pregunta.opcionD}\n"; respuesta =
                linea4 + restoRespuestaComodinPublico(linea1, linea2, linea3)
        }
    }
    return respuesta
}

private fun restoRespuestaComodinPublico(linea1: String, linea2: String, linea3: String): String {
    val restoRespuesta1 = "--     10% $linea1\n"
    val restoRespuesta2 = "---    31% $linea2\n"
    val restoRespuesta3 = "-      5%  $linea3\n"
    return restoRespuesta1 + restoRespuesta2 + restoRespuesta3
}

private fun onClickDialogo(miViewModel: PantallaCompetitivo) {
    if (miViewModel.indice.intValue < miViewModel.numeroPreguntas.intValue - 1) {
        miViewModel.indice.intValue = miViewModel.indice.intValue + 1
        miViewModel.indiceImagenes.intValue = Random.nextInt(0, 10)

        miViewModel.resaltarOpcionA.value = false
        miViewModel.resaltarOpcionB.value = false
        miViewModel.resaltarOpcionC.value = false
        miViewModel.resaltarOpcionD.value = false
    } else {
        miViewModel.muestraMensajeFinal.value = true
    }
    miViewModel.dialogoAbierto.value = false
    miViewModel.esCorrecta.value = false
    miViewModel.estaPulsado.value = false
}

@Composable
private fun FinalDeRonda(navController: NavController) {
    AlertDialog(
        onDismissRequest = {
            navController.popBackStack()
        },
        title = { Text("ERES MILLONARIO!!!") },
        text = {
            Column {
                Text("Has terminado con éxito todas las preguntas de Quien quiere ser millonario y has ganado el premio del millón de euros!!\n(Estamos experimentando algunos problemas para realizar el pago.)")
            }

        },
        confirmButton = {
            Button(
                onClick = {
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(blue_Grotto)
            ) {
                Text("Volver al menú principal")
            }
        }
    )
}

@Composable
private fun RondaPerdida(miViewModel: PantallaCompetitivo, navController: NavController) {
    AlertDialog(
        onDismissRequest = {
            navController.popBackStack()
        },
        title = { Text("Ronda perdida") },
        text = {
            Column {
                Text("Ohhhh, lamentablemente has fallado la pregunta y has quedado eliminado. La respuesta correcta era la ${miViewModel.laRespuestaEra.value} \nPuedes volver a intentarlo o practicar en la ronda amistosa.")
            }

        },
        confirmButton = {
            Button(
                onClick = {
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(blue_Grotto)
            ) {
                Text("Volver al menú principal")
            }
        }
    )
}

private fun compruebaRespuesta(
    texto: String,
    respuesta: String,
    miViewModel: PantallaCompetitivo
): Color {
    if (texto.contains(respuesta)) {
        miViewModel.esCorrecta.value = true

        return good
    } else {
        return danger
    }
}
