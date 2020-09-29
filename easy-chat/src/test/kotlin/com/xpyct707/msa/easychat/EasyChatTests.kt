package com.xpyct707.msa.easychat

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.test.context.TestConstructor
import java.net.URI

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EasyChatTests {
    private lateinit var requester: RSocketRequester

    @BeforeAll
    fun init(@Autowired requesterBuilder: RSocketRequester.Builder, @LocalServerPort port: Int) {
        requester = requesterBuilder.connectWebSocket(URI("ws://localhost:$port/rsocket")).block()!!
    }

    @Test
    fun testRSocket() {
        val greeting = requester.route("/hello").data("World").retrieveMono(String::class.java).block()
        Assertions.assertEquals("Hello, World!", greeting)
    }
}
