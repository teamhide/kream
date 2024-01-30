package com.teamhide.kream

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KreamApplication

fun main(args: Array<String>) {
    runApplication<KreamApplication>(*args)
}
