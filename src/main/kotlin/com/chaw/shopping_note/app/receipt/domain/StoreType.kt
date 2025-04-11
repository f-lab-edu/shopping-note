package com.chaw.shopping_note.app.receipt.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "store_type")
data class StoreType (
    @Id val id: Long? = null,
    val name: String,
)
