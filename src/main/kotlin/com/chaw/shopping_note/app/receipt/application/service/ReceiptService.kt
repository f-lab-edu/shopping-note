package com.chaw.shopping_note.app.receipt.application.service

import com.chaw.shopping_note.app.receipt.domain.Receipt
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

    suspend fun updateReceiptTotal(receipt: Receipt) {
        val receiptItems = receiptItemRepository.findAllByReceiptId(receipt.id!!)
            .collectList()
            .awaitSingle()
        receipt.setTotal(receiptItems)
        receiptRepository.save(receipt).awaitSingle()
    }
}
