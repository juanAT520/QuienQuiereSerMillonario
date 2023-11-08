package com.juan.quienquieresermillonario.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.juan.quienquieresermillonario.MenuPrincipal
import com.juan.quienquieresermillonario.almacenaImagenes
import com.juan.quienquieresermillonario.leerArchivo
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

    NavHost(navController = navController, startDestination = Rutas.Principal.ruta) {
        composable("principal") {
            MenuPrincipal(navController)
        }
        composable("amistoso") {
            val listaPreguntas = leerArchivo(LocalContext.current)
            pantallaPreguntas.Inicio(listaPreguntas, listaImagenes, navController)
        }
        /*TODO hacer que lea solo 10 lineas aleatorias de preguntas.txt*/
        composable("competitivo") {
            val listaPreguntas = leerArchivo(LocalContext.current)
            pantallaCompetitivo.Inicio(listaPreguntas, listaImagenes, navController)
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