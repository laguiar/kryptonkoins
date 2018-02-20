package com.kryptonkoins

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories(basePackages = ["com.kryptopnkoins.repository"])
class KryptonKoinsApplication

fun main(args: Array<String>) {
    runApplication<KryptonKoinsApplication>(*args)
}
