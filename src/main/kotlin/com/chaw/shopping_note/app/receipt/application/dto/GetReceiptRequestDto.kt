package com.chaw.shopping_note.app.receipt.application.dto

data class GetReceiptRequestDto(
    val userId: Long,
    val receiptId: Long
)
