package com.chaw.shopping_note.app.receipt.infrastructure.repository

import com.chaw.shopping_note.app.receipt.domain.Receipt
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ReceiptRepository : CrudRepository<Receipt, Long> {
}
