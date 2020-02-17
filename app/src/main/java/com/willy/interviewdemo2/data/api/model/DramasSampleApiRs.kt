package com.willy.interviewdemo2.data.api.model

import com.google.gson.annotations.SerializedName

data class DramasSampleApiRs(
    @SerializedName("data")
    val dramas: ArrayList<Drama>
)