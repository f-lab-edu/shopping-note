package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.GetReceiptRequestDto
import com.chaw.shopping_note.app.receipt.application.mapper.ReceiptMapperImpl
import com.chaw.shopping_note.app.receipt.application.service.CategoryService
import com.chaw.shopping_note.app.receipt.application.service.ReceiptService
import com.chaw.shopping_note.app.receipt.application.service.StoreService
import com.chaw.shopping_note.app.receipt.application.usecase.GetReceiptUseCase
import com.chaw.shopping_note.app.receipt.domain.Category
import com.chaw.shopping_note.app.receipt.domain.Receipt
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.domain.Store
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class GetReceiptUseCaseTest {

    private val categoryService: CategoryService = mockk<CategoryService>()
    private val storeService: StoreService = mockk<StoreService>()
    private val receiptService: ReceiptService = mockk<ReceiptService>()
    private val receiptMapper = ReceiptMapperImpl()

    private val getReceiptUseCase = GetReceiptUseCase(
        categoryService,
        storeService,
        receiptService,
        receiptMapper
    )

    @Test
    fun success() = runTest {
        // given
        val receiptId = 1L
        val userId = 123L
        val categoryId = 1L
        val storeTypeId = 1L
        val store = Store(
            id = 10L,
            storeTypeId = storeTypeId,
            name = "코스트코 공세점",
        )
        val category = Category(
            id = categoryId,
            name = "FOOD",
        )

        val receipt = Receipt(
            id = receiptId,
            userId = userId,
            storeId = store.id!!,
            purchaseAt = LocalDateTime.now(),
            createdAt = LocalDateTime.now()
        )

        val receiptItems = listOf(
            ReceiptItem(
                id = 100L,
                receiptId = receiptId,
                categoryId = categoryId,
                productName = "상품1",
                productCode = "P001",
                unitPrice = 5000,
                quantity = 1,
                totalPrice = 5000,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            ReceiptItem(
                id = 101L,
                receiptId = receiptId,
                categoryId = categoryId,
                productName = "상품2",
                productCode = "P002",
                unitPrice = 5000,
                quantity = 1,
                totalPrice = 5000,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )
        val categoryIds = receiptItems.map { it.categoryId }.distinct()

        coEvery { receiptService.findReceiptWithPermission(receiptId, userId) } returns receipt
        coEvery { receiptService.findAllReceiptItemByReceiptId(receiptId) } returns receiptItems
        coEvery { storeService.findStoreById(receipt.storeId) } returns store
        coEvery { categoryService.findCategoryMapByIds(categoryIds) } returns mapOf(Pair(categoryId, category))

        val input = GetReceiptRequestDto(
            userId = userId,
            receiptId = receiptId
        )

        // when
        val result = getReceiptUseCase.execute(input)

        // then
        assertEquals(receiptId, result.id)
        assertEquals(store.name, result.store?.name)
        assertEquals(2, result.items.size)
        assertEquals(receiptItems[0].productName, result.items[0].productName)
        assertEquals(receiptItems[0].categoryId, result.items[0].category.id)
        assertEquals(category.name, result.items[0].category.name)
    }
}
