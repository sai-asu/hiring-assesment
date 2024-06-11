package com.example.fetchexercise.data.network

import com.example.fetchexercise.data.model.HiringModel
import kotlinx.coroutines.flow.Flow

interface ApiRepository {
    suspend fun getHiringList(): Flow<List<HiringModel>>
}