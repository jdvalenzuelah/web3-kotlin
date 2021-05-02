package com.github.web3client.ui

import com.github.web3client.eth.EthRepo
import com.github.web3client.eth.privateKeyToCredentials
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import kweb.*
import kweb.plugins.fomanticUI.fomantic
import kweb.plugins.fomanticUI.fomanticUIPlugin
import kweb.state.KVar
import kweb.state.render
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger

class TransactionsApp(
    port: Int = 7659,
    debug: Boolean = false,
    private val ethRepo: EthRepo
) {

    private data class TransferInfo(val from: String, val fromPk: String, val to: String, val transferAmount: String) {
        fun isValid() = from.isNotEmpty() && fromPk.isNotEmpty() && to.isNotEmpty() && transferAmount.toBigDecimalOrNull() != null
    }

    val server: Kweb

    private val accounts: KVar<Collection<Pair<String, BigInteger>>>
    private val transfer: KVar<TransferInfo>
    private val network: KVar<EthRepo.NetworkInfo>

    init {
        server = Kweb(port = port, debug = debug, plugins = listOf(fomanticUIPlugin), buildPage = {
            doc.head {
                meta(name = "Description", content = "Simple UI for eth transactions")
            }

            doc.body {
                mainView {
                    transactionsView()
                }
            }

        })
        accounts = KVar(ethRepo.getAllEthAccountsBalance())
        transfer = KVar(TransferInfo("", "", "", ""))
        network = KVar(ethRepo.getNetworkInfo())

    }


    private fun ElementCreator<*>.mainView(content: ElementCreator<DivElement>.() -> Unit) {
        div(fomantic.ui.main.container) {
            div(fomantic.column) {
                div(fomantic.ui.vertical.segment) {
                    content(this)
                }
            }
        }
    }

    private fun ElementCreator<*>.transactionsView() {
        div(fomantic.ui.two.column.stackable.grid.container) {
            div(fomantic.ui.row) {
                div(fomantic.column) {
                    h1().text("Etherum Transfer Portal")
                }
                div(fomantic.column) {
                    networkInfo()
                }
            }
            div(fomantic.ui.row) {
                div(fomantic.column) {
                    accountsBalance()
                }
                div(fomantic.column) {
                    div(fomantic.ui.segment.vertical) {
                        transferForm()
                        processTransaction()
                    }
                }
            }
        }
    }

    private fun ElementCreator<*>.accountsBalance() {
        render(accounts) { fAccounts ->
            table(fomantic.ui.celled.table).new {
                thead().new {
                    tr().new {
                        th().text("Account address")
                        th().text("Balance")
                    }
                }
                tbody().new {
                    fAccounts.forEach { (account, balance) ->
                        tr().new {
                            td().text(account)
                            td().text(balance.toString())
                        }
                    }
                }
            }
        }
    }

    private fun ElementCreator<*>.transferForm() {
        lateinit var fromInput: InputElement
        lateinit var fromPkInput: InputElement
        lateinit var toInput: InputElement
        lateinit var amount: InputElement
        form(fomantic.ui.form) {
            div(fomantic.field) {
                label().text("From Address")
                fromInput = input(type = InputType.text, placeholder = "0x0000000000000000000000000000000000000000")
            }

            div(fomantic.field) {
                label().text("From private key")
                fromPkInput = input(
                    type = InputType.text,
                    placeholder = "0x0000000000000000000000000000000000000000000000000000000000000000"
                )
            }

            div(fomantic.field) {
                label().text("To Address")
                toInput = input(type = InputType.text, placeholder = "0x0000000000000000000000000000000000000000")
            }

            div(fomantic.field) {
                label().text("Transfer amount")
                amount = input(type = InputType.number, placeholder = "0")
            }

            button(fomantic.ui.button).text("Process").apply {
                on.click {
                    GlobalScope.launch {
                        transfer.value = transfer.value.copy(
                            from = fromInput.getValue().await(),
                            to = toInput.getValue().await(),
                            fromPk = fromPkInput.getValue().await(),
                            transferAmount = amount.getValue().await()
                        )
                    }
                }
            }
        }
    }

    private fun ElementCreator<*>.tryOrRenderError(block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            div(fomantic.ui.segment) {
                div(fomantic.ui.bottom.attached.error.message) {
                    i(fomantic.warning.icon)
                    text("An error occurred during execution, please check the fields and try again!")

                }
            }
        }
    }

    private fun ElementCreator<*>.processTransaction() {
        render(transfer) { transferInfo ->
            if(transferInfo.isValid()) {
                tryOrRenderError {
                    val transaction = this@TransactionsApp.processTransaction(transferInfo)

                    div(fomantic.ui.segment) {
                        div(fomantic.ui.segment.bottom.attached.success.message) {
                            i(fomantic.success.icon)
                            text("Processed Transaction!")
                            p().text("Transaction hash: ${transaction.transactionHash}")
                        }
                    }

                    accounts.value = ethRepo.getAllEthAccountsBalance()
                }
            }
        }
    }

    private fun processTransaction(transferInfo: TransferInfo): TransactionReceipt {
        return ethRepo.sendEthFunds(
            transferInfo.fromPk.privateKeyToCredentials(),
            transferInfo.to,
            transferInfo.transferAmount.toBigDecimal()
        )
    }

    private fun ElementCreator<*>.networkInfo() {
        render(network) { networkInfo ->
            div(fomantic.ui.card) {
                div(fomantic.content) {
                    div(fomantic.header).text("Network information:")
                }
                div(fomantic.content) {
                    div(fomantic.ui.small) {
                        div(fomantic.content) {
                            p().text("Chain ID: ${networkInfo.chainId}")
                        }
                    }
                }
                div(fomantic.content) {
                    div(fomantic.ui.small) {
                        div(fomantic.content) {
                            p().text("Block Number: ${networkInfo.blockNumber}")
                        }
                    }
                }

                div(fomantic.content) {
                    div(fomantic.ui.small) {
                        div(fomantic.content) {
                            p().text("Gas Price: ${networkInfo.gasPrice}")
                        }
                    }
                }
            }
        }
    }

}
