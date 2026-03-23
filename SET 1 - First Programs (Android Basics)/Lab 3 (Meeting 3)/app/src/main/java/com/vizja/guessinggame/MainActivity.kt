package com.vizja.guessinggame

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var tvAttempt: TextView
    private lateinit var etGuess: EditText
    private lateinit var btnGuess: Button
    private lateinit var tvMessage: TextView

    private var targetNumber = 0
    private var attemptsCount = 1
    private var gameEnded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvAttempt = findViewById(R.id.tvAttempt)
        etGuess = findViewById(R.id.etGuess)
        btnGuess = findViewById(R.id.btnGuess)
        tvMessage = findViewById(R.id.tvMessage)

        startNewGame()

        btnGuess.setOnClickListener {
            if (gameEnded) {
                startNewGame()
                return@setOnClickListener
            }
            checkGuess()
        }
    }

    private fun startNewGame() {
        targetNumber = Random.nextInt(1, 11) // 1 to 10 inclusive
        attemptsCount = 1
        gameEnded = false
        
        tvAttempt.text = "Attempt: $attemptsCount"
        etGuess.text.clear()
        etGuess.isEnabled = true
        btnGuess.text = "Check Guess"
        tvMessage.text = ""
        tvMessage.setTextColor(Color.parseColor("#333333"))
    }

    private fun checkGuess() {
        val inputStr = etGuess.text.toString().trim()
        
        // Validation: empty or not integer
        val userGuess = inputStr.toIntOrNull()
        if (userGuess == null) {
            showMessage("Please enter an integer number.")
            return
        }

        // Validation: range 1..10
        if (userGuess !in 1..10) {
            showMessage("Number must be in range 1..10.")
            return
        }

        // Valid guess, compare
        if (userGuess < targetNumber) {
            showMessage("Value too small")
            attemptsCount++
            tvAttempt.text = "Attempt: $attemptsCount"
        } else if (userGuess > targetNumber) {
            showMessage("Value too large")
            attemptsCount++
            tvAttempt.text = "Attempt: $attemptsCount"
        } else {
            // Correct guess logic
            if (attemptsCount == 2) {
                showMessage("Correct — achieved on the 2nd attempt")
                gameEnded = true
                etGuess.isEnabled = false
                btnGuess.text = "Play Again"
            } else {
                // Instantly reset but preserve the message
                startNewGame()
                showMessage("Correct, but not on the 2nd attempt. Try again.")
            }
        }
    }

    private fun showMessage(msg: String) {
        tvMessage.text = msg
        // Make the success/warning message stand out
        if (msg.contains("Correct")) {
            tvMessage.setTextColor(Color.parseColor("#008000")) // Green for correct
        } else if (msg.contains("too small") || msg.contains("too large")) {
            tvMessage.setTextColor(Color.parseColor("#EE0000")) // Red for wrong
        } else {
            tvMessage.setTextColor(Color.parseColor("#EE9900")) // Orange for validation warnings
        }
    }
}
