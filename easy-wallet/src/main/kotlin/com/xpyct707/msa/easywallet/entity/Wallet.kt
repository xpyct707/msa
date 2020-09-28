package com.xpyct707.msa.easywallet.entity

import org.springframework.data.annotation.Id

class Wallet(
        @Id
        var id: Long?,
        val userId: String,
        val balance: Long
)