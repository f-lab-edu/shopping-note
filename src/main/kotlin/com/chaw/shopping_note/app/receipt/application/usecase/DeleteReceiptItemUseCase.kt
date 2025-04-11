package com.chaw.shopping_note.app.receipt.application.usecase

import com.chaw.shopping_note.app.receipt.application.dto.DeleteReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.application.service.ReceiptService
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.exception.ReceiptItemNotFoundException
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

@Service
class DeleteReceiptItemUseCase(
    private val receiptItemRepository: ReceiptItemRepository,
    private val receiptService: ReceiptService
) {

    suspend fun execute(input: DeleteReceiptItemRequestDto): Boolean {
        val receiptItem = findReceiptItem(input.receiptItemId)
        val receipt = receiptService.findReceiptWithPermission(receiptItem.receiptId, input.userId)
        deleteReceiptItem(receiptItem)
        receiptService.updateReceiptTotal(receipt)
        return true
    }

    private suspend fun findReceiptItem(receiptItemId: Long): ReceiptItem {
        val receiptItem = receiptItemRepository.findById(receiptItemId).awaitSingleOrNull()
            ?: throw ReceiptItemNotFoundException()
        return receiptItem
    }

    private suspend fun deleteReceiptItem(receiptItem: ReceiptItem) {
        receiptItemRepository.delete(receiptItem).awaitFirstOrNull()
    }
}
