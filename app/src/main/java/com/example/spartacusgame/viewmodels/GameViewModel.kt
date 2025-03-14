package com.example.spartacusgame.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spartacusgame.screens.Score
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.spartacusgame.utils.Constants

/**
 * Clase que nos maneja la logica de nuestra ventana juego
 *
 * @author oscarruiz-code
 *
 */
class GameViewModel : ViewModel() {

    var ballPosition by mutableStateOf(Offset(200f, 350f))
    var isFalling by mutableStateOf(true)
    var isJumping by mutableStateOf(false)
    var ballBounds by mutableStateOf(Rect.Zero)
    var floorBounds by mutableStateOf(Rect.Zero)
    var floorSecondBounds by mutableStateOf(Rect.Zero)
    var floorSecondPosition by mutableStateOf(Offset(25f, 400f))
    var ballVelocity by mutableStateOf(0f)
    var jumpVelocity by mutableStateOf(0f)
    val gravity = Constants.GRAVITY
    var shots: MutableList<Rect> = mutableListOf()
    var fallingSquares: MutableList<Rect> = mutableListOf()
    var score by mutableStateOf(0)
    var isGameOver by mutableStateOf(false)
    var gameOverMessageVisible by mutableStateOf(false)

    var screenWidth by mutableStateOf(0f)
    var screenHeight by mutableStateOf(0f)

    private var squareGenerationInterval = 3000L
    private var lastShotTime = 0L
    private val shotCooldown = 600L
    private var floorSecondVelocity = -2f
    val floorSecondWidth = 130f

    //Empieza el juegi a los 10 mils
    fun startGameLoop() {
        viewModelScope.launch {
            while (!isGameOver) {
                delay(10)
                updateGameState()
            }
        }
    }

    //Actualiza conforme vayan ocurriendo eventos en nuestra aplicacion y lo mantenemos actualizado en cualquier momento
    private fun updateGameState() {
        if (isGameOver) return

        applyHorizontalMovement()
        applyGravity()
        moveFloorSecond()
        handleCollisions()
        updateBallBounds()

        shots = shots.map { shot ->
            Rect(shot.left, shot.top - 5f, shot.right, shot.bottom - 5f)
        }.filter { it.bottom > 0 }.toMutableList()

        fallingSquares = fallingSquares.map { square ->
            Rect(square.left, square.top + 3f, square.right, square.bottom + 3f)
        }.filter { it.top < screenHeight }.toMutableList()

        fallingSquares.removeAll { square ->
            square.overlaps(floorBounds) || square.overlaps(floorSecondBounds)
        }

        if (fallingSquares.any { it.overlaps(ballBounds) }) {
            gameOver()
        }
    }

    //Movimiento horizontal
    private fun applyHorizontalMovement() {
        ballPosition = ballPosition.copy(x = ballPosition.x + ballVelocity)
        restrictBallPosition()
    }

    //Manejo de gravedad
    private fun applyGravity() {
        if (isFalling) {
            ballPosition = ballPosition.copy(y = ballPosition.y + jumpVelocity)
            jumpVelocity += gravity
        }
        restrictBallPosition()
    }

    //Manejo de colision con la bola y el suelo primario
    private fun handleCollisions() {

        if (ballBounds.overlaps(floorBounds)) {
            handleFloorCollision(floorBounds)
        } else {
            isFalling = true
        }
    }

    //Manejo de distancias en la que hace la colision la bola con el suelo primario
    private fun handleFloorCollision(floorBounds: Rect) {
        isFalling = false
        isJumping = false
        jumpVelocity = 0f
        ballPosition = ballPosition.copy(y = floorBounds.top - 25f)
        restrictBallPosition()
    }

    //Actualiza en cualquier momento la posicion de la bola
    private fun updateBallBounds() {
        ballBounds = Rect(
            left = ballPosition.x - 25f,
            top = ballPosition.y - 25f,
            right = ballPosition.x + 25f,
            bottom = ballPosition.y + 25f
        )
    }

    //Restringe el movimiento de la bola en las dimensiones de la pantalla que nosotros le hemos dado
    private fun restrictBallPosition() {
        ballPosition = Offset(
            x = ballPosition.x.coerceIn(25f, screenWidth - 150f),
            y = ballPosition.y.coerceIn(25f, screenHeight - 150f)
        )
        updateBallBounds()
    }

    //Definirle las dimensiones a nuestra pantalla con el suelo secundario
    fun setScreenDimensions(width: Float, height: Float) {
        screenWidth = width
        screenHeight = height

        floorSecondPosition = Offset(200f, 300f)
    }

    //Manejo del movimiento dinamico del suelo secundario
    private fun moveFloorSecond() {
        floorSecondPosition = floorSecondPosition.copy(x = floorSecondPosition.x + floorSecondVelocity)

        if (floorSecondPosition.x <= 25f) {
            floorSecondPosition = floorSecondPosition.copy(x = 25f)
            floorSecondVelocity = -floorSecondVelocity
        } else if (floorSecondPosition.x >= screenWidth - floorSecondWidth - 50f) {
            floorSecondPosition = floorSecondPosition.copy(x = screenWidth - floorSecondWidth - 50f)
            floorSecondVelocity = -floorSecondVelocity
        }

        floorSecondBounds = Rect(
            left = floorSecondPosition.x,
            top = floorSecondPosition.y,
            right = floorSecondPosition.x + floorSecondWidth,
            bottom = floorSecondPosition.y + 30f
        )
    }

    //Manejo del disparo
    fun onShoot() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastShotTime >= shotCooldown) {

            val ballCenterX = ballPosition.x
            val ballCenterY = ballPosition.y - 25f

            val shot = Rect(
                left = ballCenterX - 10f,
                top = ballCenterY - 20f,
                right = ballCenterX + 10f,
                bottom = ballCenterY
            )

            shots.add(shot)
            lastShotTime = currentTime
        }
    }

    //Manejo de perder el juego
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
        floorSecondVelocity = -2f
        floorSecondPosition = Offset(screenWidth - 150f - floorSecondWidth, 400f)
    }

    //Definir movimiento al joystick
    fun onJoystickMove(direction: Offset) {
        ballVelocity = direction.x * 5f
    }

    //Funcion para comenzar a generar enemigos
    fun startSquareGeneration() {
        viewModelScope.launch {
            while (!isGameOver) {
                delay(squareGenerationInterval)
                generateSquare()
                increaseDifficulty()
            }
        }
    }

    //Dimensiones en la que se me generan los enemigos
    private fun generateSquare() {
        val x = (0..screenWidth.toInt()).random().toFloat()
        fallingSquares.add(Rect(x, 0f, x + 50f, 50f))
    }

    //Incrememto de la dificultad segun la puntacion
    private fun increaseDifficulty() {
        if (score >= 100 && score % 100 == 0) {
            squareGenerationInterval = (squareGenerationInterval * 0.9).toLong() // Reduce el intervalo
            floorSecondVelocity *= 1.1f
        }
    }

    //Comienzo de deteccion de colisiones
    fun startCollisionDetection() {
        viewModelScope.launch {
            while (!isGameOver) {
                delay(50)
                checkShotCollisions()
                checkBallCollisions()
            }
        }
    }

    //Comprobar colisiones con el suelo secundario
    private fun checkShotCollisions() {
        val newShots = mutableListOf<Rect>()
        val newFallingSquares = mutableListOf<Rect>()

        for (shot in shots) {
            var shotHit = false

            for (square in fallingSquares) {
                if (shot.overlaps(square)) {
                    score += 20
                    shotHit = true
                    break
                }
            }

            if (shot.overlaps(floorSecondBounds)) {
                shotHit = true
            }

            if (!shotHit) {
                newShots.add(shot)
            }
        }

        for (square in fallingSquares) {
            if (!shots.any { it.overlaps(square) }) {
                newFallingSquares.add(square)
            }
        }

        shots = newShots
        fallingSquares = newFallingSquares
    }


    private fun checkBallCollisions() {

        if (fallingSquares.any { it.overlaps(ballBounds) }) {
            gameOver()
        }

        if (ballBounds.overlaps(floorBounds)) {
            handleFloorCollision(floorBounds)
        } else {
            isFalling = true
        }
    }

    var scores by mutableStateOf<List<Score>>(emptyList())
        private set

    fun addScore(playerName: String, score: Int) {
        scores = scores + Score(playerName, score)
    }

}