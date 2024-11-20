package com.chamith.srilankavotestream

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chamith.srilankavotestream.databinding.ActivityChooseResultBinding
import com.chamith.srilankavotestream.spinners.SpinnerData

class ChooseResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        binding = ActivityChooseResultBinding.inflate(layoutInflater)
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

        binding.btnView.setOnClickListener {
            ElectionResultClient.fetchPollingDivisionResult(
                this,
                binding.spinnerElectoralDistrict.selectedItem.toString(),
                binding.spinnerPollingDivision.selectedItem.toString()
            )
        }
    }

    private fun updatePollingDivisionSpinner(pollingDivisions: List<String>) {
        val pollingDivisionsAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, pollingDivisions)
        pollingDivisionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPollingDivision.adapter = pollingDivisionsAdapter
    }
}