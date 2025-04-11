package com.chaw.shopping_note.app.receipt.application.usecase

import com.chaw.shopping_note.app.receipt.application.dto.CreateReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.domain.Receipt
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.exception.ReceiptNotFoundException
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptRepository
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

@Service
class CreateReceiptItemUseCase(
    private val receiptRepository: ReceiptRepository,
    private val receiptItemRepository: ReceiptItemRepository
) {

    suspend fun execute(input: CreateReceiptItemRequestDto): ReceiptItem {
        val receipt = findReceiptWithPermission(input.receiptId, input.userId)
        val receiptItem = createAndSaveReceiptItem(input)
        updateReceiptTotal(receipt)
        return receiptItem
    }

    private suspend fun findReceiptWithPermission(receiptId: Long, userId: Long): Receipt {
        val receipt = receiptRepository.findById(receiptId).awaitSingleOrNull()
            ?: throw ReceiptNotFoundException(receiptId)

        receipt.validatePermission(userId)
        return receipt
    }

    private suspend fun createAndSaveReceiptItem(input: CreateReceiptItemRequestDto): ReceiptItem {
        val receiptItem = ReceiptItem.create(
            receiptId = input.receiptId,
            productName = input.productName,
            productCode = input.productCode,
            unitPrice = input.unitPrice,
            quantity = input.quantity,
            category = input.category
        )

        receiptItemRepository.save(receiptItem).awaitSingle()
        return receiptItem
    }

    private suspend fun updateReceiptTotal(receipt: Receipt) {
        val receiptItems = receiptItemRepository.findAllByReceiptId(receipt.id!!)
            .collectList()
            .awaitSingle()
        receipt.setTotal(receiptItems)
        receiptRepository.save(receipt).awaitSingle()
    }
}
