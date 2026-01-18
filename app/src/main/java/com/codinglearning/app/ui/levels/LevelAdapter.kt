package com.codinglearning.app.ui.levels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codinglearning.app.R
import com.codinglearning.app.data.model.LevelState

class LevelAdapter(
    private val onLevelClick: (LevelState) -> Unit
) : ListAdapter<LevelState, LevelAdapter.LevelViewHolder>(LevelDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_level, parent, false)
        return LevelViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.bind(getItem(position), onLevelClick)
    }
    
    class LevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val card: CardView = itemView.findViewById(R.id.card_level)
        private val levelNumber: TextView = itemView.findViewById(R.id.tv_level_number)
        private val levelTitle: TextView = itemView.findViewById(R.id.tv_level_title)
        private val levelStatus: TextView = itemView.findViewById(R.id.tv_level_status)
        
        fun bind(levelState: LevelState, onClick: (LevelState) -> Unit) {
            levelNumber.text = "Level ${levelState.level.id}"
            levelTitle.text = levelState.level.title
            
            val context = itemView.context
            
            when {
                levelState.isCompleted -> {
                    levelStatus.text = context.getString(R.string.level_completed)
                    levelStatus.setTextColor(ContextCompat.getColor(context, R.color.level_completed))
                    card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_background_light))
                    card.isClickable = true
                }
                levelState.isUnlocked -> {
                    levelStatus.text = "Start"
                    levelStatus.setTextColor(ContextCompat.getColor(context, R.color.level_unlocked))
                    card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_background))
                    card.isClickable = true
                }
                else -> {
                    levelStatus.text = context.getString(R.string.level_locked)
                    levelStatus.setTextColor(ContextCompat.getColor(context, R.color.level_locked))
                    card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_background))
                    card.alpha = 0.5f
                    card.isClickable = false
                }
            }
            
            card.setOnClickListener {
                onClick(levelState)
            }
        }
    }
    
    class LevelDiffCallback : DiffUtil.ItemCallback<LevelState>() {
        override fun areItemsTheSame(oldItem: LevelState, newItem: LevelState): Boolean {
            return oldItem.level.id == newItem.level.id
        }
        
        override fun areContentsTheSame(oldItem: LevelState, newItem: LevelState): Boolean {
            return oldItem == newItem
        }
    }
}
