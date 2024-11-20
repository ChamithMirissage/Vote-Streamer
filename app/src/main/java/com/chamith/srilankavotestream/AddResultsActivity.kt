package com.chamith.srilankavotestream

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.chamith.srilankavotestream.databinding.ActivityAddResultsBinding
import com.chamith.srilankavotestream.spinners.SpinnerData
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime

class AddResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddResultsBinding

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        binding = ActivityAddResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val electoralDistrictsAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, SpinnerData.electoralDistricts)
        electoralDistrictsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerElectoralDistrict.adapter = electoralDistrictsAdapter

        val candidatesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, SpinnerData.candidates)
        candidatesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerParty1.adapter = candidatesAdapter
        binding.spinnerParty2.adapter = candidatesAdapter
        binding.spinnerParty3.adapter = candidatesAdapter
        binding.spinnerParty4.adapter = candidatesAdapter
        binding.spinnerParty5.adapter = candidatesAdapter

        binding.spinnerElectoralDistrict.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedElectoralDistrict = SpinnerData.electoralDistricts[position]
                    if (position != 0) {
                        updatePollingDivisionSpinner(
                            SpinnerData.pollingDivisionsMap[selectedElectoralDistrict]
                                ?: emptyList()
                        )
                    } else {
                        updatePollingDivisionSpinner(emptyList())
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        binding.btnSave.setOnClickListener {
            val electoralDistrict = binding.spinnerElectoralDistrict.selectedItem.toString()
            val pollingDivision = binding.spinnerPollingDivision.selectedItem.toString()
            val party1 = binding.spinnerParty1.selectedItem.toString()
            val party1Votes = binding.editTextParty1.text.toString().toInt()
            val party2 = binding.spinnerParty2.selectedItem.toString()
            val party2Votes = binding.editTextParty2.text.toString().toInt()
            val party3 = binding.spinnerParty3.selectedItem.toString()
            val party3Votes = binding.editTextParty3.text.toString().toInt()
            val party4 = binding.spinnerParty4.selectedItem.toString()
            val party4Votes = binding.editTextParty4.text.toString().toInt()
            val party5 = binding.spinnerParty5.selectedItem.toString()
            val party5Votes = binding.editTextParty5.text.toString().toInt()
            val validVotes = binding.editTextValidVotes.text.toString().toInt()
            val polledVotes = binding.editTextPolledVotes.text.toString().toInt()
            val registeredVotes = binding.editTextRegisteredVotes.text.toString().toInt()

            // Instantiate the RequestQueue
            val queue = Volley.newRequestQueue(this)
            val url = "http://192.168.1.46:8080/vote-streamer/result"

            val postData = JSONObject().apply {
                put("electoralDistrict", electoralDistrict)
                put("pollingDivision", pollingDivision)

                val parties = JSONArray()
                parties.put(JSONObject().apply {
                    put("name", party1)
                    put("votes", party1Votes)
                    put(
                        "votePercentage",
                        String.format(
                            "%.2f",
                            100.0 * party1Votes.toDouble() / validVotes.toDouble()
                        ).toDouble()
                    )
                })
                parties.put(JSONObject().apply {
                    put("name", party2)
                    put("votes", party2Votes)
                    put(
                        "votePercentage",
                        String.format(
                            "%.2f",
                            100.0 * party2Votes.toDouble() / validVotes.toDouble()
                        ).toDouble()
                    )
                })
                parties.put(JSONObject().apply {
                    put("name", party3)
                    put("votes", party3Votes)
                    put(
                        "votePercentage",
                        String.format(
                            "%.2f",
                            100.0 * party3Votes.toDouble() / validVotes.toDouble()
                        ).toDouble()
                    )
                })
                parties.put(JSONObject().apply {
                    put("name", party4)
                    put("votes", party4Votes)
                    put(
                        "votePercentage",
                        String.format(
                            "%.2f",
                            100.0 * party4Votes.toDouble() / validVotes.toDouble()
                        ).toDouble()
                    )
                })
                parties.put(JSONObject().apply {
                    put("name", party5)
                    put("votes", party5Votes)
                    put(
                        "votePercentage",
                        String.format(
                            "%.2f",
                            100.0 * party5Votes.toDouble() / validVotes.toDouble()
                        ).toDouble()
                    )
                })

                put("parties", parties)
                put("validVotes", validVotes)
                put("polledVotes", polledVotes)
                put("registeredVotes", registeredVotes)
                put("addedTime", LocalDateTime.now())
            }

            // Json Object Request
            val jsonObjectRequest =
                JsonObjectRequest(Request.Method.POST, url, postData, { response: JSONObject ->
                    val status = response.getString("status")
                    if (status.equals("success")) {
                        binding.spinnerElectoralDistrict.setSelection(0)
                        binding.spinnerParty1.setSelection(0)
                        binding.editTextParty1.text.clear()
                        binding.spinnerParty2.setSelection(0)
                        binding.editTextParty2.text.clear()
                        binding.spinnerParty3.setSelection(0)
                        binding.editTextParty3.text.clear()
                        binding.spinnerParty4.setSelection(0)
                        binding.editTextParty4.text.clear()
                        binding.spinnerParty5.setSelection(0)
                        binding.editTextParty5.text.clear()
                        binding.editTextValidVotes.text.clear()
                        binding.editTextPolledVotes.text.clear()
                        binding.editTextRegisteredVotes.text.clear()

                        Toast.makeText(this, "Entry Saved!", Toast.LENGTH_SHORT).show()
                    }
                }, { error: VolleyError ->
                    error.printStackTrace()  // Log the error to get more details
                    val errorMsg =
                        error.networkResponse?.data?.let { String(it) } ?: "Unknown error"
                    Toast.makeText(this, "Entry Failed! Error: $errorMsg", Toast.LENGTH_LONG).show()
                })

            queue.add(jsonObjectRequest)
        }
    }

    private fun updatePollingDivisionSpinner(pollingDivisions: List<String>) {
        val pollingDivisionsAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, pollingDivisions)
        pollingDivisionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPollingDivision.adapter = pollingDivisionsAdapter
    }
}