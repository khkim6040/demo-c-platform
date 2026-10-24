package io.github.khkim6040.platform.http

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class StarterTestApplication

@RestController
class ProbeController {
    @GetMapping("/ok")
    fun ok() = mapOf("ok" to true)

    @GetMapping("/bad")
    fun bad(): Nothing = throw IllegalArgumentException("bad input")

    @GetMapping("/missing")
    fun missing(): Nothing = throw NoSuchElementException("nothing here")

    @GetMapping("/conflict")
    fun conflict(): Nothing = throw IllegalStateException("wrong state")
}
