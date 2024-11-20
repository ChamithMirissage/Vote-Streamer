package com.chamith.srilankavotestream.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chamith.srilankavotestream.R
import com.chamith.srilankavotestream.data.ElectionResult
import java.util.ArrayList

class ElectionResultAdapter(
    private var electionResults: ArrayList<ElectionResult>,
    private val onItemClick: (ElectionResult) -> Unit
) : RecyclerView.Adapter<ElectionResultAdapter.ElectionResultViewHolder>() {

    class ElectionResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val electoralDistrict: TextView = itemView.findViewById(R.id.textViewElectoralDistrict)
        val pollingDivision: TextView = itemView.findViewById(R.id.textViewPollingDivision)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ElectionResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_election_result, parent, false)
        return ElectionResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ElectionResultViewHolder, position: Int) {
        val electionResult = electionResults[position]
        holder.electoralDistrict.text = electionResult.electoralDistrict
        holder.pollingDivision.text = electionResult.pollingDivision

        holder.itemView.setOnClickListener {
            onItemClick(electionResult)
        }
    }

    override fun getItemCount(): Int = electionResults.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateElectionResults(newResults: ArrayList<ElectionResult>) {
        electionResults = newResults
        notifyDataSetChanged()
    }

}