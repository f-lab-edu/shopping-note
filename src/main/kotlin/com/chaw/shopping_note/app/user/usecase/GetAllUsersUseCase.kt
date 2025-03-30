package com.chaw.shopping_note.app.user.usecase

import com.chaw.shopping_note.app.user.entity.User
import com.chaw.shopping_note.app.user.infrastructure.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class GetAllUsersUseCase(
    private val userRepository: UserRepository
) {
    fun execute(): Flux<User> {
        return userRepository.findAll()
    }
}
