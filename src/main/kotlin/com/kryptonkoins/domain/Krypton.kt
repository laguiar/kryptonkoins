package com.kryptonkoins.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.*

@Document
data class Krypton(
    @Id
    val id: UUID = UUID.randomUUID(),
    val koin: Koin,
    val shares: BigDecimal,
    val buy: BigDecimal,
    val sell: BigDecimal = BigDecimal.ZERO,
    val buyFees: BigDecimal,
    val sellFees: BigDecimal = BigDecimal.ZERO
)
