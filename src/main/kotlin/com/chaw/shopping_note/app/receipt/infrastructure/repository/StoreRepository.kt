package com.chaw.shopping_note.app.receipt.infrastructure.repository

import com.chaw.shopping_note.app.receipt.domain.Store
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository : ReactiveCrudRepository<Store, Long> {
}
