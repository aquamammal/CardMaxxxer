package com.cardmaxxxer.domain.repository

import com.cardmaxxxer.domain.model.UserCardCrossReference
import kotlinx.coroutines.flow.Flow

interface UserCardCrossReferenceRepository {
    fun observeByUser(userId: String): Flow<List<UserCardCrossReference>>
    suspend fun insert(crossRef: UserCardCrossReference)
    suspend fun update(crossRef: UserCardCrossReference)
}
