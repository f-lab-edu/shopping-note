package com.chaw.shopping_note.app.receipt.application.usecase

import com.chaw.shopping_note.app.receipt.application.dto.DeleteReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.application.service.ReceiptService
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.stereotype.Service

@Service
class DeleteReceiptItemUseCase(
    private val receiptItemRepository: ReceiptItemRepository,
    private val receiptService: ReceiptService
) {

    suspend fun execute(input: DeleteReceiptItemRequestDto): Boolean {
        val receiptItem = receiptService.findReceiptItem(input.receiptItemId)
        deleteReceiptItem(receiptItem)
        return true
    }

    private suspend fun deleteReceiptItem(receiptItem: ReceiptItem) {
        receiptItemRepository.delete(receiptItem).awaitFirstOrNull()
    }
}
