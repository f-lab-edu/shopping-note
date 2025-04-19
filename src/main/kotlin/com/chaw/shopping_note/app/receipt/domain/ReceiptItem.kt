package com.chaw.shopping_note.app.receipt.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("receipt_item")
data class ReceiptItem(
    @Id val id: Long? = null,

    val receiptId: Long,    // 어떤 영수증에 속해있는지

    var categoryId: Long, // 카테고리 ID

    var productName: String, // 상품명

    var productCode: String, // 상품코드

    var unitPrice: Int,     // 단가

    var quantity: Int,      // 수량

    var totalPrice: Int,    // 합계 (단가 * 수량)

    @CreatedDate
    val createdAt: LocalDateTime? = null,

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null
) {
    companion object {
        fun create(
            receiptId: Long,
            categoryId: Long,
            productName: String,
            productCode: String,
            unitPrice: Int,
            quantity: Int,
        ): ReceiptItem {
            return ReceiptItem(
                receiptId = receiptId,
                categoryId = categoryId,
                productName = productName,
                productCode = productCode,
                unitPrice = unitPrice,
                quantity = quantity,
                totalPrice = unitPrice * quantity,
            )
        }
    }

    fun update(
        categoryId: Long,
        productName: String,
        productCode: String,
        unitPrice: Int,
        quantity: Int,
    ) {
        this.categoryId = categoryId
        this.productName = productName
        this.productCode = productCode
        this.unitPrice = unitPrice
        this.quantity = quantity
        this.totalPrice = unitPrice * quantity
    }
}
