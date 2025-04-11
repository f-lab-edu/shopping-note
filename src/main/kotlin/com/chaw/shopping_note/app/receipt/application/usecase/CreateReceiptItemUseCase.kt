package com.chaw.shopping_note.app.receipt.application.usecase

import com.chaw.shopping_note.app.receipt.application.dto.CreateReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.application.service.ReceiptService
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

@Service
class CreateReceiptItemUseCase(
    private val receiptItemRepository: ReceiptItemRepository,
    private val receiptService: ReceiptService
) {

    suspend fun execute(input: CreateReceiptItemRequestDto): ReceiptItem {
        val receipt = receiptService.findReceiptWithPermission(input.receiptId, input.userId)
        val receiptItem = createAndSaveReceiptItem(input)
        receiptService.updateReceiptTotal(receipt)
        return receiptItem
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
}
