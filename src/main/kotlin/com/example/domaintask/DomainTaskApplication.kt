package com.example.domaintask

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DomainTaskApplication

fun main(args: Array<String>) {
    runApplication<DomainTaskApplication>(*args)
}

fun <T : Any> T.logger(): Logger = LoggerFactory.getLogger(javaClass)






