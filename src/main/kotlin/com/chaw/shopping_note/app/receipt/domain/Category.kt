package com.chaw.shopping_note.app.receipt.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("category")
data class Category (
    @Id val id: Long? = null,
    val name: String,
)
