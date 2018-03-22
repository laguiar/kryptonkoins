package com.kryptonkoins.web

import com.kryptonkoins.domain.Krypton
import com.kryptonkoins.domain.sellIt
import com.kryptonkoins.repository.KryptonRepository
import com.kryptonkoins.web.dto.KryptonDTO
import com.kryptonkoins.web.dto.Portfolio
import com.kryptonkoins.web.dto.Stock
import com.kryptonkoins.web.dto.toKrypton
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.*

@Component
class KryptonHandler(val repository: KryptonRepository) {

    fun get(req: ServerRequest) = ok().body(repository.findById(UUID.fromString(req.pathVariable("id"))))
        .switchIfEmpty(notFound().build())

    fun buy(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<KryptonDTO>()
            .map { it.toKrypton() }
            .flatMap { ok().body(repository.save(it)) }
            .switchIfEmpty(badRequest().build())
    }

    fun sell(req: ServerRequest): Mono<ServerResponse> {
        return repository.findById(UUID.fromString(req.pathVariable("id")))
            .map { krypton ->
                req.bodyToMono<KryptonDTO>().map(krypton::sellIt)
            }
            .doOnNext { repository.saveAll(it) }
            .then(noContent().build())
            .switchIfEmpty(notFound().build())
    }

    fun portfolio(req: ServerRequest) = ok().body(toPortfolio(repository.findAll()))

    private fun toPortfolio(kryptons: Flux<Krypton>): Mono<Portfolio> {
        val stocks = kryptons
            .map(this::toStock)
            .collectList()
            .block() // TODO - I didn't manage to do it in a non-blocking way 😖
            .orEmpty()

        val balance = stocks.map { it.market }.reduce { acc, next -> acc.add(next) }
        val gains = stocks.map { it.gain }.reduce { acc, next -> acc.add(next) }
        val percent = stocks.sumByDouble { it.percent }

        return Mono.just(Portfolio(balance, gains, percent, stocks))
    }

    private fun toStock(krypton: Krypton): Stock {
        val totalCost = krypton.buy.plus(krypton.buyFees)
        val marketValue = krypton.shares.multiply(BigDecimal("150.0")) // TODO actual price from external api
        val gain = marketValue.minus(totalCost)

        return Stock(
            koin = krypton.koin,
            shares = krypton.shares,
            price = krypton.buy,
            fees = krypton.buyFees,
            market = marketValue,
            gain = gain,
            percent = gain.divide(totalCost).movePointRight(2).toDouble()
        )
    }

}
