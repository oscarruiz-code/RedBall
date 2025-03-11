package com.example.spartacusgame.screens

import android.app.Activity
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spartacusgame.R
import com.example.spartacusgame.utils.AudioManager
import com.example.spartacusgame.viewmodels.SharedViewModel
import kotlinx.coroutines.delay

/**
 * Clase que nos maneja el estilo visual de nuestra pantalla principal y se encarga de hacer visible nuestros componentes
 *
 * @author oscarruiz-code
 *
 */

@Composable
fun MainScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val audioManager = remember { AudioManager(context) }

    //Inicio el Audio apartir de mis utilidades
    LaunchedEffect(Unit) {
        audioManager.playLoopingAudio(R.raw.intro)
    }

    DisposableEffect(Unit) {
        onDispose {
            audioManager.stopAudio()
        }
    }


    var startAnimation by remember { mutableStateOf(false) }
    var playerName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    val transition = updateTransition(targetState = startAnimation, label = "Scale Animation")

    val scale by transition.animateFloat(
        transitionSpec = {
            keyframes {
                durationMillis = 5000
                1.5f at 0 with LinearOutSlowInEasing
                1f at 1000 with LinearOutSlowInEasing
            }
        },
        label = "Scale"
    ) { state ->
        if (state) 1f else 1.5f
    }

    val activity = context as? Activity

    //Representacion de nuestra imagen de fondo de la mainscreen
    Box(modifier = Modifier.fillMaxSize()) {

        val backgroundImage = R.drawable.fondopantalla

        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = "Fondo",
            modifier = Modifier
                .fillMaxSize()
                .scale(scale),
            contentScale = ContentScale.FillBounds
        )


        //Representacion del recuadro de introducir nombre
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 110.dp)
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            PulsatingInput(
                value = playerName,
                onValueChange = { playerName = it },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 16.dp)
            )
        }

        //Representacion de botones correctamente
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 150.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Button(
                onClick = {
                    if (playerName.isNotBlank()) {
                        sharedViewModel.playerName = playerName
                        navController.navigate("game_screen")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .width(120.dp)
                    .height(40.dp)
            ) {
                Text("Start", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("score_screen") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .width(120.dp)
                    .height(40.dp)
            ) {
                Text("Historial", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { activity?.finish() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .width(120.dp)
                    .height(40.dp)
            ) {
                Text("Exit", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }

}

/**
 * Funcion que controla la animacion para hacer que el recuadro de introducir nombre parezca que esta
 * palpitando
 */
@Composable
fun PulsatingInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1f) }

    LaunchedEffect(Unit) {
        while (true) {
            scale = if (scale == 1f) 1.1f else 1f
            delay(500)
        }
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .scale(scale)
            .background(Color.Transparent)
            .padding(8.dp),
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = "Ingresa tu nombre",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                innerTextField()
            }
        }
    )
}