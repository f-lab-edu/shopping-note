package com.chaw.shopping_note.app.receipt.application.usecase

import com.chaw.shopping_note.app.receipt.application.dto.CreateReceiptRequestDto
import com.chaw.shopping_note.app.receipt.domain.Receipt
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

@Service
class CreateReceiptUseCase (
    private val receiptRepository: ReceiptRepository
){
    suspend fun execute(input: CreateReceiptRequestDto): Receipt {
        val receipt = Receipt(
            userId = input.userId,
            storeId = input.storeId,
            purchaseAt = input.purchaseAt,
        )
        return receiptRepository.save(receipt).awaitSingle()
    }
}
