package com.chaw.shopping_note.app.receipt.application.usecase

import com.chaw.shopping_note.app.receipt.application.dto.GetReceiptRequestDto
import com.chaw.shopping_note.app.receipt.application.dto.GetReceiptResponseDto
import com.chaw.shopping_note.app.receipt.application.mapper.ReceiptMapper
import com.chaw.shopping_note.app.receipt.application.service.ReceiptService
import com.chaw.shopping_note.app.receipt.application.service.StoreService
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

@Service
class GetReceiptUseCase (
    private val receiptItemRepository: ReceiptItemRepository,
    private val receiptService: ReceiptService,
    private val storeService: StoreService,
    private val receiptMapper: ReceiptMapper
){

    suspend fun execute(input: GetReceiptRequestDto): GetReceiptResponseDto {
        val receipt = receiptService.findReceiptWithPermission(input.receiptId, input.userId)
        val store = storeService.findStoreById(receipt.storeId)
        val receiptItems = receiptItemRepository.findAllByReceiptId(receipt.id!!)
            .collectList()
            .awaitSingle()

        return receiptMapper.toGetReceiptResponseDto(receipt, store, receiptItems)
    }
}
