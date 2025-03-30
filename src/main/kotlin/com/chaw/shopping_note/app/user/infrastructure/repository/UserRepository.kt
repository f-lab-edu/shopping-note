package com.chaw.shopping_note.app.user.infrastructure.repository

import com.chaw.shopping_note.app.user.entity.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : ReactiveCrudRepository<User, Long> {
    suspend fun findByEmail(email: String): User
}
