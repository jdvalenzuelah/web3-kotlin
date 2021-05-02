package com.github.web3client.eth

import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.methods.response.EthGasPrice
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger

class EthRepo(
    private val web3: Web3j,
) {

    data class NetworkInfo(
        val blockNumber: BigInteger,
        val chainId: BigInteger,
        val gasPrice: BigInteger
    )

    fun getEthAccounts(): List<String> {
        return web3.ethAccounts().send().accounts
    }

    fun sendEthFunds(credentials: Credentials, toAddress: String, amount: BigDecimal): TransactionReceipt {
        return Transfer.sendFunds(web3, credentials, toAddress, amount, Convert.Unit.ETHER).send()
    }

    private fun getEthAccountBalance(account: String): BigInteger {
        val blockParameter = DefaultBlockParameter.valueOf(web3.ethBlockNumber().send().blockNumber)
        return web3.ethGetBalance(account, blockParameter).send().balance
    }

    fun getAllEthAccountsBalance(): Collection<Pair<String, BigInteger>> {
        return getEthAccounts().map { it to getEthAccountBalance(it) }
    }

    fun getNetworkInfo(): NetworkInfo {
        return NetworkInfo(
            web3.ethBlockNumber().send().blockNumber,
            web3.ethChainId().send().chainId,
            web3.ethGasPrice().send().gasPrice
        )
    }

}

fun String.privateKeyToCredentials(): Credentials = Credentials.create(this)
