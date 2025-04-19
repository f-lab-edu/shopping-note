package com.chaw.shopping_note.app.receipt.application.dto

import java.time.LocalDateTime

data class StoreResponseDto(
    val id: Long,
    val storeTypeId: Long,
    val name: String,
)

data class CategoryResponseDto(
    val id: Long,
    val name: String,
    val isUnknown: Boolean = false
)

data class ReceiptItemResponseDto(
    val id: Long,
    val category: CategoryResponseDto,
    val productName: String,
    val productCode: String,
    val unitPrice: Int,
    val quantity: Int,
    val totalPrice: Int,
    val updatedAt: LocalDateTime,
)

data class GetReceiptResponseDto(
    val id: Long,
    val store: StoreResponseDto?,
    val items: List<ReceiptItemResponseDto>,
    val purchaseAt: LocalDateTime,
    val totalPrice: Int,
    val totalCount: Int,
    val createdAt: LocalDateTime,
)
