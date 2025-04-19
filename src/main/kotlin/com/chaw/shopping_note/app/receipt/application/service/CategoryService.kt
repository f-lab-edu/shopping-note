package com.chaw.shopping_note.app.receipt.application.service

import com.chaw.shopping_note.app.receipt.domain.Category
import com.chaw.shopping_note.app.receipt.domain.Store
import com.chaw.shopping_note.app.receipt.infrastructure.repository.CategoryRepository
import com.chaw.shopping_note.app.receipt.infrastructure.repository.StoreRepository
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

@Service
class CategoryService (
    private val categoryRepository: CategoryRepository
){
    suspend fun findCategoryMapByIds(categoryIds: List<Long>): Map<Long, Category> {
        if (categoryIds.isEmpty()) return emptyMap()

        val categories = categoryRepository.findAllById(categoryIds)
            .collectList()
            .awaitSingle()

        return categories.associateBy { it.id!! }
    }
}
