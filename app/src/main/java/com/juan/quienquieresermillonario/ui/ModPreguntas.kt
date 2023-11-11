package com.juan.quienquieresermillonario.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.juan.quienquieresermillonario.leerArchivo
import com.juan.quienquieresermillonario.ui.theme.baby_Blue
import com.juan.quienquieresermillonario.ui.theme.blue_Grotto
import com.juan.quienquieresermillonario.ui.theme.orange
import com.juan.quienquieresermillonario.ui.theme.shadow
import com.juan.quienquieresermillonario.ui.theme.white
import java.io.File

class ModPreguntas : ViewModel() {
    @Composable
    fun Inicio(navController: NavController) {
        PantallaMuestraPregunta(navController)
    }
}

@Composable
private fun PantallaMuestraPregunta(navController: NavController) {
    val context = LocalContext.current
    val orangeTransparente = orange.copy(alpha = 0.85f)
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
            .background(color = orangeTransparente, shape = RoundedCornerShape(20.dp))
            .border(width = 3.dp, color = white, shape = RoundedCornerShape(20.dp))

    ) {
        val listaPreguntas = leerArchivo(context)
        val preguntaYRespuestasViejas = remember { mutableStateOf("") }
        val preguntaYRespuestasNuevas = remember { mutableStateOf("") }
        val textoPregunta = remember { mutableStateOf("") }
        val opcionA = remember { mutableStateOf("A: ") }
        val opcionB = remember { mutableStateOf("B: ") }
        val opcionC = remember { mutableStateOf("C: ") }
        val opcionD = remember { mutableStateOf("D: ") }
        val respuesta = remember { mutableStateOf(" ") }
        val expanded = remember { mutableStateOf(false) }

        Text(
            text = "Modificar preguntas",
            color = shadow,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        CampoDeTexto(texto = textoPregunta, descripcion = "Pregunta: ", 65)
        CampoDeTexto(texto = opcionA, descripcion = "Primera opción: ", 13)
        CampoDeTexto(texto = opcionB, descripcion = "Segunda opción: ", 13)
        CampoDeTexto(texto = opcionC, descripcion = "Tercera opción: ", 13)
        CampoDeTexto(texto = opcionD, descripcion = "Cuarta opción: ", 13)
        when (respuesta.value[0]) {
            'A' -> { respuesta.value = opcionA.value }
            'B' -> { respuesta.value = opcionB.value }
            'C' -> { respuesta.value = opcionC.value }
            'D' -> { respuesta.value = opcionD.value }
        }

        TextButton(
            modifier = Modifier.padding(20.dp),
            border = BorderStroke(1.dp, white),
            colors = ButtonDefaults.buttonColors(baby_Blue),
            onClick = { expanded.value = true }) {
            Text(
                color = white,
                textAlign = TextAlign.Center,
                text = "Pulsa para seleccionar la pregunta que quieres modificar."
            )
            DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
                listaPreguntas.forEach { pregunta ->
                    DropdownMenuItem(
                        text = { Text(pregunta.pregunta) },
                        onClick = {
                            preguntaYRespuestasViejas.value =
                                "${pregunta.pregunta},${pregunta.opcionA},${pregunta.opcionB},${pregunta.opcionC},${pregunta.opcionD},${pregunta.respuesta}"
                            textoPregunta.value = pregunta.pregunta
                            opcionA.value = pregunta.opcionA
                            opcionB.value = pregunta.opcionB
                            opcionC.value = pregunta.opcionC
                            opcionD.value = pregunta.opcionD
                            respuesta.value = pregunta.respuesta
                            expanded.value = false
                        }
                    )
                }
            }
        }
        Row {
            Button(
                onClick = {
                    val resultadoVerificacion = verificaPreguntas(
                        context,
                        textoPregunta.value,
                        opcionA.value,
                        opcionB.value,
                        opcionC.value,
                        opcionD.value,
                        respuesta.value
                    )
                    if (resultadoVerificacion != "error") {
                        preguntaYRespuestasNuevas.value =
                            "${textoPregunta.value},${opcionA.value},${opcionB.value},${opcionC.value},${opcionD.value},${respuesta.value}"
                        modificaLinea(
                            preguntaYRespuestasViejas.value,
                            preguntaYRespuestasNuevas.value,
                            context,
                            navController
                        )
                    }
                },
                border = BorderStroke(1.dp, white),
                colors = ButtonDefaults.buttonColors(blue_Grotto),
                modifier = Modifier.padding(10.dp, 0.dp)
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
}

fun verificaPreguntas(
    context: Context,
    pregunta: String,
    opcionA: String,
    opcionB: String,
    opcionC: String,
    opcionD: String,
    respuesta: String
): String {
    if (
        !pregunta.isEmpty() &&
        !respuesta.isEmpty() &&
        !opcionA.isEmpty() &&
        !opcionB.isEmpty() &&
        !opcionC.isEmpty() &&
        !opcionD.isEmpty()
    ) {
        return "$pregunta,$opcionA,$opcionB,$opcionC,$opcionD,$respuesta"
    } else if (pregunta.isEmpty()) {
        val texto = "Falta la pregunta."
        val duration = Toast.LENGTH_LONG
        Toast.makeText(context, texto, duration).show()
    } else if (respuesta.isEmpty()) {
        val texto = "Falta la respuesta correcta."
        val duration = Toast.LENGTH_LONG
        Toast.makeText(context, texto, duration).show()
    } else if (opcionA.isEmpty()) {
        val texto = "Falta la opción A."
        val duration = Toast.LENGTH_LONG
        Toast.makeText(context, texto, duration).show()
    } else if (opcionB.isEmpty()) {
        val texto = "Falta la opción B."
        val duration = Toast.LENGTH_LONG
        Toast.makeText(context, texto, duration).show()
    } else if (opcionC.isEmpty()) {
        val texto = "Falta la opción C."
        val duration = Toast.LENGTH_LONG
        Toast.makeText(context, texto, duration).show()
    } else if (opcionD.isEmpty()) {
        val texto = "Falta la opción D."
        val duration = Toast.LENGTH_LONG
        Toast.makeText(context, texto, duration).show()
    }
    return "error"
}

private fun modificaLinea(
    lineaVieja: String,
    lineaNueva: String,
    context: Context,
    navController: NavController
) {
    val file = File(context.filesDir, "preguntas.txt")
    val lines = file.readLines().toMutableList()
    val index = lines.indexOfFirst { it == lineaVieja }
    if (index != -1) {
        lines[index] = lineaNueva
    }

    /*  El 'lines.joinToString(separator = "\n")' sirve para recoger todos los elementos
     de la lista 'lines' en un solo String separando cada elemento con un salto de linea.
      El 'file.writeText' sirve para sobreescribir el contenido del archivo que tenía
      con el nuevo String que ha generado.*/
    file.writeText(lines.joinToString(separator = "\n"))
    val texto = "Pregunta guardada con éxito."
    val duration = Toast.LENGTH_SHORT
    Toast.makeText(context, texto, duration).show()
    navController.popBackStack()
}
