package com.xpyct707.msa.easywallet

import com.xpyct707.msa.easywallet.entity.Wallet
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class WalletControllerTests(private val databaseClient: DatabaseClient,
							private val client: WebTestClient) {
	@BeforeAll
	fun setup() {
		val rowsUpdated = databaseClient
				.execute("create table if not exists " +
						"wallet(id IDENTITY PRIMARY KEY, " +
						"user_id VARCHAR NOT NULL, " +
						"balance bigint NOT NULL" +
						")")
				.fetch()
				.rowsUpdated()
				.block()!!
		println("rowsUpdated: $rowsUpdated")
	}

	@Test
	fun walletCreateTest() {
		val userId = "a"
		val walletA = createWallet(userId)
		assertEquals(userId, walletA.userId)
		assertEquals(0L, walletA.balance)
	}

	private fun createWallet(userId: String)
			= client.post()
					.uri("/api/wallets?userId={userId}", userId)
					.exchange()
					.expectBody(Wallet::class.java)
					.returnResult()
					.responseBody!!

	@Test
	fun walletGetTest() {
		val walletBId = createWallet("b").id!!
		val walletB = getWallet(walletBId)
		assertEquals(walletBId, walletB.id)
	}

	private fun getWallet(id: Long)
			= tryGetWallet(id)
					.expectBody(Wallet::class.java)
					.returnResult()
					.responseBody!!

	private fun tryGetWallet(id: Long): WebTestClient.ResponseSpec {
		return client.get()
				.uri("/api/wallets/{id}", id)
				.exchange()
	}

	@Test
	fun walletDeleteTest() {
		val walletCId = createWallet("c").id!!
		deleteWallet(walletCId)
		tryGetWallet(walletCId)
				.expectStatus()
				.is5xxServerError
	}

	private fun deleteWallet(id: Long)
			= client.delete()
					.uri("/api/wallets/{id}", id)
					.exchange()
					.expectStatus()
					.isOk

	@Test
	fun walletReplenishTest() {
		val walletD = createWallet("d")
		assertEquals(0L, walletD.balance)

		val walletDId = walletD.id!!
		replenish(walletDId, 100L)

		assertEquals(100L, getWallet(walletDId).balance)

		replenish(walletDId, 300L)

		assertEquals(400L, getWallet(walletDId).balance)

		replenish(walletDId, -250L)

		assertEquals(150L, getWallet(walletDId).balance)
	}

	private fun replenish(id: Long, amount: Long)
			= client.post()
					.uri("/api/wallets/{id}/replenish?amount={amount}", id, amount)
					.exchange()
					.expectStatus()
					.isOk

	@Test
	fun walletTransferToTest() {
		val walletE = createWallet("e")
		assertEquals(0L, walletE.balance)

		val walletEId = walletE.id!!
		tryTransferTo(-1, walletEId, 100L)
				.expectStatus()
				.is4xxClientError

		tryTransferTo(walletEId, -1, 100L)
				.expectStatus()
				.is4xxClientError

		tryTransferTo(walletEId, walletEId, 100L)
				.expectStatus()
				.is4xxClientError

		val walletF = createWallet("f")
		assertEquals(0L, walletF.balance)
		val walletFId = walletF.id!!

		tryTransferTo(walletEId, walletFId, 100L)
				.expectStatus()
				.is4xxClientError

		replenish(walletEId, 50L)
		tryTransferTo(walletEId, walletEId, 20L)
				.expectStatus()
				.isOk
		assertEquals(50L, getWallet(walletEId).balance)

		tryTransferTo(walletEId, walletFId, 100L)
				.expectStatus()
				.is4xxClientError

		replenish(walletEId, 100L)
		tryTransferTo(walletEId, walletFId, 70L)
				.expectStatus()
				.isOk
		assertEquals(80L, getWallet(walletEId).balance)
		assertEquals(70L, getWallet(walletFId).balance)

		tryTransferTo(walletFId, walletEId, 25L)
				.expectStatus()
				.isOk
		assertEquals(105L, getWallet(walletEId).balance)
		assertEquals(45L, getWallet(walletFId).balance)
	}

	private fun tryTransferTo(from: Long, to: Long, amount: Long)
			= client.post()
			.uri("/api/wallets/{from}/transfer?to={to}&amount={amount}", from, to, amount)
			.exchange()

}
