package com.kryptonkoins.web.dto

import com.kryptonkoins.domain.Koin
import com.kryptonkoins.domain.Krypton
import org.springframework.validation.annotation.Validated
import java.math.BigDecimal
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Validated
data class BuyDTO(
    @NotEmpty val koin: Koin,
    @NotNull val shares: BigDecimal,
    @NotNull val price: BigDecimal,
    val fees: BigDecimal = BigDecimal("0.0000000000")
)

@Validated
data class SellDTO(
    @NotNull val shares: BigDecimal,
    @NotNull val price: BigDecimal,
    val fees: BigDecimal = BigDecimal("0.0000000000")
)

fun BuyDTO.toKrypton(): Krypton = Krypton(
    koin = this.koin,
    shares = this.shares,
    buy = this.price,
    buyFees = this.fees
)
