package com.kryptonkoins.web

import com.kryptonkoins.domain.Koin
import com.kryptonkoins.web.dto.KryptonDTO
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
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

}
