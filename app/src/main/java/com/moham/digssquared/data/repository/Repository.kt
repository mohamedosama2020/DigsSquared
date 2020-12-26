package com.moham.digssquared.data.repository


import com.moham.digssquared.data.remote.RemoteDataSource
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) {

    suspend fun getValues() = remoteDataSource.getValues()

}