package com.chaw.shopping_note.app.receipt.application.dto

data class DeleteReceiptItemRequestDto(
    val userId: Long,
    val receiptItemId: Long
)
