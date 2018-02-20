package com.kryptonkoins.web.routing

import com.kryptonkoins.web.handler.KryptonHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class Router {

    @Bean
    fun routerFunction(handler: KryptonHandler) = router {

        ("/kryptons").nest {
            GET("/", handler::list)
            GET("/{id}", handler::get)
            POST("/buy", handler::buy)
            POST("/sell/{id}", handler::sell)
        }
    }
}