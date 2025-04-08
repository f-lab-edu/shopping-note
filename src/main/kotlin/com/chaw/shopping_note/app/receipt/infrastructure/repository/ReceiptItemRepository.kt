package com.chaw.shopping_note.app.receipt.infrastructure.repository

import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ReceiptItemRepository : CrudRepository<ReceiptItem, Long> {
    fun findAllByReceiptId(receiptId: Long): List<ReceiptItem>
}
