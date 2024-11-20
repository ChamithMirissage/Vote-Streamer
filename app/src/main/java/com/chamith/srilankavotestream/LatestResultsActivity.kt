package com.chamith.srilankavotestream

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chamith.srilankavotestream.adapters.ElectionResultAdapter
import com.chamith.srilankavotestream.data.ElectionResult
import com.chamith.srilankavotestream.databinding.ActivityLatestResultsBinding
import org.json.JSONObject
import java.time.LocalDateTime
import java.util.ArrayList

class LatestResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLatestResultsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ElectionResultAdapter
    private lateinit var progressBar: ProgressBar
    private var electionResults = ArrayList<ElectionResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        binding = ActivityLatestResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recycleViwLatest.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE

        ElectionResultClient.fetchLatestResults(this, object : FetchResultsCallback {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onSuccess(result: JSONObject) {
                val resultsArray = result.getJSONArray("results")

                for (i in 0 until resultsArray.length()) {
                    val resultObject = resultsArray.getJSONObject(i)
                    val electoralDistrict = resultObject.getString("electoralDistrict")
                    val pollingDivision = resultObject.getString("pollingDivision")
                    val addedTime = LocalDateTime.parse(resultObject.getString("addedTime"))

                    electionResults.add(
                        ElectionResult(electoralDistrict, pollingDivision, addedTime)
                    )
                }

                electionResults = ArrayList(electionResults.sortedByDescending { it.addedTime })

                binding.recycleViwLatest.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE

                adapter.updateElectionResults(electionResults)
            }

            override fun onError(errorMessage: String) {
                Toast.makeText(
                    this@LatestResultsActivity,
                    "Error: $errorMessage",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

//        electionResults = ArrayList(electionResults.sortedByDescending { it.addedTime })

        recyclerView = binding.recycleViwLatest
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ElectionResultAdapter(electionResults) { result ->
            ElectionResultClient.fetchPollingDivisionResult(
                this,
                result.electoralDistrict,
                result.pollingDivision
            )
        }

        recyclerView.adapter = adapter
    }
}