package com.chaw.shopping_note.app.receipt.application.dto

data class UpdateReceiptItemRequestDto(
    val userId: Long,
    val categoryId: Long,
    val receiptItemId: Long,
    val productName: String,
    val productCode: String,
    val unitPrice: Int,
    val quantity: Int,
)
