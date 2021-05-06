## HTTP API DOCS


### To get all accounts with balance

#### Request

GET `/accountsBalance`

#### Response
```json
{
  "data": [
    {
      "account": "0xe587d4c25fc0436fc46a6a53540cb52a5aadd5f8",
      "balance": 99999580000000000000
    },
    {
      "account": "0x711223e05a093ab2fa6ffcee7175cd1ea4a8a713",
      "balance": 100000000000000000000
    },
    {
      "account": "0x7ff0c9c3f27beb9ac8186a2f091c528324781d50",
      "balance": 100000000000000000000
    }
  ]
}
```

#### Example
```shell
curl -X GET "http://localhost:8080/accountsBalance"
```


### To get network info

#### Request

GET `/networkInfo`

#### Response
```json
{
  "data": {
    "blockNumber": 1,
    "chainId": 1337,
    "gasPrice": 20000000000
  }
}
```

#### Example
```shell
curl -X GET "http://localhost:8080/networkInfo"
```

### To transfer funds

#### Request

POST `/transferFunds`

#### Request

```json
{
  "from": "0xE587D4c25FC0436fC46A6A53540cB52a5AaDd5f8",
  "fromPrivateKey": "0x012fa5afd6477a3ff62ae9f49758f2f72f99ac7ef9cf9babf3142b64bcfd4736",
  "to": "0xE587D4c25FC0436fC46A6A53540cB52a5AaDd5f8",
  "amount": 1000
}
```

#### Response
##### Successful
```json
{
  "data": "OK"
}
```
##### Error
```json
{
  "data": {
    "error": "Error processing transaction request: sender doesn't have enough funds to send tx. The upfront cost is: 1000000420000000000000 and the sender's account only has: 99999160000000000000"
  }
}
```

#### Example
```shell
curl -X POST --data "{\"from\":\"0xE587D4c25FC0436fC46A6A53540cB52a5AaDd5f8\",\"fromPrivateKey\":\"0x012fa5afd6477a3ff62ae9f49758f2f72f99ac7ef9cf9babf3142b64bcfd4736\",\"to\":\"0xE587D4c25FC0436fC46A6A53540cB52a5AaDd5f8\",\"amount\":10}" "http://localhost:8080/transferFunds"
```
