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
import com.juan.quienquieresermillonario.leerArchivo
import com.juan.quienquieresermillonario.ui.theme.baby_Blue
import com.juan.quienquieresermillonario.ui.theme.blue_Grotto
import com.juan.quienquieresermillonario.ui.theme.orange
import com.juan.quienquieresermillonario.ui.theme.white
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter

class ModPreguntas : ViewModel() {
    @Composable
    fun Inicio(navController: NavController) {
        PantallaMuestraPregunta(navController)
    }
}

@Composable
private fun PantallaMuestraPregunta(navController: NavController) {
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
        val listaPreguntas = leerArchivo(context)
        val preguntaYRespuestasViejas = remember { mutableStateOf("") }
        val preguntaYRespuestasNuevas = remember { mutableStateOf("") }
        val textoPregunta = remember { mutableStateOf("") }
        val opcionA = remember { mutableStateOf("A: ") }
        val opcionB = remember { mutableStateOf("B: ") }
        val opcionC = remember { mutableStateOf("C: ") }
        val opcionD = remember { mutableStateOf("D: ") }
        val respuesta = remember { mutableStateOf("") }
        val expanded = remember { mutableStateOf(false) }

        TextButton(
            modifier = Modifier.padding(20.dp),
            border = BorderStroke(1.dp, white),
            colors = ButtonDefaults.buttonColors(baby_Blue),
            onClick = { expanded.value = true }) {
            Text(
                color = white,
                textAlign = TextAlign.Center,
                text = "Pulsa para seleccionar la pregunta que quieres modificar / eliminar."
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
        CampoDeTexto(texto = textoPregunta, descripcion = "Introduce la pregunta: ", 65)
        CampoDeTexto(texto = opcionA, descripcion = "Introduce la primera opción: ", 13)
        CampoDeTexto(texto = opcionB, descripcion = "Introduce la segunda opción: ", 13)
        CampoDeTexto(texto = opcionC, descripcion = "Introduce la tercera opción: ", 13)
        CampoDeTexto(texto = opcionD, descripcion = "Introduce la cuarta opción: ", 13)

        Row {
            Button(
                onClick = {
                    verificaYAlmacenaPreguntas(
                        navController,
                        context,
                        textoPregunta.value,
                        opcionA.value,
                        opcionB.value,
                        opcionC.value,
                        opcionD.value,
                        respuesta.value
                    )
                    preguntaYRespuestasNuevas.value =
                        "${textoPregunta.value},${opcionA.value},${opcionB.value},${opcionC.value},${opcionD.value},${respuesta.value}"
                    println(preguntaYRespuestasViejas.value)
                    println(preguntaYRespuestasNuevas.value)
                    modificaLinea(
                        preguntaYRespuestasViejas.value,
                        preguntaYRespuestasNuevas.value,
                        context
                    )
                },
                border = BorderStroke(1.dp, white),
                colors = ButtonDefaults.buttonColors(blue_Grotto),
                modifier = Modifier.padding(10.dp, 0.dp)
            ) {
                Text("Guardar cambios")
            }

            Button(
                onClick = {
                    eliminaLinea(preguntaYRespuestasViejas.value, context)
                    val texto = "Pregunta eliminada con éxito."
                    val duration = Toast.LENGTH_SHORT
                    Toast.makeText(context, texto, duration).show()
                    navController.popBackStack()
                },
                border = BorderStroke(1.dp, white),
                colors = ButtonDefaults.buttonColors(blue_Grotto)
            ) {
                Text("Eliminar")
            }
        }
    }
}

@Composable
private fun CampoDeTexto(
    texto: MutableState<String>,
    descripcion: String,
    maximo: Int
) {
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
    }
}

fun verificaYAlmacenaPreguntas(
    navController: NavController,
    context: Context,
    pregunta: String,
    opcionA: String,
    opcionB: String,
    opcionC: String,
    opcionD: String,
    respuesta: String
) {
    if (!pregunta.isEmpty() &&
        !respuesta.isEmpty() &&
        !opcionA.isEmpty() &&
        !opcionB.isEmpty() &&
        !opcionC.isEmpty() &&
        !opcionD.isEmpty()
    ) {
        val contenido =
            "$pregunta,$opcionA,$opcionB,$opcionC,$opcionD,$respuesta"
        val fileOutputStream: FileOutputStream =
            context.openFileOutput("preguntas.txt", Context.MODE_APPEND)
        fileOutputStream.write("$contenido\n".toByteArray())
        fileOutputStream.close()
        val texto = "Pregunta guardada con éxito."
        val duration = Toast.LENGTH_SHORT
        Toast.makeText(context, texto, duration).show()
        navController.popBackStack()
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
}

// Este método lo tengo como auxiliar para eliminar preguntas. No lo he hecho yo y si
// me preguntas por como funciona alguna cosa concreta probablemente no sepa responder
private fun eliminaLinea(lineaNoDeseada: String, context: Context) {
    val file = File(context.filesDir, "preguntas.txt")
    val reader = BufferedReader(FileReader(file))

    val fileTemporal = File.createTempFile("buffer", null)
    val writer = BufferedWriter(FileWriter(fileTemporal))

    var currentLine: String?

    while (reader.readLine().also { currentLine = it } != null) {
        // trim newline when comparing with lineToRemove
        val trimmedLine = currentLine!!.trim()
        if (trimmedLine == lineaNoDeseada) continue
        writer.write(currentLine + System.getProperty("line.separator"))
    }
    writer.close()
    reader.close()

    //Delete the original file
    if (!file.delete()) {
        //Failed to delete the file
        println("Could not delete file")
        return
    }

    //Rename the new file to the filename the original file had.
    if (!fileTemporal.renameTo(file))
        println("Could not rename file")
}

private fun modificaLinea(lineaVieja: String, lineaNueva: String, context: Context) {
    val file = File(context.filesDir, "preguntas.txt")
    val reader = BufferedReader(FileReader(file))

    val fileTemporal = File.createTempFile("buffer", null)
    val writer = BufferedWriter(FileWriter(fileTemporal))

    var currentLine: String?

    while (reader.readLine().also { currentLine = it } != null) {
        val trimmedLine = currentLine!!.trim()
        if (trimmedLine == lineaVieja) {
            writer.write(lineaNueva + System.getProperty("line.separator"))
        } else {
            writer.write(currentLine + System.getProperty("line.separator"))
        }
    }
    writer.close()
    reader.close()

    if (!file.delete()) {
        println("No se pudo eliminar el archivo.")
        return
    }

    if (!fileTemporal.renameTo(file))
        println("No se pudo renombrar el archivo.")
}
