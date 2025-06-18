package com.example.apurvroy_worldt2

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.apurvroy_worldt2.models.Outcomes
import com.example.apurvroy_worldt2.utils.AppConstants
import com.example.apurvroy_worldt2.utils.MatchHelper
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

class MatchActivity : AppCompatActivity() {

    private lateinit var teamOneLabel: TextView
    private lateinit var teamOneScore: TextView
    private lateinit var teamOneOvers: TextView
    private lateinit var teamTwoLabel: TextView
    private lateinit var teamTwoScore: TextView
    private lateinit var teamTwoOvers: TextView
    private lateinit var ballByBallUpdates: TextView
    private lateinit var nextBallButton: MaterialButton
    private lateinit var toolbar: MaterialToolbar
    private lateinit var teamOneCard: MaterialCardView
    private lateinit var teamTwoCard: MaterialCardView
    private lateinit var matchUpdatesCard: MaterialCardView

    private var firstInningsCompleted = false
    private var battingRuns = 0
    private var battingWickets = 0
    private var battingBalls = 0

    private var targetScore = 0
    private var chasingRuns = 0
    private var chasingWickets = 0
    private var chasingBalls = 0
    private var isFreeHit = false

    private lateinit var teamOneName: String
    private lateinit var teamTwoName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        teamOneName = intent.getStringExtra(AppConstants.TEAM_1) ?: "Team A"
        teamTwoName = intent.getStringExtra(AppConstants.TEAM_2) ?: "Team B"

        // Initialize UI
        initializeViews()
        setupToolbar()
        
        // Set initial team roles
        teamOneLabel.text = "$teamOneName (Batting)"
        teamTwoLabel.text = "$teamTwoName (Bowling)"

        // Apply initial animations
        applyEntryAnimations()

        nextBallButton.setOnClickListener {
            playNextBall()
        }
    }
    
    private fun initializeViews() {
        teamOneLabel = findViewById(R.id.teamOneLabel)
        teamOneScore = findViewById(R.id.teamOneScore)
        teamOneOvers = findViewById(R.id.teamOneOvers)
        teamTwoLabel = findViewById(R.id.teamTwoLabel)
        teamTwoScore = findViewById(R.id.teamTwoScore)
        teamTwoOvers = findViewById(R.id.teamTwoOvers)
        ballByBallUpdates = findViewById(R.id.ballByBallUpdates)
        nextBallButton = findViewById(R.id.playNextBallButton)
        toolbar = findViewById(R.id.toolbar)
        teamOneCard = findViewById(R.id.teamOneCard)
        teamTwoCard = findViewById(R.id.teamTwoCard)
        matchUpdatesCard = findViewById(R.id.matchUpdatesCard)
    }
    
    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "$teamOneName vs $teamTwoName"
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
    
    private fun applyEntryAnimations() {
        val slideInTop = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        val slideInBottom = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        slideInTop.duration = 500
        slideInBottom.duration = 500
        slideInBottom.startOffset = 300
        
        teamOneCard.startAnimation(slideInTop)
        teamTwoCard.startAnimation(slideInBottom)
        
        // Fade in animation for the updates card
        matchUpdatesCard.alpha = 0f
        matchUpdatesCard.animate().alpha(1f).setDuration(800).setStartDelay(600).start()
        
        // Button animation
        nextBallButton.alpha = 0f
        nextBallButton.animate().alpha(1f).setDuration(500).setStartDelay(800).start()
    }

    private fun playNextBall() {
        val probOutcome = MatchHelper.getRandomizeOutCome()
        
        // Animate the button click
        nextBallButton.isEnabled = false
        nextBallButton.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                nextBallButton.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .withEndAction {
                        nextBallButton.isEnabled = true
                    }
                    .start()
            }
            .start()

        if (!firstInningsCompleted) {
            // First innings (Team One bats)
            handleBallOutcome(probOutcome, true)
            
            battingBalls++
            val battingOversText = "${battingBalls / 6}.${battingBalls % 6}"

            teamOneScore.text = "Score: $battingRuns/$battingWickets"
            teamOneOvers.text = "Overs: $battingOversText"

            // Animate the score update
            animateTextChange(teamOneScore)
            animateTextChange(teamOneOvers)

            // Check if first innings is over
            if (MatchHelper.isInningOver(battingBalls, battingWickets)) {
                firstInningsCompleted = true
                targetScore = battingRuns + 1
                
                // Show a snackbar with innings over message
                Snackbar.make(
                    nextBallButton,
                    "Innings Over! $teamTwoName needs $targetScore to win",
                    Snackbar.LENGTH_LONG
                ).show()
                
                ballByBallUpdates.text = "Target: $targetScore runs"
                
                switchInnings()
            }

        } else {
            // Second innings (Team Two bats)
            handleBallOutcome(probOutcome, false)
            
            chasingBalls++
            val chasingOversText = "${chasingBalls / 6}.${chasingBalls % 6}"

            // Update UI
            teamTwoScore.text = "Score: $chasingRuns/$chasingWickets"
            teamTwoOvers.text = "Overs: $chasingOversText"
            
            // Animate the score update
            animateTextChange(teamTwoScore)
            animateTextChange(teamTwoOvers)

            // Check if match is over
            checkMatchStatus()
        }
    }
    
    private fun handleBallOutcome(outcome: Outcomes, isFirstInnings: Boolean) {
        // Apply a short animation to the updates card
        matchUpdatesCard.animate()
            .scaleX(1.05f)
            .scaleY(1.05f)
            .setDuration(150)
            .withEndAction {
                matchUpdatesCard.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(150)
                    .start()
            }
            .start()
            
        when (outcome) {
            Outcomes.DOT_BALL -> {
                ballByBallUpdates.text = "Dot Ball"
                setUpdateCardColor(R.color.md_theme_tertiaryContainer)
                isFreeHit = false
            }

            Outcomes.SINGLE -> {
                if (isFirstInnings) battingRuns += 1 else chasingRuns += 1
                ballByBallUpdates.text = "1 run"
                setUpdateCardColor(R.color.md_theme_tertiaryContainer)
                isFreeHit = false
            }

            Outcomes.Double -> {
                if (isFirstInnings) battingRuns += 2 else chasingRuns += 2
                ballByBallUpdates.text = "2 runs"
                setUpdateCardColor(R.color.md_theme_tertiaryContainer)
                isFreeHit = false
            }

            Outcomes.Triple -> {
                if (isFirstInnings) battingRuns += 3 else chasingRuns += 3
                ballByBallUpdates.text = "3 runs"
                setUpdateCardColor(R.color.md_theme_tertiaryContainer)
                isFreeHit = false
            }

            Outcomes.Four -> {
                if (isFirstInnings) battingRuns += 4 else chasingRuns += 4
                ballByBallUpdates.text = "FOUR!"
                setUpdateCardColor(R.color.md_theme_primaryContainer)
                isFreeHit = false
            }

            Outcomes.Six -> {
                if (isFirstInnings) battingRuns += 6 else chasingRuns += 6
                ballByBallUpdates.text = "SIX!"
                setUpdateCardColor(R.color.md_theme_primary)
                isFreeHit = false
            }

            Outcomes.Wide -> {
                if (isFirstInnings) {
                    battingRuns += 1
                    battingBalls--
                } else {
                    chasingRuns += 1
                    chasingBalls--
                }
                ballByBallUpdates.text = "Wide Ball"
                setUpdateCardColor(R.color.md_theme_secondaryContainer)
                isFreeHit = false
            }

            Outcomes.NoBall -> {
                if (isFirstInnings) {
                    battingRuns += 1
                    battingBalls--
                } else {
                    chasingRuns += 1
                    chasingBalls--
                }
                ballByBallUpdates.text = "No Ball!"
                setUpdateCardColor(R.color.md_theme_secondaryContainer)
                isFreeHit = true
            }

            Outcomes.Wicket -> {
                if (isFreeHit) {
                    ballByBallUpdates.text = "Free Hit - No Wicket!"
                    setUpdateCardColor(R.color.md_theme_secondaryContainer)
                } else {
                    if (isFirstInnings) battingWickets++ else chasingWickets++
                    ballByBallUpdates.text = "WICKET!"
                    setUpdateCardColor(R.color.md_theme_error)
                }
                isFreeHit = false
            }
        }
    }
    
    private fun setUpdateCardColor(colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        matchUpdatesCard.setCardBackgroundColor(color)
        
        // Set the text color based on the background (for contrast)
        if (colorRes == R.color.md_theme_primary) {
            ballByBallUpdates.setTextColor(ContextCompat.getColor(this, R.color.md_theme_onPrimary))
        } else if (colorRes == R.color.md_theme_error) {
            ballByBallUpdates.setTextColor(ContextCompat.getColor(this, R.color.md_theme_onError))
        } else {
            ballByBallUpdates.setTextColor(ContextCompat.getColor(this, R.color.md_theme_onTertiaryContainer))
        }
    }
    
    private fun animateTextChange(textView: TextView) {
        textView.animate()
            .alpha(0.5f)
            .setDuration(100)
            .withEndAction {
                textView.animate()
                    .alpha(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }
    
    private fun checkMatchStatus() {
        if (chasingRuns >= targetScore) {
            // Team Two Wins
            ballByBallUpdates.text = "$teamTwoName Wins!"
            setUpdateCardColor(R.color.md_theme_primary)
            endMatch("$teamTwoName wins by ${10 - chasingWickets} wickets!")
        } else if (MatchHelper.isInningOver(chasingBalls, chasingWickets)) {
            // Team One Wins
            ballByBallUpdates.text = "$teamOneName Wins!"
            setUpdateCardColor(R.color.md_theme_primary)
            endMatch("$teamOneName wins by ${targetScore - chasingRuns - 1} runs!")
        }
    }
    
    private fun endMatch(resultMessage: String) {
        nextBallButton.isEnabled = false
        nextBallButton.text = "Match Over"
        
        // Show match result in a Snackbar
        Snackbar.make(nextBallButton, resultMessage, Snackbar.LENGTH_INDEFINITE)
            .setAction("Back") { onBackPressed() }
            .show()
    }

    private fun switchInnings() {
        firstInningsCompleted = true

        // Apply animation to the team cards during switch
        teamOneCard.animate()
            .translationX(-50f)
            .alpha(0.7f)
            .setDuration(300)
            .withEndAction {
                // Update labels
                teamOneLabel.text = "$teamOneName (Bowling)"
                teamOneCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.md_theme_secondaryContainer))
                
                // Animate back
                teamOneCard.animate()
                    .translationX(0f)
                    .alpha(1f)
                    .setDuration(300)
                    .start()
            }
            .start()
            
        teamTwoCard.animate()
            .translationX(50f)
            .alpha(0.7f)
            .setDuration(300)
            .withEndAction {
                // Update labels
                teamTwoLabel.text = "$teamTwoName (Batting)"
                teamTwoCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.md_theme_primaryContainer))
                
                // Reset second innings stats
                teamTwoScore.text = "Score: 0/0"
                teamTwoOvers.text = "Overs: 0.0"
                
                // Animate back
                teamTwoCard.animate()
                    .translationX(0f)
                    .alpha(1f)
                    .setDuration(300)
                    .start()
            }
            .start()
    }
}
