package com.chaw.shopping_note.app.receipt.application.dto

import com.chaw.shopping_note.app.receipt.domain.*
import java.time.LocalDateTime

data class StoreResponseDto(
    val id: Long,
    val name: String,
    val type: StoreType
)

data class ReceiptItemResponseDto(
    val id: Long,
    val productName: String,
    val productCode: String,
    val unitPrice: Int,
    val quantity: Int,
    val totalPrice: Int,
    val category: Category,
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
