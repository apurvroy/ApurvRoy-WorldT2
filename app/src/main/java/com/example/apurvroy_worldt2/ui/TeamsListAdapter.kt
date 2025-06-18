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
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox

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
        
        // Update team status text based on selection state
        val isSelected = selectedTeams.contains(team)
        holder.teamStatus.text = if (isSelected) "Selected" else "Tap to select"
        
        // Update checkbox state
        holder.teamCheckbox.isChecked = isSelected
        
        // Update card appearance for selection state without using isChecked
        holder.teamCard.strokeWidth = if (isSelected) 2 else 1
        holder.teamCard.strokeColor = if (isSelected) 
            holder.itemView.context.getColor(R.color.md_theme_primary) 
            else holder.itemView.context.getColor(R.color.team_card_stroke)

        // Set background color based on selection
        holder.teamCard.setCardBackgroundColor(
            if (isSelected) holder.itemView.context.getColor(R.color.team_selected)
            else holder.itemView.context.getColor(R.color.md_theme_surface)
        )

        // Load team flag with Glide
        Glide.with(holder.teamFlag.context)
            .load(team.flag)
            .placeholder(R.drawable.default_flag2)
            .error(R.drawable.default_flag2)
            .into(holder.teamFlag)

        // Handle click events
        holder.teamCard.setOnClickListener {
            onTeamClick(team)
            if (selectedTeams.contains(team)) {
                selectedTeams.remove(team)
            } else if (selectedTeams.size < 2) {
                selectedTeams.add(team)
            }
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = teams.size

    class TeamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val teamCard: MaterialCardView = view.findViewById(R.id.teamCardView)
        val teamName: TextView = view.findViewById(R.id.textTeamName)
        val teamStatus: TextView = view.findViewById(R.id.textTeamStatus)
        val teamFlag: ImageView = view.findViewById(R.id.imageTeamFlag)
        val teamCheckbox: MaterialCheckBox = view.findViewById(R.id.teamSelectionCheckbox)
    }
}
