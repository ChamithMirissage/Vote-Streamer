package com.chamith.srilankavotestream.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class ElectionResult(

    val electoralDistrict: String,
    val pollingDivision: String,
    val addedTime: LocalDateTime

) : Parcelable
