package com.example.cricketmatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apurvroy_worldt2.R
import com.example.apurvroy_worldt2.models.Team

class TeamsListAdapter(private val teams: List<Team>, private val onTeamClick: (Team) -> Unit) :
    RecyclerView.Adapter<TeamsListAdapter.TeamViewHolder>() {

    private val selectedTeams = mutableSetOf<Team>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)
        return TeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team = teams[position]
        holder.teamName.text = team.name

        Glide.with(holder.teamFlag.context)
            .load(team.flag)
            .placeholder(R.drawable.default_flag2)
            .error(R.drawable.default_flag2)
            .into(holder.teamFlag)

        holder.itemView.setOnClickListener {
            onTeamClick(team)
            if (selectedTeams.contains(team)) {
                selectedTeams.remove(team)
                holder.itemView.setBackgroundResource(android.R.color.white)
            } else if (selectedTeams.size < 2) {
                selectedTeams.add(team)
                holder.itemView.setBackgroundResource(android.R.color.darker_gray)
            }
        }
    }

    override fun getItemCount() = teams.size

    class TeamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val teamName: TextView = view.findViewById(R.id.textTeamName)
        val teamFlag: ImageView = view.findViewById(R.id.imageTeamFlag)
    }
}
