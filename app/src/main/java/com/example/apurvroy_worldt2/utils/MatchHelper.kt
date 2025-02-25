package com.example.apurvroy_worldt2.utils

import com.example.apurvroy_worldt2.models.Outcomes

object MatchHelper {
    fun isInningOver(balls:Int, wickets:Int):Boolean{
        return balls >= 12 || wickets >= 3;
    }
    fun getRandomizeOutCome():Outcomes{
        val randomNum=(1..100).random()
        return when(randomNum){
            in 1..10->Outcomes.DOT_BALL
            in 21..40->Outcomes.SINGLE
            in 41..60->Outcomes.Double
            in 61..70->Outcomes.Triple
            in 71..80->Outcomes.Four
            in 81..87->Outcomes.Six
            in 88..95->Outcomes.Wide
            in 96..98->Outcomes.NoBall
            else -> {Outcomes.Wicket}
        }
    }
}