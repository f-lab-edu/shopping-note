package com.chaw.shopping_note.app.receipt.application.dto

import com.chaw.shopping_note.app.receipt.domain.Category

data class UpdateReceiptItemRequestDto(
    val userId: Long,
    val receiptItemId: Long,
    val productName: String,
    val productCode: String,
    val unitPrice: Double,
    val quantity: Int,
    val category: Category
)
