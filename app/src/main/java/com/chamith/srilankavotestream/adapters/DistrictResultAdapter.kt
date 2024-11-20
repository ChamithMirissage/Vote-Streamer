package com.chamith.srilankavotestream.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chamith.srilankavotestream.R
import com.chamith.srilankavotestream.spinners.SpinnerData

class DistrictResultAdapter(
    private val items: List<String>,
    private val districtWinners: HashMap<String, String>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<DistrictResultAdapter.DistrictResultViewHolder>() {

    class DistrictResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemLayout = itemView
        val electoralDistrict: TextView = itemView.findViewById(R.id.textViewElectoralDistrict)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_district_result, parent, false)
        return DistrictResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: DistrictResultViewHolder, position: Int) {
        val electoralDistrict = items[position]
        holder.electoralDistrict.text = electoralDistrict

        if (districtWinners.containsKey(electoralDistrict)) {
            setItemViewBackground(holder.itemLayout, districtWinners[electoralDistrict].toString())
        } else {
            setItemViewBackground(holder.itemLayout, "None")
        }

        // Call the lambda function when the item is clicked
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun setItemViewBackground(itemView: View, party: String) {
        when (party) {
            "NATIONAL PEOPLE'S POWER" -> itemView.setBackgroundResource(R.drawable.layout_bg_npp)
            "SAMAGI JANA BALAWEGAYA" -> itemView.setBackgroundResource(R.drawable.layout_bg_sjb)
            "NEW DEMOCRATIC FRONT" -> itemView.setBackgroundResource(R.drawable.layout_bg_ndf)
            "ILANKAI TAMIL ARASU KACHCHI" -> itemView.setBackgroundResource(R.drawable.layout_bg_itak)
            "SRI LANKA PODUJANA PERAMUNA" -> itemView.setBackgroundResource(R.drawable.layout_bg_slpp)
            "SARVAJANA BALAYA" -> itemView.setBackgroundResource(R.drawable.layout_bg_sb)
            "UNITED NATIONAL PARTY" -> itemView.setBackgroundResource(R.drawable.layout_bg_unp)
            "None" -> itemView.setBackgroundResource(R.drawable.item_bg_recycler_view)
        }
    }

}