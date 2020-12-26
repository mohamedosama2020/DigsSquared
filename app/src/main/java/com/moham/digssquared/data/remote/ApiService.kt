package com.moham.digssquared.data.remote


import com.moham.digssquared.data.entities.Values
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/random")
    suspend fun getValues() : Response<Values>
}