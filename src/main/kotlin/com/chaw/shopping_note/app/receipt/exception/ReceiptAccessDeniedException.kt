package com.chaw.shopping_note.app.receipt.exception

import org.springframework.security.access.AccessDeniedException

class ReceiptAccessDeniedException(receiptId: Long, userId: Long) : AccessDeniedException("No Permission to access this receipt $receiptId for user $userId")
