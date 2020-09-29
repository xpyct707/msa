package com.xpyct707.msa.easywallet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.xpyct707.msa"])
class EasyWalletApplication

fun main(args: Array<String>) {
	runApplication<EasyWalletApplication>(*args)
}
