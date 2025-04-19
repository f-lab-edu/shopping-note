package com.chaw.shopping_note.app.receipt.application.usecase

import com.chaw.shopping_note.app.receipt.application.dto.UpdateReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.application.service.ReceiptService
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.stereotype.Service

@Service
class UpdateReceiptItemUseCase(
    private val receiptItemRepository: ReceiptItemRepository,
    private val receiptService: ReceiptService
) {

    suspend fun execute(input: UpdateReceiptItemRequestDto): ReceiptItem {
        val receiptItem = receiptService.findReceiptItem(input.receiptItemId)
        val receipt = receiptService.findReceiptWithPermission(receiptItem.receiptId, input.userId)
        updateReceiptItem(receiptItem, input)
        receiptService.updateReceiptTotal(receipt)

        return receiptItem
    }

    private suspend fun updateReceiptItem(receiptItem: ReceiptItem, input: UpdateReceiptItemRequestDto) {
        receiptItem.update(
            input.categoryId,
            input.productName,
            input.productCode,
            input.unitPrice,
            input.quantity,
        )
        receiptItemRepository.save(receiptItem).awaitFirstOrNull()
    }
}
