package com.chaw.shopping_note.app.receipt.application.usecase

import com.chaw.shopping_note.app.receipt.application.dto.CreateReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptRepository
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

@Service
class CreateReceiptItemUseCase(
    private val receiptRepository: ReceiptRepository,
    private val receiptItemRepository: ReceiptItemRepository
) {

    suspend fun execute(input: CreateReceiptItemRequestDto): ReceiptItem {
        val receipt = receiptRepository.findById(input.receiptId).awaitSingleOrNull()
            ?: throw IllegalArgumentException("Receipt not found")

        if (receipt.userId != input.userId) {
            throw AccessDeniedException("No Permission")
        }

        val receiptItem = ReceiptItem.create(
            receiptId = input.receiptId,
            productName = input.productName,
            productCode = input.productCode,
            unitPrice = input.unitPrice,
            quantity = input.quantity,
            category = input.category
        )

        receiptItemRepository.save(receiptItem).awaitSingle()

        val receiptItems = receiptItemRepository.findAllByReceiptId(receiptItem.receiptId)
            .collectList()
            .awaitSingle()

        receipt.setTotal(receiptItems)
        receiptRepository.save(receipt).awaitSingle()

        return receiptItem
    }

}
