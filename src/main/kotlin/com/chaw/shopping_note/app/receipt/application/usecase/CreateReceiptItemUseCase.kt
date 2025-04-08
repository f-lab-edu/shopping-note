package com.chaw.shopping_note.app.receipt.application.usecase

import com.chaw.shopping_note.app.receipt.application.dto.CreateReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptRepository
import com.github.jasync.sql.db.util.length
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.access.AccessDeniedException

@Service
class CreateReceiptItemUseCase (
    private val receiptRepository: ReceiptRepository,
    private val receiptItemRepository: ReceiptItemRepository
){

    @Transactional
    fun execute(input: CreateReceiptItemRequestDto): ReceiptItem {
        val receipt = receiptRepository.findById(input.receiptId)
            .orElseThrow { IllegalArgumentException("Receipt with id ${input.receiptId} not found") }
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
        receiptItemRepository.save(receiptItem)

        val receiptItems = receiptItemRepository.findAllByReceiptId(receiptItem.receiptId)
        receipt.setTotal(receiptItems)
        receiptRepository.save(receipt)

        return receiptItem
    }
}
