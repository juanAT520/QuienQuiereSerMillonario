package com.juan.quienquieresermillonario.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.juan.quienquieresermillonario.R
import com.juan.quienquieresermillonario.TarjetaMenu
import com.juan.quienquieresermillonario.nav.Rutas
import com.juan.quienquieresermillonario.ui.theme.orange
import com.juan.quienquieresermillonario.ui.theme.white

class ConfigPreguntas : ViewModel() {
    @Composable
    fun Inicio(navController: NavController) {
        PantallaMuestraPregunta(navController)
    }
}

@Composable
private fun PantallaMuestraPregunta(navController: NavController) {
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
        TarjetaMenu("Añadir pregunta") { navController.navigate(Rutas.AddPregunta.ruta) }
        TarjetaMenu("Modificar pregunta") { navController.navigate(Rutas.ModPregunta.ruta) }
        TarjetaMenu("Eliminar pregunta") { navController.navigate(Rutas.DelPregunta.ruta) }
        Spacer(modifier = Modifier.size(50.dp))
        BotonVolver() { navController.popBackStack() }
        Spacer(modifier = Modifier.size(15.dp))
    }
}

@Composable
private fun BotonVolver(funcion: () -> Unit) {
    Card(
        border = BorderStroke(3.dp, white),
        modifier = Modifier
            .padding(45.dp, 25.dp)
            .height(50.dp)
            .width(150.dp)
            .fillMaxWidth()
            .clickable { funcion() }
    ) {
        Surface(color = orange) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Volver al menú",
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