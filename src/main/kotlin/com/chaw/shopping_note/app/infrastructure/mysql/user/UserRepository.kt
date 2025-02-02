package com.chaw.shopping_note.app.infrastructure.mysql.user

import com.chaw.shopping_note.app.domain.user.entity.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository : ReactiveCrudRepository<User, Long> {
    suspend fun findByEmail(email: String): User
}
