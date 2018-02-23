package com.kryptonkoins.web.handler

import com.kryptonkoins.domain.sellIt
import com.kryptonkoins.repository.KryptonRepository
import com.kryptonkoins.web.dto.BuyDTO
import com.kryptonkoins.web.dto.SellDTO
import com.kryptonkoins.web.dto.toKrypton
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import java.util.*

@Component
class KryptonHandler(val repository: KryptonRepository) {

    fun list(req: ServerRequest) = ok().body(repository.findAll())

    fun get(req: ServerRequest) = ok().body(repository.findById(UUID.fromString(req.pathVariable("id"))))
        .switchIfEmpty(notFound().build())

    fun buy(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<BuyDTO>()
            .map { it.toKrypton() }
            .flatMap { ok().body(repository.save(it)) }
            .switchIfEmpty(badRequest().build())
    }

    fun sell(req: ServerRequest): Mono<ServerResponse> {
        return repository.findById(UUID.fromString(req.pathVariable("id")))
            .map { krypton ->
                req.bodyToMono<SellDTO>().map(krypton::sellIt)
            }
            .doOnNext { repository.saveAll(it) }
            .then(noContent().build())
            .switchIfEmpty(notFound().build())
    }

}
