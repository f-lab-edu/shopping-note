package com.chaw.shopping_note.app.presenter.controller.api.v1.user

import com.chaw.shopping_note.app.domain.user.entity.User
import com.chaw.shopping_note.app.domain.user.usecase.GetAllUsersUseCase
import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.delay
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val getAllUsersUseCase: GetAllUsersUseCase
) {

    @Operation(summary = "사용자 정보 조회")
    @GetMapping("/users")
    fun getUsers(): Flux<User> {
        return getAllUsersUseCase.execute()
    }

}
