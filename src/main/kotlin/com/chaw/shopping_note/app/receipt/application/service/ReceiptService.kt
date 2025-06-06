package com.chaw.shopping_note.app.receipt.application.service

import com.chaw.shopping_note.app.receipt.domain.Receipt
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.exception.ReceiptItemNotFoundException
import com.chaw.shopping_note.app.receipt.exception.ReceiptNotFoundException
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptRepository
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

@Service
class ReceiptService (
    private val receiptRepository: ReceiptRepository,
    private val receiptItemRepository: ReceiptItemRepository,
){
    suspend fun findReceiptWithPermission(receiptId: Long, userId: Long): Receipt {
        val receipt = receiptRepository.findById(receiptId).awaitSingleOrNull()
            ?: throw ReceiptNotFoundException(receiptId)

        receipt.validatePermission(userId)
        return receipt
    }

    suspend fun findReceiptItem(receiptItemId: Long): ReceiptItem {
        val receiptItem = receiptItemRepository.findById(receiptItemId).awaitSingleOrNull()
            ?: throw ReceiptItemNotFoundException()
        return receiptItem
    }

    suspend fun findAllReceiptItemByReceiptId(receiptId: Long): List<ReceiptItem> {
        return receiptItemRepository.findAllByReceiptId(receiptId).collectList().awaitSingle()
    }
}
