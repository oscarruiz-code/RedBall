package com.example.spartacusgame.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.spartacusgame.utils.Constants

class GameViewModel : ViewModel() {

    // Estado del juego
    var ballPosition by mutableStateOf(Offset(200f, 350f))
    var isFalling by mutableStateOf(true)
    var isJumping by mutableStateOf(false)
    var ballBounds by mutableStateOf(Rect.Zero)
    var floorBounds by mutableStateOf(Rect.Zero)
    var floorSecondBounds by mutableStateOf(Rect.Zero)
    var floorSecondPosition by mutableStateOf(Offset(0f, 400f)) // Inicia en el límite derecho
    var ballVelocity by mutableStateOf(0f)
    var jumpVelocity by mutableStateOf(0f)
    val gravity = Constants.GRAVITY
    var shots: MutableList<Rect> = mutableListOf()
    var fallingSquares: MutableList<Rect> = mutableListOf()
    var score by mutableStateOf(0)
    var isGameOver by mutableStateOf(false)
    var gameOverMessageVisible by mutableStateOf(false)

    // Dimensiones de la pantalla en píxeles
    var screenWidth by mutableStateOf(0f)
    var screenHeight by mutableStateOf(0f)

    // Configuración del juego
    private var squareGenerationInterval = 3000L
    private var lastShotTime = 0L
    private val shotCooldown = 600L
    private var floorSecondVelocity = -2f // Velocidad inicial hacia la izquierda
    private val floorSecondWidth = 200f // Ancho de la barra roja

    // Método para establecer las dimensiones de la pantalla
    fun setScreenDimensions(width: Float, height: Float) {
        screenWidth = width
        screenHeight = height
        floorSecondPosition = Offset(screenWidth - floorSecondWidth, 400f) // Inicia en el límite derecho
    }

    // Inicia el bucle principal del juego
    fun startGameLoop() {
        viewModelScope.launch {
            while (!isGameOver) {
                delay(10)
                updateGameState()
            }
        }
    }

    // Actualiza el estado del juego
    private fun updateGameState() {
        if (isGameOver) return

        applyHorizontalMovement()
        applyGravity()
        moveFloorSecond()
        handleCollisions()
        updateBallBounds()

        // Mueve las balas hacia arriba
        shots = shots.map { shot ->
            Rect(shot.left, shot.top - 5f, shot.right, shot.bottom - 5f)
        }.filter { it.bottom > 0 }.toMutableList()

        // Mueve los cuadrados hacia abajo
        fallingSquares = fallingSquares.map { square ->
            Rect(square.left, square.top + 3f, square.right, square.bottom + 3f)
        }.filter { it.top < screenHeight }.toMutableList()

        // Elimina cuadrados que toquen el suelo verde o el suelo rojo
        fallingSquares.removeAll { square ->
            square.overlaps(floorBounds) || square.overlaps(floorSecondBounds)
        }

        // Verifica si la bola colisiona con algún cuadrado
        if (fallingSquares.any { it.overlaps(ballBounds) }) {
            gameOver()
        }
    }

    // Aplica el movimiento horizontal de la bola
    private fun applyHorizontalMovement() {
        ballPosition = ballPosition.copy(x = ballPosition.x + ballVelocity)
        restrictBallPosition()
    }

    // Aplica la gravedad a la bola
    private fun applyGravity() {
        if (isFalling) {
            ballPosition = ballPosition.copy(y = ballPosition.y + jumpVelocity)
            jumpVelocity += gravity
        }
        restrictBallPosition()
    }

    // Maneja las colisiones de la bola con los suelos
    private fun handleCollisions() {
        // Verifica colisión de la bola con el suelo verde
        if (ballBounds.overlaps(floorBounds)) {
            handleFloorCollision(floorBounds)
        } else {
            isFalling = true
        }
    }

    // Maneja la colisión con el suelo verde
    private fun handleFloorCollision(floorBounds: Rect) {
        isFalling = false
        isJumping = false
        jumpVelocity = 0f
        ballPosition = ballPosition.copy(y = floorBounds.top - 25f)
        restrictBallPosition()
    }

    // Actualiza los límites de la bola
    private fun updateBallBounds() {
        ballBounds = Rect(
            left = ballPosition.x - 25f,
            top = ballPosition.y - 25f,
            right = ballPosition.x + 25f,
            bottom = ballPosition.y + 25f
        )
    }

    // Restringe la posición de la bola dentro de la pantalla
    private fun restrictBallPosition() {
        ballPosition = Offset(
            x = ballPosition.x.coerceIn(25f, screenWidth - 25f),
            y = ballPosition.y.coerceIn(25f, screenHeight - 25f)
        )
        updateBallBounds()
    }

    // Mueve el suelo rojo
    private fun moveFloorSecond() {
        floorSecondPosition = floorSecondPosition.copy(x = floorSecondPosition.x + floorSecondVelocity)

        // Restringe el movimiento de la barra roja dentro de la pantalla
        if (floorSecondPosition.x <= 0) {
            floorSecondPosition = floorSecondPosition.copy(x = 0f) // Límite izquierdo
            floorSecondVelocity = -floorSecondVelocity // Invierte la dirección (va hacia la derecha)
        } else if (floorSecondPosition.x >= screenWidth - floorSecondWidth) { // Límite derecho
            floorSecondPosition = floorSecondPosition.copy(x = screenWidth - floorSecondWidth) // Límite derecho
            floorSecondVelocity = -floorSecondVelocity // Invierte la dirección (va hacia la izquierda)
        }

        // Actualiza los límites de la barra roja
        floorSecondBounds = Rect(
            left = floorSecondPosition.x,
            top = floorSecondPosition.y,
            right = floorSecondPosition.x + floorSecondWidth, // Ancho de la barra roja
            bottom = floorSecondPosition.y + 30f // Altura de la barra roja
        )
    }

    // Dispara una bala
    fun onShoot() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastShotTime >= shotCooldown) {
            val shot = Rect(ballPosition.x - 10f, ballPosition.y - 25f, ballPosition.x + 10f, ballPosition.y - 15f)
            shots.add(shot)
            lastShotTime = currentTime
        }
    }

    // Finaliza el juego
    private fun gameOver() {
        isGameOver = true
        gameOverMessageVisible = true
    }

    // Reinicia el juego
    fun resetGame() {
        ballPosition = Offset(200f, 350f)
        isFalling = true
        isJumping = false
        ballVelocity = 0f
        jumpVelocity = 0f
        shots.clear()
        fallingSquares.clear()
        score = 0
        isGameOver = false
        gameOverMessageVisible = false
        squareGenerationInterval = 3000L
        floorSecondVelocity = -2f // Velocidad inicial hacia la izquierda
        floorSecondPosition = Offset(screenWidth - floorSecondWidth, 400f) // Inicia en el límite derecho
    }

    // Maneja el movimiento del joystick
    fun onJoystickMove(direction: Offset) {
        ballVelocity = direction.x * 5f
    }

    // Inicia la generación de cuadrados
    fun startSquareGeneration() {
        viewModelScope.launch {
            while (!isGameOver) {
                delay(squareGenerationInterval)
                generateSquare()
                increaseDifficulty()
            }
        }
    }

    // Genera un nuevo cuadrado en una posición aleatoria
    private fun generateSquare() {
        val x = (0..screenWidth.toInt()).random().toFloat() // Posición aleatoria en el eje X
        fallingSquares.add(Rect(x, 0f, x + 50f, 50f)) // Crea un cuadrado de 50x50 píxeles
    }

    // Aumenta la dificultad cada 100 puntos
    private fun increaseDifficulty() {
        if (score >= 100 && score % 100 == 0) {
            squareGenerationInterval = (squareGenerationInterval * 0.9).toLong() // Reduce el intervalo
            floorSecondVelocity *= 1.1f // Aumenta la velocidad del suelo
        }
    }

    // Inicia la detección de colisiones
    fun startCollisionDetection() {
        viewModelScope.launch {
            while (!isGameOver) {
                delay(50)
                checkShotCollisions()
                checkBallCollisions()
            }
        }
    }

    // Verifica colisiones entre balas y cuadrados
    private fun checkShotCollisions() {
        val newShots = mutableListOf<Rect>()
        val newFallingSquares = mutableListOf<Rect>()

        for (square in fallingSquares) {
            val hitShot = shots.find { it.overlaps(square) }
            if (hitShot != null) {
                score += 20 // Aumenta el score si hay colisión
            } else {
                newFallingSquares.add(square) // Mantén el cuadrado si no colisiona
            }
        }

        // Filtra las balas que no han colisionado
        for (shot in shots) {
            if (!fallingSquares.any { it.overlaps(shot) }) {
                newShots.add(shot) // Mantén la bala si no colisiona
            }
        }

        // Actualiza las listas de balas y cuadrados
        shots = newShots
        fallingSquares = newFallingSquares
    }

    // Verifica colisiones entre la bola y los cuadrados o suelos
    private fun checkBallCollisions() {
        // Verifica colisión de la bola con los cuadrados
        if (fallingSquares.any { it.overlaps(ballBounds) }) {
            gameOver() // Si la bola toca un cuadrado, termina el juego
        }

        // Verifica colisión de la bola con el suelo verde
        if (ballBounds.overlaps(floorBounds)) {
            handleFloorCollision(floorBounds)
        } else {
            isFalling = true
        }
    }
}