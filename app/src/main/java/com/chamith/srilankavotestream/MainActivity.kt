package com.chamith.srilankavotestream

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chamith.srilankavotestream.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnLatestResults.setOnClickListener {
            val intent = Intent(this, LatestResultsActivity::class.java)
            startActivity(intent)
        }

        binding.btnPollingDivisionResult.setOnClickListener {
            val intent = Intent(this, ChooseResultActivity::class.java)
            startActivity(intent)
        }

        binding.btnDistrictResult.setOnClickListener {
            val intent = Intent(this, DistrictResultsActivity::class.java)
            startActivity(intent)
        }

        binding.btnOverallResult.setOnClickListener {
            ElectionResultClient.fetchOverallResult(this)
        }

        binding.btnAddResults.setOnClickListener {
            val intent = Intent(this, AddResultsActivity::class.java)
            startActivity(intent)
        }

    }
}