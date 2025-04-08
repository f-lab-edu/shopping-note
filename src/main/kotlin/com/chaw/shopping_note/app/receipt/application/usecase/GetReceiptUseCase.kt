package com.chaw.shopping_note.app.receipt.application.usecase

import com.chaw.shopping_note.app.receipt.application.dto.GetReceiptRequestDto
import com.chaw.shopping_note.app.receipt.application.dto.GetReceiptResponseDto
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptRepository
import com.chaw.shopping_note.app.receipt.infrastructure.repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.access.AccessDeniedException

@Service
class GetReceiptUseCase (
    private val storeRepository: StoreRepository,
    private val receiptRepository: ReceiptRepository,
    private val receiptItemRepository: ReceiptItemRepository
){

    @Transactional(readOnly = true)
    fun execute(input: GetReceiptRequestDto): GetReceiptResponseDto {
        val receipt = receiptRepository.findById(input.receiptId)
            .orElseThrow { IllegalArgumentException("Receipt with id ${input.receiptId} not found") }
        if (receipt.userId != input.userId) {
            throw AccessDeniedException("No Permission")
        }

        val store = storeRepository.findById(receipt.storeId).orElse(null)
        val receiptItems = receiptItemRepository.findAllByReceiptId(receipt.id!!)

        return GetReceiptResponseDto.from(receipt, store, receiptItems)
    }
}
