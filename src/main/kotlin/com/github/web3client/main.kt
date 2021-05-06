package com.github.web3client

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.clikt.parameters.types.int
import com.github.web3client.api.EthApi
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import com.github.web3client.eth.EthRepo
import com.github.web3client.ui.TransactionsApp
import org.http4k.server.Apache4Server
import org.http4k.server.asServer

enum class AppMode {
    SITE,
    API
}

private class TransactionsAppRunner : CliktCommand() {

    private val url by option("-u", "--url", help = "URL of web3 service").default("http://localhost:8721/")
    private val port by option("-p", "--port", help = "Port where webserver will be listening").int().default(8080)
    private val mode by option("-m", "--mode", help = "Mose use to execute app").enum<AppMode>(ignoreCase = true).default(AppMode.API)

    override fun run() {
        val web3 = Web3j.build(HttpService(url))

        val repo = EthRepo(web3)

        when(mode) {
            AppMode.SITE -> TransactionsApp(port = port, ethRepo = repo)
            AppMode.API -> EthApi(repo).asServer(Apache4Server(port)).start()
        }
    }

}


fun main(args: Array<String>) = TransactionsAppRunner().main(args)
