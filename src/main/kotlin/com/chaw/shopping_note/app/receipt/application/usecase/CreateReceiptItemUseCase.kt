package com.chaw.shopping_note.app.receipt.application.usecase

import com.chaw.shopping_note.app.receipt.application.dto.CreateReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

@Service
class CreateReceiptItemUseCase(
    private val receiptItemRepository: ReceiptItemRepository,
) {

    suspend fun execute(input: CreateReceiptItemRequestDto): ReceiptItem {
        return createAndSaveReceiptItem(input)
    }

    private suspend fun createAndSaveReceiptItem(input: CreateReceiptItemRequestDto): ReceiptItem {
        val receiptItem = ReceiptItem.create(
            receiptId = input.receiptId,
            categoryId = input.categoryId,
            productName = input.productName,
            productCode = input.productCode,
            unitPrice = input.unitPrice,
            quantity = input.quantity,
        )

        receiptItemRepository.save(receiptItem).awaitSingle()
        return receiptItem
    }
}
