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

    var ballPosition by mutableStateOf(Offset(200f, 350f))  // Actualiza la posición inicial aquí
    var isFalling by mutableStateOf(true)
    var isJumping by mutableStateOf(false)

    var ballBounds by mutableStateOf(Rect.Zero)
    var floorBounds by mutableStateOf(Rect.Zero)
    var floorSecondBounds by mutableStateOf(Rect.Zero)
    var floorSecondPosition by mutableStateOf(Offset(0f, 400f))

    var ballVelocity by mutableStateOf(0f)
    var jumpVelocity by mutableStateOf(0f)
    val gravity = Constants.GRAVITY
    private var floorSecondVelocity = 2f

    private fun updateBallBounds() {
        ballBounds = Rect(
            left = ballPosition.x - 25f,  // Radio de la bola
            top = ballPosition.y - 25f,   // Radio de la bola
            right = ballPosition.x + 25f, // Radio de la bola
            bottom = ballPosition.y + 25f // Radio de la bola
        )
    }

    fun startGameLoop() {
        viewModelScope.launch {
            while (true) {
                delay(16) // Simula 60 FPS
                updateGameState()
            }
        }
    }

    private fun updateGameState() {
        applyHorizontalMovement()
        applyGravity()
        moveFloorSecond()
        handleCollisions()
        updateBallBounds()
    }

    private fun applyHorizontalMovement() {
        ballPosition = ballPosition.copy(x = ballPosition.x + ballVelocity)
    }

    private fun applyGravity() {
        if (isFalling) {
            ballPosition = ballPosition.copy(y = ballPosition.y + jumpVelocity)
            jumpVelocity += gravity
        }

    }

    private fun moveFloorSecond() {
        floorSecondPosition = floorSecondPosition.copy(x = floorSecondPosition.x + floorSecondVelocity)

        if (floorSecondPosition.x <= -500 || floorSecondPosition.x >= 500) {
            floorSecondVelocity = -floorSecondVelocity
        }
        floorSecondBounds = Rect(
            left = floorSecondPosition.x,
            top = floorSecondPosition.y,
            right = floorSecondPosition.x + 200f,
            bottom = floorSecondPosition.y + 30f
        )
    }

    private fun handleCollisions() {

        if (ballBounds.overlaps(floorBounds)) {
            handleFloorCollision(floorBounds)
        } else if (ballBounds.overlaps(floorSecondBounds)) {
            handleFloorSecondCollision(floorSecondBounds)
        } else {
            isFalling = true
        }
    }

    private fun handleFloorCollision(floorBounds: Rect) {
        isFalling = false
        isJumping = false
        jumpVelocity = 0f
        ballPosition = ballPosition.copy(y = floorBounds.top - 25f)
    }

    private fun handleFloorSecondCollision(floorSecondBounds: Rect) {
        if (ballBounds.bottom <= floorSecondBounds.top) {
            handleTopCollision(floorSecondBounds)
        } else if (ballBounds.top >= floorSecondBounds.bottom) {
            handleBottomCollision(floorSecondBounds)
        }
    }

    private fun handleTopCollision(floorSecondBounds: Rect) {
        isFalling = false
        isJumping = false
        jumpVelocity = 0f
        ballPosition = ballPosition.copy(y = floorSecondBounds.top - 25f)
    }

    private fun handleBottomCollision(floorSecondBounds: Rect) {
        isFalling = true
        isJumping = false
        jumpVelocity = -jumpVelocity * 0.8f
        ballPosition = ballPosition.copy(y = floorSecondBounds.bottom + 25f)
    }

    fun onJoystickMove(direction: Offset) {
        ballVelocity = direction.x * 5f
    }

    fun onJump() {
        if (!isJumping) {
            isJumping = true
            jumpVelocity = -20f
        }
    }
}
