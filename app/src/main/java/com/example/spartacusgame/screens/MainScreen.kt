package com.example.spartacusgame.screens

import android.app.Activity
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spartacusgame.R
import com.example.spartacusgame.viewmodels.SharedViewModel

@Composable
fun MainScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    var startAnimation by remember { mutableStateOf(false) }

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

    val context = LocalContext.current
    val activity = context as? Activity

    Box(modifier = Modifier.fillMaxSize()) {
        val backgroundImage = if (sharedViewModel.background == "first") {
            R.drawable.fondopantalla
        } else {
            R.drawable.fondopantalla1
        }

        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = "Fondo",
            modifier = Modifier
                .fillMaxSize()
                .scale(scale),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 80.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(150.dp))

            Button(onClick = {
                navController.navigate("game_screen")
            },
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .padding(bottom = 16.dp)
                    .height(60.dp)
            ){
                Text("Start",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                activity?.finish()
            },
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .padding(bottom = 16.dp)
                    .height(60.dp)
            ){
                Text("Exit",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
