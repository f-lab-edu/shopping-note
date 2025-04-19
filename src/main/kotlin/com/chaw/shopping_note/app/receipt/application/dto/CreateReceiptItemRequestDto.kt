package com.chaw.shopping_note.app.receipt.application.dto

import com.chaw.shopping_note.app.receipt.domain.Category

data class CreateReceiptItemRequestDto(
    val userId: Long,
    val receiptId: Long,
    val productName: String,
    val productCode: String,
    val unitPrice: Int,
    val quantity: Int,
    val category: Category
)
