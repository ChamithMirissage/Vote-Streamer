package com.chamith.srilankavotestream

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.chamith.srilankavotestream.adapters.DistrictResultAdapter
import com.chamith.srilankavotestream.databinding.ActivityDistrictResultsBinding
import com.chamith.srilankavotestream.spinners.SpinnerData
import org.json.JSONObject

class DistrictResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDistrictResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        binding = ActivityDistrictResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recycleViewDistricts.layoutManager = GridLayoutManager(this, 2)
        val itemList = SpinnerData.electoralDistricts.drop(1)
        val districtWinners: HashMap<String, String> = HashMap()

        ElectionResultClient.fetchDistrictWinners(this, object : FetchResultsCallback {
            override fun onSuccess(result: JSONObject) {
                val districts = result.keys()
                while (districts.hasNext()) {
                    val district = districts.next()
                    val partyName = result.get(district)
                    districtWinners[district] = partyName.toString()
                }

                val adapter = DistrictResultAdapter(itemList, districtWinners) { position ->
                    ElectionResultClient.fetchDistrictResult(this@DistrictResultsActivity, itemList[position])
                }

                binding.recycleViewDistricts.adapter = adapter
            }

            override fun onError(errorMessage: String) {
                TODO("Not yet implemented")
            }
        });
    }
}