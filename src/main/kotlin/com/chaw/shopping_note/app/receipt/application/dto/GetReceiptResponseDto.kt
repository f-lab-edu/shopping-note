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
    val unitPrice: Double,
    val quantity: Int,
    val totalPrice: Double,
    val category: Category,
    val updatedAt: LocalDateTime,
)

data class GetReceiptResponseDto(
    val id: Long,
    val store: StoreResponseDto?,
    val items: List<ReceiptItemResponseDto>,
    val purchaseAt: LocalDateTime,
    val totalPrice: Double,
    val totalCount: Int,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(
            receipt: Receipt,
            store: Store?,
            receiptItems: List<ReceiptItem>
        ): GetReceiptResponseDto {
            return GetReceiptResponseDto(
                id = receipt.id!!,
                store = store?.let {
                    StoreResponseDto(
                        id = store.id!!,
                        name = store.name,
                        type = store.type
                    )
                },
                items = receiptItems.map {
                    ReceiptItemResponseDto(
                        id = it.id!!,
                        productName = it.productName,
                        productCode = it.productCode,
                        unitPrice = it.unitPrice.toDouble(),  // 타입 맞추기
                        quantity = it.quantity,
                        totalPrice = it.totalPrice.toDouble(),
                        category = it.category,
                        updatedAt = it.updatedAt!!
                    )
                },
                purchaseAt = receipt.purchaseAt!!,
                totalPrice = receipt.totalPrice.toDouble(),
                totalCount = receipt.totalCount,
                createdAt = receipt.createdAt!!
            )
        }
    }
}
