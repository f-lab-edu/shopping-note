package com.chaw.shopping_note.app.receipt.application.dto

import java.time.LocalDateTime

data class CreateReceiptRequestDto(
    val userId: Long,
    val storeId: Long,
    val purchaseAt: LocalDateTime
)
