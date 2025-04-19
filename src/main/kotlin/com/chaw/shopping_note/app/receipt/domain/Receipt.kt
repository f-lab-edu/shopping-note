package com.chaw.shopping_note.app.receipt.domain

import com.chaw.shopping_note.app.receipt.exception.ReceiptAccessDeniedException
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("receipt")
data class Receipt(
    @Id val id: Long? = null,

    // 소유자 - 사용자 ID
    val userId: Long,

    // 구매처 ID
    val storeId: Long,

    // 구매일
    var purchaseAt: LocalDateTime? = null,

    // 합계금액
    var totalPrice: Int,

    // 총 구매건수
    var totalCount: Int,

    // 생성일
    @CreatedDate
    val createdAt: LocalDateTime? = null
) {
    fun setTotal(receiptItems: List<ReceiptItem>) {
        val totalPrice = receiptItems.sumOf { it.totalPrice }
        this.totalPrice = totalPrice
        this.totalCount = receiptItems.size
    }

    fun validatePermission(userId: Long) {
        if (this.userId != userId) {
            throw ReceiptAccessDeniedException(this.id!!, userId)
        }
    }
}
