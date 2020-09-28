package com.xpyct707.msa.easywallet.service

import com.xpyct707.msa.easywallet.entity.Wallet
import com.xpyct707.msa.easywallet.repository.WalletRepository
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class WalletService(private val walletRepository: WalletRepository) {
    fun create(userId: String): Mono<Wallet> {
        return walletRepository.save(Wallet(null, userId, 0L))
    }

    fun get(id: Long)
            = walletRepository.findById(id)
                              .switchIfEmpty {
                                  Mono.error(IllegalArgumentException("Wallet '$id' not found"))
                              }

    fun replenish(id: Long, amount: Long)
            = walletRepository.replenish(id = id, amount = amount)
                              .doOnNext { affected ->
                                  if (affected != 1L) throw ResponseStatusException(BAD_REQUEST, "Wallet not found")
                              }
                              .then()

    @Transactional
    fun transferTo(from: Long, to: Long, amount: Long)
            = walletRepository.withdraw(from, amount)
                              .single()
                              .zipWith(walletRepository.withdraw(to, -amount)
                                                       .single()) {
                                  affectedFrom, affectedTo ->
                                  if (affectedFrom != 1L) throw ResponseStatusException(BAD_REQUEST, "Incorrect source wallet")
                                  if (affectedTo != 1L) throw ResponseStatusException(BAD_REQUEST, "Incorrect target wallet")
                              }
                              .then()

    fun delete(id: Long) = walletRepository.deleteById(id)
}