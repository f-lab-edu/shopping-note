package com.chaw.shopping_note.app.presenter.controller.api.v1.user

import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.delay
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/v1/user")
class UserController {

    @Operation(summary = "사용자 정보 조회")
    @GetMapping("/hello")
    suspend fun getUser(): String {
        delay(100)
        return "Hello, User!"
    }

    @GetMapping("/getItems")
    fun getItems(): Flux<String> {
        val client = WebClient.create("https://jsonplaceholder.typicode.com")
        return client.get()
            .uri("/posts")
            .retrieve()
            .bodyToFlux(String::class.java)
    }
}
