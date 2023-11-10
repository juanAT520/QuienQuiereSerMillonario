package com.juan.quienquieresermillonario.nav

sealed class Rutas(val ruta: String) {
    object Principal : Rutas("principal")
    object Amistoso : Rutas("amistoso")
    object Competitivo : Rutas("competitivo")
    object Estadisticas : Rutas("estadisticas")
    object Configuracion : Rutas("configuracion")
    object AddPregunta : Rutas("addPregunta")
    object ModPregunta : Rutas("modPregunta")
    object DelPregunta : Rutas("delPregunta")
}
