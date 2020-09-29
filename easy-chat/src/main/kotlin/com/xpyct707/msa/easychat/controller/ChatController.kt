package com.xpyct707.msa.easychat.controller

import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.annotation.ConnectMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class ChatController {
    @ConnectMapping
    fun connect(@Headers headers: Map<String, Any>,
                @Payload payload: String, requester: RSocketRequester): Mono<Void> {
        return Mono.empty()
    }


    @MessageMapping("/hello")
    fun getHelloMessage(@Headers headers: Map<String, Any>,
                        @Payload name: String, requester: RSocketRequester): Mono<String> {
        return Mono.just("Hello, $name!")
    }
}