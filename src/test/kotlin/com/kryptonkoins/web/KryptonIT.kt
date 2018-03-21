package com.kryptonkoins.web

import com.kryptonkoins.domain.Koin
import com.kryptonkoins.web.dto.KryptonDTO
import com.kryptonkoins.web.dto.Portfolio
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import java.math.BigDecimal

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class KryptonIT {

    @Autowired
    lateinit var client: WebTestClient

    @Test
    fun shouldBuyKrypton() {
        val dto = KryptonDTO(
            koin = Koin.BTC,
            shares = BigDecimal.ONE,
            price = BigDecimal("1000.00")
        )

        client.post().uri("/kryptons/buy")
            .syncBody(dto)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.koin").isEqualTo("BTC")
            .jsonPath("$.shares").isEqualTo("1")
            .jsonPath("$.buy").isEqualTo("1000.0")
    }

    @Test
    @DisplayName("Should Calculate the Portfolio")
    fun shouldReturnPortfolioWithCalculatedGain() {
        val dto = KryptonDTO(
            koin = Koin.BTC,
            shares = BigDecimal.ONE,
            price = BigDecimal("100.0")
        )

        client.post().uri("/kryptons/buy")
            .syncBody(dto)
            .exchange()
            .expectStatus().isOk

        // TODO - fake the external service integration call
        val result = client.get().uri("/portfolio")
            .exchange()
            .expectStatus().isOk
            .returnResult<Portfolio>()

        val portfolio = result.responseBody.blockFirst()!!

        assertEquals(1, portfolio.stocks.size)
        assertEquals(BigDecimal("150.0"), portfolio.balance)
        assertEquals(BigDecimal("50.0"), portfolio.gains)
        assertEquals(50.0, portfolio.percent)
    }

}
