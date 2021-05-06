# Kotlin Web3 Transactions Portal


## Requirements
### Install Java SDK 11
- Use [sdkman](http://sdkman.io/)
```sh
$ curl -s "https://get.sdkman.io" | bash
$ source "$HOME/.sdkman/bin/sdkman-init.sh"
$ sdk version
$ sdk install java
```

### Install Gradle 5.3 or higher
```sh
$ sdk update
$ sdk install gradle
```

## Usage
Clone and compile dist
```sh
$ git clone git@github.com:jdvalenzuelah/web3-kotlin.git ${YOUR_PROJECT_NAME}
$ cd ${YOUR_PROJECT_NAME}
$ ./gradlew clean build
```

Extract generated dist
```sh
$ tar -xf build/distributions/web3-kotlin-1.0.0.tar
```

Execute
```sh
$ ./web3-kotlin-1.0.0/bin/web3-kotlin
```
by defaults it tries to connect to web3 service on http://localhost:8721/ and starts site on port 8080.

To use a different web3 service use `--url` option 

Execute
```sh
$ ./web3-kotlin-1.0.0/bin/web3-kotlin --url "http://localhost:8545/"
```

To use a different port for the web server use `--port` option

Execute
```sh
$ ./web3-kotlin-1.0.0/bin/web3-kotlin --port 9000
```

Run as website or as json api, use `--mode` to change the mode of execution. `site` for website or `api` for json api
```shell

```
