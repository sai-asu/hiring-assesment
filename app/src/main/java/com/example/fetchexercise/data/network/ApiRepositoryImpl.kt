package com.example.fetchexercise.data.network

import com.example.fetchexercise.data.model.HiringModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ApiRepositoryImpl(private val service: ApiService): ApiRepository {
    override suspend fun getHiringList(): Flow<List<HiringModel>> = flow {
        emit(service.getHiringList())
    }
}