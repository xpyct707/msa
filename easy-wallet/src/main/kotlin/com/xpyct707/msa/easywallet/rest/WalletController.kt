package com.xpyct707.msa.easywallet.rest

import com.xpyct707.msa.easywallet.entity.Wallet
import com.xpyct707.msa.easywallet.service.WalletService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/wallets")
class WalletController(private val walletService: WalletService) {
    @PostMapping
    fun create(@RequestParam userId: String): Mono<Wallet> = walletService.create(userId)

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Mono<Wallet> = walletService.get(id)

    @PostMapping("/{id}/replenish")
    fun replenish(@PathVariable id: Long, @RequestParam amount: Long): Mono<Void> =
            walletService.replenish(id = id, amount = amount)

    @PostMapping("/{from}/transfer")
    fun transferTo(@PathVariable from: Long, @RequestParam to: Long, @RequestParam amount: Long): Mono<Void> =
            walletService.transferTo(from, to, amount)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): Mono<Void> = walletService.delete(id)
}
