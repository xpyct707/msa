package com.xpyct707.msa.easychat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.xpyct707.msa"])
class EasyChatApplication

fun main(args: Array<String>) {
	runApplication<EasyChatApplication>(*args)
}
