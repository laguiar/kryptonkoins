package com.kryptonkoins.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class Router {

    @Bean
    fun routerFunction(handler: KryptonHandler) = router {

        ("/kryptons").nest {
            accept(MediaType.APPLICATION_JSON_UTF8)
            GET("/{id}", handler::get)
            POST("/buy", handler::buy)
            POST("/sell/{id}", handler::sell)
        }
        GET("/portfolio", handler::portfolio)
    }
}