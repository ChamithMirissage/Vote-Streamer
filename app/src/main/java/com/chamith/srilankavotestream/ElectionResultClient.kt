package com.chamith.srilankavotestream

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

object ElectionResultClient {

    fun fetchPollingDivisionResult(
        context: Context,
        electoralDistrict: String,
        pollingDivision: String
    ) {
        val url =
            "http://192.168.1.46:8080/vote-streamer/result/$electoralDistrict/$pollingDivision"
        val queue = Volley.newRequestQueue(context)

        val jsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, url, null, { response: JSONObject ->
                if (response.getInt("validVotes") == 0) {
                    Toast.makeText(context, "Result hasn't been released yet", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val intent = Intent(context, ViewResultsActivity::class.java)
                    intent.putExtra("resultObject", response.toString())
                    context.startActivity(intent)
                }

            }, { error: VolleyError ->
                error.printStackTrace()
                val errorMsg =
                    error.networkResponse?.data?.let { String(it) } ?: "Unknown error"
                Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_LONG).show()
            })

        queue.add(jsonObjectRequest)
    }

    fun fetchDistrictResult(context: Context, electoralDistrict: String) {
        val url = "http://192.168.1.46:8080/vote-streamer/result/district/$electoralDistrict"
        val queue = Volley.newRequestQueue(context)

        val jsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, url, null, { response: JSONObject ->
                if (response.getInt("validVotes") == 0) {
                    Toast.makeText(context, "The results of the polling divisions in the district are yet to be released", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val intent = Intent(context, ViewDistrictResultActivity::class.java)
                    intent.putExtra("resultObject", response.toString())
                    intent.putExtra("electoralDistrict", electoralDistrict)
                    context.startActivity(intent)
                }

            }, { error: VolleyError ->
                error.printStackTrace()
                val errorMsg =
                    error.networkResponse?.data?.let { String(it) } ?: "Unknown error"
                Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_LONG).show()
            })

        queue.add(jsonObjectRequest)
    }

    fun fetchOverallResult(context: Context) {
        val url = "http://192.168.1.46:8080/vote-streamer/result/overall"
        val queue = Volley.newRequestQueue(context)

        val jsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, url, null, { response: JSONObject ->
                if (response.getInt("validVotes") == 0) {
                    Toast.makeText(context, "The results of the polling divisions are yet to be released", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val intent = Intent(context, ViewDistrictResultActivity::class.java)
                    intent.putExtra("resultObject", response.toString())
                    intent.putExtra("electoralDistrict", "ALL-ISLAND")
                    intent.putExtra("pollingDivision", "Result")
                    context.startActivity(intent)
                }

            }, { error: VolleyError ->
                error.printStackTrace()
                val errorMsg =
                    error.networkResponse?.data?.let { String(it) } ?: "Unknown error"
                Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_LONG).show()
            })

        queue.add(jsonObjectRequest)
    }

    fun fetchLatestResults(context: Context, callback: FetchResultsCallback) {
        val queue = Volley.newRequestQueue(context)
        val url = "http://192.168.1.46:8080/vote-streamer/result/latest"

        val jsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, url, null, { response: JSONObject ->
                callback.onSuccess(response)

            }, { error: VolleyError ->
                error.printStackTrace()
                val errorMsg = error.networkResponse?.data?.let { String(it) } ?: "Unknown error"
                Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_LONG).show()
                callback.onError(errorMsg)
            })

        queue.add(jsonObjectRequest)
    }

    fun fetchDistrictWinners(context: Context, callback: FetchResultsCallback) {
        val url = "http://192.168.1.46:8080/vote-streamer/winners"
        val queue = Volley.newRequestQueue(context)

        val jsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, url, null, { response: JSONObject ->
                callback.onSuccess(response)

            }, { error: VolleyError ->
                error.printStackTrace()
                val errorMsg = error.networkResponse?.data?.let { String(it) } ?: "Unknown error"
                Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_LONG).show()
                callback.onError(errorMsg)
            })

        queue.add(jsonObjectRequest)
    }

}