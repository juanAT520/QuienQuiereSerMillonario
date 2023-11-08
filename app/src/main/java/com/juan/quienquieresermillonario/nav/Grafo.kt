package com.juan.quienquieresermillonario.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.juan.quienquieresermillonario.MenuPrincipal
import com.juan.quienquieresermillonario.almacenaImagenes
import com.juan.quienquieresermillonario.leerArchivo
import com.juan.quienquieresermillonario.leerArchivoCompetitivo
import com.juan.quienquieresermillonario.ui.AddPreguntas
import com.juan.quienquieresermillonario.ui.Estadistica
import com.juan.quienquieresermillonario.ui.PantallaCompetitivo
import com.juan.quienquieresermillonario.ui.PantallaPreguntas

@Composable
fun GrafoNavegacion() {
    val navController = rememberNavController()
    val pantallaPreguntas = PantallaPreguntas()
    val pantallaCompetitivo = PantallaCompetitivo()
    val pantallaEstadistica = Estadistica()
    val pantallaAddPregunta = AddPreguntas()
    val listaImagenes = almacenaImagenes()
    var listaPreguntas = leerArchivo(LocalContext.current)
    var listaPreguntasCompetitivo = leerArchivo(LocalContext.current)

    NavHost(navController = navController, startDestination = Rutas.Principal.ruta) {
        composable("principal") {
            listaPreguntas = leerArchivo(LocalContext.current)
            listaPreguntasCompetitivo = leerArchivoCompetitivo(LocalContext.current)
            MenuPrincipal(navController)
        }
        composable("amistoso") {
            pantallaPreguntas.Inicio(listaPreguntas, listaImagenes, navController)
        }
        /*TODO hacer que lea solo 10 lineas aleatorias de preguntas.txt*/
        composable("competitivo") {
            pantallaCompetitivo.Inicio(listaPreguntasCompetitivo, listaImagenes, navController)
        }
        composable("estadisticas") {
            pantallaEstadistica.Inicio(
                pantallaPreguntas.preguntasAcertadas,
                pantallaPreguntas.preguntasFalladas,
                pantallaPreguntas.numeroPreguntas,
                pantallaPreguntas.numeroClicks
            )
        }
        composable("addPregunta") {
            pantallaAddPregunta.Inicio(navController)
        }
    }
}