package com.chaw.shopping_note.app.receipt.application.usecase

import com.chaw.shopping_note.app.receipt.application.dto.UpdateReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.access.AccessDeniedException

@Service
class UpdateReceiptItemUseCase(
    private val receiptRepository: ReceiptRepository,
    private val receiptItemRepository: ReceiptItemRepository
) {

    @Transactional
    fun execute(input: UpdateReceiptItemRequestDto): ReceiptItem {
        val receiptItem = receiptItemRepository.findById(input.receiptItemId)
            .orElseThrow { IllegalArgumentException("ReceiptItem not found") }
        val receipt = receiptRepository.findById(receiptItem.receiptId)
            .orElseThrow { IllegalArgumentException("Receipt with id ${receiptItem.receiptId} not found") }
        if (receipt.userId != input.userId) {
            throw AccessDeniedException("No Permission")
        }

        receiptItem.update(
            input.productName,
            input.productCode,
            input.unitPrice,
            input.quantity,
            input.category
        )
        receiptItemRepository.save(receiptItem)

        val receiptItems = receiptItemRepository.findAllByReceiptId(receiptItem.receiptId)
        receipt.setTotal(receiptItems)
        receiptRepository.save(receipt)

        return receiptItem
    }
}
