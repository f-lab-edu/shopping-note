package com.chaw.shopping_note.app.receipt.application.mapper

import com.chaw.shopping_note.app.receipt.application.dto.CategoryResponseDto
import com.chaw.shopping_note.app.receipt.application.dto.GetReceiptResponseDto
import com.chaw.shopping_note.app.receipt.application.dto.ReceiptItemResponseDto
import com.chaw.shopping_note.app.receipt.application.dto.StoreResponseDto
import com.chaw.shopping_note.app.receipt.domain.Category
import com.chaw.shopping_note.app.receipt.domain.Receipt
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.domain.Store
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.IterableMapping

@Mapper(componentModel = "spring")
abstract class ReceiptMapper {

    @Mapping(source = "receipt.id", target = "id")
    @Mapping(source = "store", target = "store")
    @Mapping(source = "receiptItems", target = "items")
    abstract fun toGetReceiptResponseDto(
        receipt: Receipt,
        store: Store?,
        receiptItems: List<ReceiptItem>,
        @Context categoryMap: Map<Long, Category>
    ): GetReceiptResponseDto

    abstract fun toStoreResponseDto(store: Store): StoreResponseDto

    @Mapping(target = "isUnknown", ignore = true)
    abstract fun toCategoryResponseDto(category: Category): CategoryResponseDto

    @IterableMapping(qualifiedByName = ["toReceiptItemResponseDto"])
    abstract fun toReceiptItemResponseDtoList(
        receiptItems: List<ReceiptItem>,
        @Context categoryMap: Map<Long, Category>
    ): List<ReceiptItemResponseDto>

    @Named("toReceiptItemResponseDto")
    @Mapping(source = "receiptItem", target = "category", qualifiedByName = ["mapCategory"])
    abstract fun toReceiptItemResponseDto(
        receiptItem: ReceiptItem,
        @Context categoryMap: Map<Long, Category>
    ): ReceiptItemResponseDto

    @Named("mapCategory")
    fun mapCategory(
        receiptItem: ReceiptItem,
        @Context categoryMap: Map<Long, Category>
    ): CategoryResponseDto =
        categoryMap[receiptItem.categoryId]
            ?.let { toCategoryResponseDto(it) }
            ?: CategoryResponseDto(0L, "Unknown Category", true)
}

