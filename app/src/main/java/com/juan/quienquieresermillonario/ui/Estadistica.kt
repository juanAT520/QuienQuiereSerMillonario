package com.juan.quienquieresermillonario.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.juan.quienquieresermillonario.ui.theme.blue_Grotto
import com.juan.quienquieresermillonario.ui.theme.orange
import com.juan.quienquieresermillonario.ui.theme.shadow
import com.juan.quienquieresermillonario.ui.theme.white

class Estadistica : ViewModel() {
    @Composable
    fun Inicio(
        aciertos: Int,
        fallos: Int,
        totalPreguntas: Int,
        numeroClicks: Int,
        navController: NavController
    ) {
        MuestraEstadisticas(aciertos, fallos, totalPreguntas, numeroClicks, navController)
    }
}

@Composable
private fun MuestraEstadisticas(
    aciertos: Int,
    fallos: Int,
    totalPreguntas: Int,
    numeroClicks: Int,
    navController: NavController
) {
    val orangeTransparente = orange.copy(alpha = 0.85f)
    Image(
        painter = painterResource(id = R.drawable.fondo),
        contentDescription = "Imagen de fondo",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .padding(20.dp)
            .background(color = orangeTransparente, shape = RoundedCornerShape(20.dp))
            .border(width = 3.dp, color = white, shape = RoundedCornerShape(20.dp))
    ) {
        Text(
            text = "Estadísticas:",
            color = shadow,
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        MuestraTexto(texto = "Número de preguntas acertadas: $aciertos")
        MuestraTexto(texto = "Número de preguntas falladas: $fallos")
        MuestraTexto(texto = "Porcentaje de preguntas acertadas: ${((aciertos.toFloat() / totalPreguntas) * 100).toInt()}%")
        MuestraTexto(texto = "Porcentaje de preguntas falladas: ${((fallos.toFloat() / totalPreguntas) * 100).toInt()}%")
        MuestraTexto(texto = "Número de clicks en los botones: $numeroClicks")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
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

@Composable
private fun MuestraTexto(texto: String) {
    Text(
        text = texto,
        color = shadow,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 15.dp)
    )
}