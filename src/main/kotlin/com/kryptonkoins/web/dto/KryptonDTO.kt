package com.kryptonkoins.web.dto

import com.kryptonkoins.domain.Koin
import com.kryptonkoins.domain.Krypton
import org.springframework.validation.annotation.Validated
import java.math.BigDecimal
import javax.validation.constraints.NotNull

@Validated
data class KryptonDTO(
    @NotNull val koin: Koin,
    @NotNull val shares: BigDecimal,
    @NotNull val price: BigDecimal,
    val fees: BigDecimal = BigDecimal.ZERO
)

fun KryptonDTO.toKrypton(): Krypton = Krypton(
    koin = this.koin,
    shares = this.shares,
    buy = this.price,
    buyFees = this.fees
)

data class Portfolio(
    val overall: Double = 0.0,
    val balance: BigDecimal = BigDecimal.ZERO
)