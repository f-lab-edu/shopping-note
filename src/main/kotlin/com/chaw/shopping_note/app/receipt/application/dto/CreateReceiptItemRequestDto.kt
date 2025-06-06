package com.chaw.shopping_note.app.receipt.application.dto

data class CreateReceiptItemRequestDto(
    val userId: Long,
    val categoryId: Long,
    val receiptId: Long,
    val productName: String,
    val productCode: String,
    val unitPrice: Int,
    val quantity: Int,
)
