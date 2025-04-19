package com.chaw.shopping_note.app.receipt.infrastructure.repository

import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface ReceiptItemRepository : ReactiveCrudRepository<ReceiptItem, Long> {
    fun findAllByReceiptId(receiptId: Long): Flux<ReceiptItem>
}
