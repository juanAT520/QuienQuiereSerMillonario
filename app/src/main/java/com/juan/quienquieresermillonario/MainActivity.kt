package com.juan.quienquieresermillonario

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.juan.quienquieresermillonario.nav.GrafoNavegacion
import com.juan.quienquieresermillonario.nav.Rutas
import com.juan.quienquieresermillonario.servicios.Pregunta
import com.juan.quienquieresermillonario.ui.theme.orange
import com.juan.quienquieresermillonario.ui.theme.white
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                leerArchivoAssetsYCrearEnMemoria(LocalContext.current)
                GrafoNavegacion()
            }
        }
    }
}

@Composable
fun MenuPrincipal(navController: NavHostController) {
    Image(
        painter = painterResource(id = R.drawable.fondo),
        contentDescription = "Imagen de fondo",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TarjetaMenu("Ronda amistosa") { navController.navigate(Rutas.Amistoso.ruta) }
        TarjetaMenu("Ronda competitiva") { navController.navigate(Rutas.Competitivo.ruta) }
        TarjetaMenu("Estadísticas") { navController.navigate(Rutas.Estadisticas.ruta) }
        TarjetaMenu("Añadir pregunta") { navController.navigate(Rutas.AddPregunta.ruta) }
    }
}

@Composable
fun TarjetaMenu(texto: String, funcion: () -> Unit) {
    Card(
        border = BorderStroke(3.dp, white),
        modifier = Modifier
            .padding(45.dp, 25.dp)
            .height(50.dp)
            .fillMaxWidth()
            .clickable { funcion() }
    ) {
        Surface(color = orange) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = texto,
                    color = white,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

fun leerArchivo(context: Context): List<Pregunta> {
    val file = File(context.filesDir, "preguntas.txt")
    val inputStream = FileInputStream(file)
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    val listaDePreguntas = mutableListOf<Pregunta>()

    bufferedReader.forEachLine { linea ->
        val datos = linea.split(",")
        if (datos.size == 6) {
            val pregunta = Pregunta(
                datos[0],
                datos[1],
                datos[2],
                datos[3],
                datos[4],
                datos[5]
            )
            listaDePreguntas.add(pregunta)
        }
    }
    return listaDePreguntas.shuffled()
}

fun leerArchivoCompetitivo(context: Context): List<Pregunta> {
    var listaPreguntasCompetitivo = leerArchivo(context)
    return listaPreguntasCompetitivo.take(10)
}

// Este método lo tengo como auxiliar para eliminar preguntas. No lo he hecho yo y si
// me preguntas por como funciona alguna cosa concreta probablemente no sepa responder
/*fun removeLine(file: File, lineaNoDeseada: String) {
    val fileTemporal = File.createTempFile("buffer", null)

    val reader = BufferedReader(FileReader(file))
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
}*/

fun leerArchivoAssetsYCrearEnMemoria(context: Context) {
    val file = File(context.filesDir, "preguntas.txt")

    if (!file.exists()) {
        val assetManager = context.assets
        val inputStream = assetManager.open("preguntas.txt")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))

        bufferedReader.forEachLine { linea ->
            val datos = linea.split(",")
            if (datos.size == 6) {
                val contenido =
                    "${datos[0]},${datos[1]},${datos[2]},${datos[3]},${datos[4]},${datos[5]}"
                val fileOutputStream: FileOutputStream =
                    context.openFileOutput("preguntas.txt", Context.MODE_APPEND)
                fileOutputStream.write("$contenido\n".toByteArray())
                fileOutputStream.close()
            }
        }
    }
}


@Composable
fun almacenaImagenes(): List<Painter> {
    val listaImagenes = mutableListOf<Painter>()
    listaImagenes.add(painterResource(id = R.drawable.sobera1))
    listaImagenes.add(painterResource(id = R.drawable.sobera2))
    listaImagenes.add(painterResource(id = R.drawable.sobera3))
    listaImagenes.add(painterResource(id = R.drawable.sobera4))
    listaImagenes.add(painterResource(id = R.drawable.sobera5))
    listaImagenes.add(painterResource(id = R.drawable.sobera6))
    listaImagenes.add(painterResource(id = R.drawable.sobera7))
    listaImagenes.add(painterResource(id = R.drawable.sobera8))
    listaImagenes.add(painterResource(id = R.drawable.sobera9))
    listaImagenes.add(painterResource(id = R.drawable.sobera10))
    return listaImagenes
}

