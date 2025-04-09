package com.chaw.shopping_note.app.receipt.application.usecase

import com.chaw.shopping_note.app.receipt.application.dto.GetReceiptRequestDto
import com.chaw.shopping_note.app.receipt.application.dto.GetReceiptResponseDto
import com.chaw.shopping_note.app.receipt.application.mapper.ReceiptMapper
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptRepository
import com.chaw.shopping_note.app.receipt.infrastructure.repository.StoreRepository
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.security.access.AccessDeniedException

@Service
class GetReceiptUseCase (
    private val storeRepository: StoreRepository,
    private val receiptRepository: ReceiptRepository,
    private val receiptItemRepository: ReceiptItemRepository,
    private val receiptMapper: ReceiptMapper
){

    suspend fun execute(input: GetReceiptRequestDto): GetReceiptResponseDto {
        val receipt = receiptRepository.findById(input.receiptId).awaitSingleOrNull()
            ?: throw IllegalArgumentException("Receipt with id ${input.receiptId} not found")
        if (receipt.userId != input.userId) {
            throw AccessDeniedException("No Permission")
        }

        val store = storeRepository.findById(receipt.storeId).awaitSingleOrNull()
        val receiptItems = receiptItemRepository.findAllByReceiptId(receipt.id!!)
            .collectList()
            .awaitSingle()

        return receiptMapper.toGetReceiptResponseDto(receipt, store, receiptItems)
    }
}
