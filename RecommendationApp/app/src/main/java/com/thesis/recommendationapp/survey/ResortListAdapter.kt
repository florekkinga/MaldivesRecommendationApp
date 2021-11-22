package com.thesis.recommendationapp.survey

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.thesis.recommendationapp.R

class ResortListAdapter(var dataSet: Array<ResortDetails>, private val context: Context) :
    RecyclerView.Adapter<ResortListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resortNameTextView: TextView = view.findViewById(R.id.resortName)
        val resortDetailsTextView: TextView = view.findViewById(R.id.resortDetails)
        val resortScoreTextView: TextView = view.findViewById(R.id.score)
        val resortDetailsButton: Button = view.findViewById(R.id.buttonDetails)
        val bookingUrlButton: Button = view.findViewById(R.id.buttonBooking)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.resort, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resort = dataSet[position]
        holder.resortNameTextView.text = resort.name
        if(resort.address != ""){
            holder.resortDetailsTextView.text = resort.address
        }
        val score = resort.score.toString() + resort.suffix
        holder.resortScoreTextView.text = score
        if(resort.suffix == "%" && resort.score <= 80.0){
            holder.resortScoreTextView.setTextColor(Color.parseColor("#FFC107"))
            if(resort.score <= 60.0){
                holder.resortScoreTextView.setTextColor(Color.parseColor("#FF5722"))
            }
        }
        holder.bookingUrlButton.setOnClickListener {  }
        holder.resortDetailsButton.setOnClickListener {  }
    }

    override fun getItemCount(): Int = dataSet.size
}