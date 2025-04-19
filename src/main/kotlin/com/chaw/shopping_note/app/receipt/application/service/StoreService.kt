package com.chaw.shopping_note.app.receipt.application.service

import com.chaw.shopping_note.app.receipt.domain.Store
import com.chaw.shopping_note.app.receipt.infrastructure.repository.StoreRepository
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

@Service
class StoreService (
    private val storeRepository: StoreRepository
){
    suspend fun findStoreById(storeId: Long): Store? {
        return storeRepository.findById(storeId).awaitSingleOrNull()
    }
}
