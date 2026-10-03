package com.cardmaxxxer.data.repository

import com.cardmaxxxer.core.database.dao.UserCardCrossReferenceDao
import com.cardmaxxxer.data.local.mapper.toDomain
import com.cardmaxxxer.data.local.mapper.toEntity
import com.cardmaxxxer.domain.model.UserCardCrossReference
import com.cardmaxxxer.domain.repository.UserCardCrossReferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserCardCrossReferenceRepositoryImpl @Inject constructor(
    private val userCardCrossReferenceDao: UserCardCrossReferenceDao,
) : UserCardCrossReferenceRepository {

    override fun observeByUser(userId: String): Flow<List<UserCardCrossReference>> =
        userCardCrossReferenceDao.observeByUser(userId).map { list -> list.map { it.toDomain() } }

    override suspend fun insert(crossRef: UserCardCrossReference) {
        userCardCrossReferenceDao.insert(crossRef.toEntity())
    }

    override suspend fun update(crossRef: UserCardCrossReference) {
        userCardCrossReferenceDao.update(crossRef.toEntity())
    }
}
