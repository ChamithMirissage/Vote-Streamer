package com.chamith.srilankavotestream

import org.json.JSONObject

interface FetchResultsCallback {

    fun onSuccess(result: JSONObject)
    fun onError(errorMessage: String)

}