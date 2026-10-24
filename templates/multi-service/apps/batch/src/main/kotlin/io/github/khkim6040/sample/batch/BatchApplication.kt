package io.github.khkim6040.sample.batch

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
class BatchApplication

fun main(args: Array<String>) {
    exitProcess(SpringApplication.exit(runApplication<BatchApplication>(*args)))
}
