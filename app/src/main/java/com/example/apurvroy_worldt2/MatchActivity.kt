package com.example.apurvroy_worldt2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.apurvroy_worldt2.models.Outcomes
import com.example.apurvroy_worldt2.utils.AppConstants
import com.example.apurvroy_worldt2.utils.MatchHelper

class MatchActivity : AppCompatActivity() {

    private lateinit var teamOneLabel: TextView
    private lateinit var teamOneScore: TextView
    private lateinit var teamOneOvers: TextView
    private lateinit var teamTwoLabel: TextView
    private lateinit var teamTwoScore: TextView
    private lateinit var teamTwoOvers: TextView
    private lateinit var ballByBallUpdates: TextView
    private lateinit var nextBallButton: Button

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
        teamOneLabel = findViewById(R.id.teamOneLabel)
        teamOneScore = findViewById(R.id.teamOneScore)
        teamOneOvers = findViewById(R.id.teamOneOvers)
        teamTwoLabel = findViewById(R.id.teamTwoLabel)
        teamTwoScore = findViewById(R.id.teamTwoScore)
        teamTwoOvers = findViewById(R.id.teamTwoOvers)
        ballByBallUpdates = findViewById(R.id.ballByBallUpdates)
        nextBallButton = findViewById(R.id.playNextBallButton)

        // Set initial team roles
        teamOneLabel.text = "$teamOneName (Batting)"
        teamTwoLabel.text = "$teamTwoName (Bowling)"

        nextBallButton.setOnClickListener {
            playNextBall()
        }
    }

    private fun playNextBall() {
//        val outcomes = listOf(0, 1, 2, 3, 4, 6, "W", "Wide", "NoBall")
//        val result = outcomes[Random.nextInt(outcomes.size)]

        val probOutcome = MatchHelper.getRandomizeOutCome()


        if (!firstInningsCompleted) {
            // First innings (Team One bats)
//            if (result == "W") {
//                if (isFreeHit) {
//                    ballByBallUpdates.text = "It's a free Hit, No Wicket!"
//                } else {
//                    battingWickets++
//                    ballByBallUpdates.text = "Wicket!"
//                }
//                isFreeHit = false
//            } else if (result == "Wide") {
//                battingRuns++
//                ballByBallUpdates.text = "Wide!"
//                battingBalls--
//            } else if (result == "NoBall") {
//                battingRuns++
//                battingBalls--
//                ballByBallUpdates.text = "No Ball!"
//                isFreeHit = true
//            } else {
//                battingRuns += result as Int
//                ballByBallUpdates.text = "$result runs"
//                isFreeHit = false
//            }
            when (probOutcome) {
                Outcomes.DOT_BALL -> {
                    ballByBallUpdates.text = "Dot Ball"
                    isFreeHit = false
                }

                Outcomes.SINGLE -> {
                    battingRuns += 1
                    ballByBallUpdates.text = "1 runs"
                    isFreeHit = false
                }

                Outcomes.Double -> {
                    battingRuns += 2
                    ballByBallUpdates.text = "2 runs"
                    isFreeHit = false
                }

                Outcomes.Triple -> {
                    battingRuns += 3
                    ballByBallUpdates.text = "3 runs"
                    isFreeHit = false
                }

                Outcomes.Four -> {
                    battingRuns += 4
                    ballByBallUpdates.text = "4 runs"
                    isFreeHit = false
                }

                Outcomes.Six -> {
                    battingRuns += 6
                    ballByBallUpdates.text = "6 runs"
                    isFreeHit = false
                }

                Outcomes.Wide -> {
                    battingRuns += 1
                    ballByBallUpdates.text = "Wide"
                    isFreeHit = false
                    battingBalls--
                }

                Outcomes.NoBall -> {
                    battingRuns += 1
                    ballByBallUpdates.text = "No Ball!"
                    isFreeHit = true
                    battingBalls--
                }

                Outcomes.Wicket -> {
                    if (isFreeHit) {
                        ballByBallUpdates.text = "It's a free Hit, No Wicket!"
                    } else {
                        battingWickets++
                        ballByBallUpdates.text = "Wicket!"
                    }
                    isFreeHit = false
                }
            }

            battingBalls++
            val battingOversText = "${battingBalls / 6}.${battingBalls % 6}"

            teamOneScore.text = "Score: $battingRuns/$battingWickets"
            teamOneOvers.text = "Overs: $battingOversText"

            // Check if first innings is over
            if (MatchHelper.isInningOver(battingBalls, battingWickets)) {
                firstInningsCompleted = true
                targetScore = battingRuns + 1
                ballByBallUpdates.text = "Innings Over! Target: $targetScore"

                switchInnings()
            }

        } else {
            // Second innings (Team Two bats)
//            if (result == "W") {
//                if (isFreeHit) {
//                    ballByBallUpdates.text = "It's a free Hit, No Wicket!"
//                } else {
//                    chasingWickets++
//                    ballByBallUpdates.text = "Wicket!"
//                }
//                isFreeHit = false
//            } else if (result == "Wide") {
//                chasingRuns++
//                ballByBallUpdates.text = "Wide!"
//                chasingBalls--
//            } else if (result == "NoBall") {
//                battingRuns++
//                battingBalls--
//                ballByBallUpdates.text = "No Ball!"
//                isFreeHit = true
//            } else {
//                chasingRuns += result as Int
//                ballByBallUpdates.text = "$result runs"
//                isFreeHit = false
//            }
            when (probOutcome) {
                Outcomes.DOT_BALL -> {
                    ballByBallUpdates.text = "Dot Ball"
                    isFreeHit = false
                }

                Outcomes.SINGLE -> {
                    chasingRuns += 1
                    ballByBallUpdates.text = "1 runs"
                    isFreeHit = false
                }

                Outcomes.Double -> {
                    chasingRuns += 2
                    ballByBallUpdates.text = "2 runs"
                    isFreeHit = false
                }

                Outcomes.Triple -> {
                    chasingRuns += 3
                    ballByBallUpdates.text = "3 runs"
                    isFreeHit = false
                }

                Outcomes.Four -> {
                    chasingRuns += 4
                    ballByBallUpdates.text = "4 runs"
                    isFreeHit = false
                }

                Outcomes.Six -> {
                    chasingRuns += 6
                    ballByBallUpdates.text = "6 runs"
                    isFreeHit = false
                }

                Outcomes.Wide -> {
                    chasingRuns += 1
                    ballByBallUpdates.text = "Wide"
                    isFreeHit = false
                    chasingBalls--
                }

                Outcomes.NoBall -> {
                    chasingRuns += 1
                    ballByBallUpdates.text = "No Ball!"
                    isFreeHit = true
                    chasingBalls--
                }

                Outcomes.Wicket -> {
                    if (isFreeHit) {
                        ballByBallUpdates.text = "It's a free Hit, No Wicket!"
                    } else {
                        chasingWickets++
                        ballByBallUpdates.text = "Wicket!"
                    }
                    isFreeHit = false
                }
            }

            chasingBalls++
            val chasingOversText = "${chasingBalls / 6}.${chasingBalls % 6}"

            // Update UI
            teamTwoScore.text = "Score: $chasingRuns/$chasingWickets"
            teamTwoOvers.text = "Overs: $chasingOversText"

            // Check if match is over
            if (chasingRuns >= targetScore) {
                ballByBallUpdates.text = "$teamTwoName Wins!"
                nextBallButton.isEnabled = false
                nextBallButton.text = "Match Over"
            } else if (MatchHelper.isInningOver(chasingBalls, chasingWickets)) {
                ballByBallUpdates.text = "$teamOneName Wins!"
                nextBallButton.isEnabled = false
                nextBallButton.text = "Match Over"
            }
        }
    }

    private fun switchInnings() {
        firstInningsCompleted = true

        //Update Labels Batting ↔ Bowling
        teamOneLabel.text = "$teamOneName (Bowling)"
        teamTwoLabel.text = "$teamTwoName (Batting)"

        // Reset second innings stats
        teamTwoScore.text = "Score: 0/0"
        teamTwoOvers.text = "Overs: 0.0"

        ballByBallUpdates.text = "$teamTwoName needs $targetScore to win!"
    }
}
