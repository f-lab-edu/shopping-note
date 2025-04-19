package com.chaw.shopping_note.app.receipt.exception

class ReceiptNotFoundException(receiptId: Long) : RuntimeException("Receipt $receiptId not found")
