package com.moham.digssquared.data.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
): BaseDataSource() {

    suspend fun getValues() = getResult { apiService.getValues() }
}