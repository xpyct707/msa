package com.xpyct707.msa.easywallet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.xpyct707.msa"])
class MsaApplication

fun main(args: Array<String>) {
	runApplication<MsaApplication>(*args)
}
