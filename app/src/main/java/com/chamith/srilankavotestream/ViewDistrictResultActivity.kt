package com.chamith.srilankavotestream

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chamith.srilankavotestream.databinding.ActivityViewDistrictResultBinding
import com.chamith.srilankavotestream.databinding.ItemViewDistrictResultBinding
import org.json.JSONArray
import org.json.JSONObject

class ViewDistrictResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewDistrictResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        binding = ActivityViewDistrictResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val resultObject = intent.getStringExtra("resultObject") ?: ""
        val resultJson = JSONObject(resultObject)

        binding.textViewElectoralDistrict.text = intent.getStringExtra("electoralDistrict")
        if (intent.getStringExtra("pollingDivision") == "Result") {
            binding.textViewPollingDivision.text = "National Total"
        }
        val parties: JSONArray = resultJson.getJSONArray("parties")

        setItemViews(parties)
        setVotesViews(resultJson);
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun setItemViews(parties: JSONArray) {
        var itemView: ItemViewDistrictResultBinding = binding.itemViewParty1

        for (i in 0 until 5) {
            val partyObject: JSONObject = parties.getJSONObject(i)
            when (i) {
                1 -> itemView = binding.itemViewParty2
                2 -> itemView = binding.itemViewParty3
                3 -> itemView = binding.itemViewParty4
                4 -> itemView = binding.itemViewParty5
            }

            val partyName = partyObject.getString("name")
            itemView.textViewParty.text = partyName
            val seats = partyObject.getInt("seats")
            if (seats == -1) {
                itemView.textViewSeats.text = ""
            } else {
                itemView.textViewSeats.text = seats.toString()
            }
            itemView.textViewVotes.text =
                String.format("%,d", partyObject.getInt("votes"))
            val votePercentage = String.format("%.2f", partyObject.getDouble("votePercentage"))
            itemView.textViewPercentage.text = "$votePercentage%"
            setPartyLogoImage(itemView.imageViewLogo, partyName)
            setViewBackground(itemView.linearLayoutParty, partyName)

            val progressView = itemView.progressView
            val progressViewParams = progressView.layoutParams as LinearLayout.LayoutParams
            progressViewParams.weight = partyObject.getDouble("votePercentage").toInt().toFloat()
            progressView.layoutParams = progressViewParams

            val emptyView = itemView.emptyView
            val emptyViewParams = emptyView.layoutParams as LinearLayout.LayoutParams
            emptyViewParams.weight = 100.0f - partyObject.getDouble("votePercentage").toInt().toFloat()
            emptyView.layoutParams = emptyViewParams

            setProgressViewBackground(progressView, partyName)
        }
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun setVotesViews(resultJson: JSONObject) {
        val validVotes = resultJson.getString("validVotes").toInt()
        val polledVotes = resultJson.getString("polledVotes").toInt()
        val registeredVotes = resultJson.getString("registeredVotes").toInt()

        val validVotesView = binding.itemViewVotes1
        val polledVotesView = binding.itemViewVotes2
        val registeredVotesView = binding.itemViewVotes3

        validVotesView.textViewSectionName.text = getString(R.string.valid_votes)
        validVotesView.textViewVotes.text = String.format("%,d", validVotes)
        val validPercentage = String.format("%.2f", 100 * validVotes.toDouble() / polledVotes).toDouble()
        validVotesView.textViewVotesPercentage.text = "$validPercentage%"

        polledVotesView.textViewSectionName.text = getString(R.string.polled_votes)
        polledVotesView.textViewVotes.text = String.format("%,d", polledVotes)
        val polledPercentage = String.format("%.2f", 100 * polledVotes.toDouble() / registeredVotes).toDouble()
        polledVotesView.textViewVotesPercentage.text = "$polledPercentage%"

        registeredVotesView.textViewSectionName.text = getString(R.string.registered_votes)
        registeredVotesView.textViewVotes.text = String.format("%,d", registeredVotes)
        registeredVotesView.textViewVotesPercentage.text = ""
    }

    private fun setPartyLogoImage(imageView: ImageView, party: String) {
        when (party) {
            "NATIONAL PEOPLE'S POWER" -> imageView.setImageResource(R.drawable.logo_npp)
            "SAMAGI JANA BALAWEGAYA" -> imageView.setImageResource(R.drawable.logo_sjb)
            "NEW DEMOCRATIC FRONT" -> imageView.setImageResource(R.drawable.logo_ndf)
            "ILANKAI TAMIL ARASU KACHCHI" -> imageView.setImageResource(R.drawable.logo_itak)
            "SRI LANKA PODUJANA PERAMUNA" -> imageView.setImageResource(R.drawable.logo_slpp)
            "SARVAJANA BALAYA" -> imageView.setImageResource(R.drawable.logo_sb)
            "UNITED NATIONAL PARTY" -> imageView.setImageResource(R.drawable.logo_unp)
            else -> imageView.setImageResource(R.drawable.logo_ind)
        }
    }

    private fun setViewBackground(linearLayout: LinearLayout, party: String) {
        when (party) {
            "NATIONAL PEOPLE'S POWER" -> linearLayout.setBackgroundResource(R.drawable.layout_bg_npp)
            "SAMAGI JANA BALAWEGAYA" -> linearLayout.setBackgroundResource(R.drawable.layout_bg_sjb)
            "NEW DEMOCRATIC FRONT" -> linearLayout.setBackgroundResource(R.drawable.layout_bg_ndf)
            "ILANKAI TAMIL ARASU KACHCHI" -> linearLayout.setBackgroundResource(R.drawable.layout_bg_itak)
            "SRI LANKA PODUJANA PERAMUNA" -> linearLayout.setBackgroundResource(R.drawable.layout_bg_slpp)
            "SARVAJANA BALAYA" -> linearLayout.setBackgroundResource(R.drawable.layout_bg_sb)
            "UNITED NATIONAL PARTY" -> linearLayout.setBackgroundResource(R.drawable.layout_bg_unp)
            else -> linearLayout.setBackgroundResource(R.drawable.layout_bg_ind)
        }
    }

    private fun setProgressViewBackground(progressView: View, party: String) {
        when (party) {
            "NATIONAL PEOPLE'S POWER" -> progressView.setBackgroundResource(R.drawable.view_progression_bg_npp)
            "SAMAGI JANA BALAWEGAYA" -> progressView.setBackgroundResource(R.drawable.view_progression_bg_sjb)
            "NEW DEMOCRATIC FRONT" -> progressView.setBackgroundResource(R.drawable.view_progression_bg_ndf)
            "ILANKAI TAMIL ARASU KACHCHI" -> progressView.setBackgroundResource(R.drawable.view_progression_bg_itak)
            "SRI LANKA PODUJANA PERAMUNA" -> progressView.setBackgroundResource(R.drawable.view_progression_bg_slpp)
            "SARVAJANA BALAYA" -> progressView.setBackgroundResource(R.drawable.view_progression_bg_sb)
            "UNITED NATIONAL PARTY" -> progressView.setBackgroundResource(R.drawable.view_progression_bg_unp)
            else -> progressView.setBackgroundResource(R.drawable.view_progression_bg_ind)
        }
    }
}