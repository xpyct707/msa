package com.xpyct707.msa.easywallet.repository

import com.xpyct707.msa.easywallet.entity.Wallet
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import reactor.core.publisher.Mono

interface WalletRepository : ReactiveSortingRepository<Wallet, Long> {
    @Modifying
    @Query("UPDATE wallet SET balance = balance - :amount WHERE id = :id AND balance >= :amount")
    fun withdraw(id: Long, amount: Long): Mono<Long>

    @Modifying
    @Query("UPDATE wallet SET balance = balance + :amount WHERE id = :id")
    fun replenish(id: Long, amount: Long): Mono<Long>
}