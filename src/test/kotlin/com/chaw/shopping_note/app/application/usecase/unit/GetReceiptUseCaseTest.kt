package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.GetReceiptRequestDto
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

    private val storeRepository = mockk<StoreRepository>()
    private val receiptRepository = mockk<ReceiptRepository>()
    private val receiptItemRepository = mockk<ReceiptItemRepository>()

    private val getReceiptUseCase = GetReceiptUseCase(
        storeRepository,
        receiptRepository,
        receiptItemRepository
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
            totalPrice = 10000.0,
            totalCount = 2,
            createdAt = LocalDateTime.now()
        )

        val receiptItems = listOf(
            ReceiptItem(
                id = 100L,
                receiptId = receiptId,
                productName = "상품1",
                productCode = "P001",
                unitPrice = 5000.0,
                quantity = 1,
                totalPrice = 5000.0,
                category = Category.FOOD,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            ReceiptItem(
                id = 101L,
                receiptId = receiptId,
                productName = "상품2",
                productCode = "P002",
                unitPrice = 5000.0,
                quantity = 1,
                totalPrice = 5000.0,
                category = Category.FOOD,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        coEvery { receiptRepository.findById(receiptId) } returns Mono.just(receipt)
        coEvery { storeRepository.findById(receipt.storeId) } returns Mono.just(store)
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

    @Test
    fun `userId가 다르면 AccessDeniedException이 발생한다`() = runTest {
        // given
        val receiptId = 1L
        val userId = 123L
        val wrongUserId = 999L

        val receipt = Receipt(
            id = receiptId,
            userId = userId,
            storeId = 10L,
            purchaseAt = LocalDateTime.now(),
            totalPrice = 10000.0,
            totalCount = 2,
            createdAt = LocalDateTime.now()
        )

        coEvery { receiptRepository.findById(receiptId) } returns Mono.just(receipt)

        val input = GetReceiptRequestDto(
            userId = wrongUserId,
            receiptId = receiptId
        )

        // when & then
        assertFailsWith<AccessDeniedException> {
            getReceiptUseCase.execute(input)
        }
    }

    @Test
    fun `receiptId가 잘못되면 IllegalArgumentException이 발생한다`() = runTest {
        // given
        val wrongReceiptId = 999L

        coEvery { receiptRepository.findById(wrongReceiptId) } returns Mono.empty()

        val input = GetReceiptRequestDto(
            userId = 123L,
            receiptId = wrongReceiptId
        )

        // when & then
        assertFailsWith<IllegalArgumentException> {
            getReceiptUseCase.execute(input)
        }
    }
}
