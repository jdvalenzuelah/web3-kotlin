package com.github.web3client

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import com.github.web3client.eth.EthRepo
import com.github.web3client.ui.TransactionsApp

private class TransactionsAppRunner : CliktCommand() {

    private val url by option("-u", "--url", help = "URL of web3 service").default("http://localhost:8721/")
    private val port by option("-p", "--port", help = "Port where webserver will be listening").int().default(8080)

    override fun run() {
        val web3 = Web3j.build(HttpService(url))

        val repo = EthRepo(web3)

        TransactionsApp(port = port, ethRepo = repo)
    }

}


fun main(args: Array<String>) = TransactionsAppRunner().main(args)
