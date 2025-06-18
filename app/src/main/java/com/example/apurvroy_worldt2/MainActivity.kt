package com.example.apurvroy_worldt2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apurvroy_worldt2.models.Team
import com.example.apurvroy_worldt2.utils.AppConstants
import com.example.cricketmatch.TeamsListAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    private lateinit var teamRecyclerView: RecyclerView
    private lateinit var startButton: ExtendedFloatingActionButton
    private lateinit var selectionInfoText: MaterialTextView
    private lateinit var toolbar: MaterialToolbar
    private val selectedTeams = mutableListOf<Team>()
    private lateinit var teamAdapter: TeamsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize views
        teamRecyclerView = findViewById(R.id.teamsListRecyclerView)
        startButton = findViewById(R.id.startButton)
        selectionInfoText = findViewById(R.id.selectionInfo)
        toolbar = findViewById(R.id.toolbar)
        
        // Set up toolbar
        setSupportActionBar(toolbar)
        
        // Disable the start button initially
        startButton.isEnabled = false
        startButton.visibility = View.GONE

        val teams = loadTeamsFromJson()
        teamAdapter = TeamsListAdapter(teams) { team ->
            if (selectedTeams.contains(team)) {
                selectedTeams.remove(team)
            } else if (selectedTeams.size < 2) {
                selectedTeams.add(team)
            }
            
            // Update the UI based on selection state
            updateSelectionUI()
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
    
    private fun updateSelectionUI() {
        when (selectedTeams.size) {
            0 -> {
                selectionInfoText.text = "Select 2 teams to start the match"
                startButton.visibility = View.GONE
                startButton.isEnabled = false
            }
            1 -> {
                selectionInfoText.text = "Selected: ${selectedTeams[0].name}. Select one more team."
                startButton.visibility = View.GONE
                startButton.isEnabled = false
            }
            2 -> {
                selectionInfoText.text = "Selected: ${selectedTeams[0].name} vs ${selectedTeams[1].name}"
                startButton.visibility = View.VISIBLE
                startButton.isEnabled = true
                // Animate the FAB to draw attention
                startButton.extend()
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