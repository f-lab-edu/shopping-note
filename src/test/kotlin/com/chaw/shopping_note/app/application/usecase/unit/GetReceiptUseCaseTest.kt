package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.GetReceiptRequestDto
import com.chaw.shopping_note.app.receipt.application.mapper.ReceiptMapperImpl
import com.chaw.shopping_note.app.receipt.application.service.ReceiptService
import com.chaw.shopping_note.app.receipt.application.service.StoreService
import com.chaw.shopping_note.app.receipt.application.usecase.GetReceiptUseCase
import com.chaw.shopping_note.app.receipt.domain.Category
import com.chaw.shopping_note.app.receipt.domain.StoreType
import com.chaw.shopping_note.app.receipt.domain.Receipt
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.domain.Store
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptRepository
import com.chaw.shopping_note.app.receipt.infrastructure.repository.StoreRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.security.access.AccessDeniedException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import kotlin.test.assertFailsWith

class GetReceiptUseCaseTest {

    private val receiptItemRepository = mockk<ReceiptItemRepository>()
    private val receiptService: ReceiptService = mockk<ReceiptService>()
    private val storeService: StoreService = mockk<StoreService>()
    private val receiptMapper = ReceiptMapperImpl()

    private val getReceiptUseCase = GetReceiptUseCase(
        receiptItemRepository,
        receiptService,
        storeService,
        receiptMapper
    )

    @Test
    fun `영수증과 영수증 항목을 조회할 수 있다`() = runTest {
        // given
        val receiptId = 1L
        val userId = 123L
        val store = Store(
            id = 10L,
            name = "코스트코 공세점",
            type = StoreType.COSTCO
        )

        val receipt = Receipt(
            id = receiptId,
            userId = userId,
            storeId = store.id!!,
            purchaseAt = LocalDateTime.now(),
            totalPrice = 10000,
            totalCount = 2,
            createdAt = LocalDateTime.now()
        )

        val receiptItems = listOf(
            ReceiptItem(
                id = 100L,
                receiptId = receiptId,
                productName = "상품1",
                productCode = "P001",
                unitPrice = 5000,
                quantity = 1,
                totalPrice = 5000,
                category = Category.FOOD,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            ReceiptItem(
                id = 101L,
                receiptId = receiptId,
                productName = "상품2",
                productCode = "P002",
                unitPrice = 5000,
                quantity = 1,
                totalPrice = 5000,
                category = Category.FOOD,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        coEvery { receiptService.findReceiptWithPermission(receiptId, userId) } returns receipt
        coEvery { storeService.findStoreById(receipt.storeId) } returns store
        coEvery { receiptItemRepository.findAllByReceiptId(receiptId) } returns Flux.fromIterable(receiptItems)

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
        assertEquals("상품1", result.items[0].productName)
    }
}
