package com.example.apurvroy_worldt2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apurvroy_worldt2.models.Team
import com.example.apurvroy_worldt2.utils.AppConstants
import com.example.cricketmatch.TeamsListAdapter
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    private lateinit var teamRecyclerView: RecyclerView
    private lateinit var startButton: Button
    private val selectedTeams = mutableListOf<Team>()
    private lateinit var teamAdapter: TeamsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        teamRecyclerView = findViewById(R.id.teamsListRecyclerView)
        startButton = findViewById(R.id.startButton)
        startButton.isEnabled = false

        val teams = loadTeamsFromJson()
        teamAdapter = TeamsListAdapter(teams) { team ->
            if (selectedTeams.contains(team)) {
                selectedTeams.remove(team)
            } else if (selectedTeams.size < 2) {
                selectedTeams.add(team)
            }
            startButton.isEnabled = selectedTeams.size == 2
        }

        teamRecyclerView.layoutManager = LinearLayoutManager(this)
        teamRecyclerView.adapter = teamAdapter

        startButton.setOnClickListener {
            if (selectedTeams.size == 2) {
                val intent = Intent(this, MatchActivity::class.java)
                intent.putExtra(AppConstants.TEAM_1, selectedTeams[0].name)
                intent.putExtra(AppConstants.TEAM_2, selectedTeams[1].name)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Select exactly two teams!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //convert json into list
    private fun loadTeamsFromJson(): List<Team> {
        val json = resources.openRawResource(R.raw.teams_data).bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(json)
        val teams = mutableListOf<Team>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            teams.add(Team(jsonObject.getString("name"), jsonObject.getString("flag")))
        }
        return teams
    }
}