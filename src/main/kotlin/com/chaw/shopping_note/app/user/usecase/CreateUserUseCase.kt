package com.chaw.shopping_note.app.user.usecase

import com.chaw.shopping_note.app.user.entity.User
import com.chaw.shopping_note.app.user.infrastructure.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class CreateUserUseCase(
    private val userRepository: UserRepository
) {

    @Transactional
    fun execute(user: User): Mono<User> {
        return userRepository.save(user)
            .flatMap { savedUser ->
                if (savedUser.name == "error") {
                    return@flatMap Mono.error(RuntimeException("이름이 error 면 롤백"))
                }

                Mono.just(savedUser)
            }
    }
}
