package com.chaw.shopping_note.app.receipt.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "store")
data class Store (
    @Id val id: Long? = null,

    // 마트 종류 (예: 코스트코)
    val storeTypeId: Long,

    // 마트 이름 (예: 코스트코 공세점)
    val name: String,
)
