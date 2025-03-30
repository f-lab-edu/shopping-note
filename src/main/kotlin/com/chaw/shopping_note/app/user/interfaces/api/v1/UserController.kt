package com.chaw.shopping_note.app.user.interfaces.api.v1

import com.chaw.shopping_note.app.user.entity.User
import com.chaw.shopping_note.app.user.usecase.CreateUserUseCase
import com.chaw.shopping_note.app.user.usecase.GetAllUsersUseCase
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val createUserUseCase: CreateUserUseCase
) {

    @Operation(summary = "사용자 조회")
    @GetMapping("/users")
    fun getUsers(): Flux<User> {
        return getAllUsersUseCase.execute()
    }

    @Operation(summary = "사용자 생성")
    @PostMapping
    fun createUser(
        @RequestBody user: User
    ): Mono<User> {
        return createUserUseCase.execute(user)
    }

}
