package com.github.web3client.api

import com.github.web3client.eth.EthRepo
import com.github.web3client.eth.privateKeyToCredentials
import org.http4k.core.*
import org.http4k.format.Jackson.auto
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import java.math.BigDecimal

data class BaseResponse<T>(val data: T)
data class TransferFundsRequest(
    val from: String,
    val fromPrivateKey: String,
    val to: String,
    val amount: BigDecimal
)

fun EthApi(ethRepo: EthRepo): HttpHandler {

    val bodyLens = Body.auto<BaseResponse<Any>>().toLens()
    val requestBodyLens = Body.auto<TransferFundsRequest>().toLens()

    return routes(
        "/accountsBalance" bind GET to {
            Response(Status.OK).with(bodyLens of BaseResponse(ethRepo.getAllEthAccountsBalance()))
        },
        "/networkInfo" bind GET to {
            Response(Status.OK).with(bodyLens of BaseResponse(ethRepo.getNetworkInfo()))
        },
        "/transferFunds" bind POST to {
            val requestData = requestBodyLens(it)

            try {
                ethRepo.sendEthFunds(requestData.fromPrivateKey.privateKeyToCredentials(), requestData.to, requestData.amount)
                Response(Status.OK).with(bodyLens of BaseResponse("OK"))
            }catch (e: Exception) {
                Response(Status.BAD_REQUEST).with(bodyLens of BaseResponse(mapOf("error" to e.message)))
            }
        },
    )
}
