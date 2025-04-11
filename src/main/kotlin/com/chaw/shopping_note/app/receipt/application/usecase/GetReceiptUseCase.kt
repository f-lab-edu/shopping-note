package com.chaw.shopping_note.app.receipt.application.usecase

import com.chaw.shopping_note.app.receipt.application.dto.GetReceiptRequestDto
import com.chaw.shopping_note.app.receipt.application.dto.GetReceiptResponseDto
import com.chaw.shopping_note.app.receipt.application.mapper.ReceiptMapper
import com.chaw.shopping_note.app.receipt.application.service.CategoryService
import com.chaw.shopping_note.app.receipt.application.service.ReceiptService
import com.chaw.shopping_note.app.receipt.application.service.StoreService
import org.springframework.stereotype.Service

@Service
class GetReceiptUseCase (
    private val categoryService: CategoryService,
    private val storeService: StoreService,
    private val receiptService: ReceiptService,
    private val receiptMapper: ReceiptMapper
){

    suspend fun execute(input: GetReceiptRequestDto): GetReceiptResponseDto {
        val receipt = receiptService.findReceiptWithPermission(input.receiptId, input.userId)
        val receiptItems = receiptService.findAllReceiptItemByReceiptId(receipt.id!!)
        val store = storeService.findStoreById(receipt.storeId)
        val categoryIds = receiptItems.map { it.categoryId }.distinct()
        val categoryMap = categoryService.findCategoryMapByIds(categoryIds)

        return receiptMapper.toGetReceiptResponseDto(receipt, store, receiptItems, categoryMap)
    }
}
