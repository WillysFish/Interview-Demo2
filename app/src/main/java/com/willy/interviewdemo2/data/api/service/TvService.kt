package com.willy.interviewdemo2.data.api.service

import com.willy.interviewdemo2.data.api.model.DramasSampleApiRs
import io.reactivex.Single
import retrofit2.http.GET

interface TvService {

    @GET("interview/dramas-sample.json")
    fun getInterviewDramas(): Single<DramasSampleApiRs>
}