package com.a360play.a360nautica.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SuggestionsAdapter(private val suggestions: List<String>) : RecyclerView.Adapter<SuggestionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val suggestion = suggestions[position]
        holder.bind(suggestion)
    }

    override fun getItemCount(): Int {
        return suggestions.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(android.R.id.text1)

        init {
            itemView.setOnClickListener {
                val selectedSuggestion = suggestions[adapterPosition]
                // Handle the selected suggestion as needed
            }
        }

        fun bind(suggestion: String) {
            textView.text = suggestion
        }
    }
}

