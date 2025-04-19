package com.chaw.shopping_note.app.receipt.application.mapper

import com.chaw.shopping_note.app.receipt.application.dto.GetReceiptResponseDto
import com.chaw.shopping_note.app.receipt.application.dto.ReceiptItemResponseDto
import com.chaw.shopping_note.app.receipt.application.dto.StoreResponseDto
import com.chaw.shopping_note.app.receipt.domain.Receipt
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.domain.Store
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface ReceiptMapper {

    @Mapping(source = "receipt.id", target = "id")
    @Mapping(source = "store", target = "store")
    @Mapping(source = "receiptItems", target = "items")
    fun toGetReceiptResponseDto(
        receipt: Receipt,
        store: Store?,
        receiptItems: List<ReceiptItem>
    ): GetReceiptResponseDto

    fun toStoreResponseDto(store: Store): StoreResponseDto

    fun toReceiptItemResponseDto(receiptItem: ReceiptItem): ReceiptItemResponseDto

    fun toReceiptItemResponseDtoList(receiptItems: List<ReceiptItem>): List<ReceiptItemResponseDto>
}
