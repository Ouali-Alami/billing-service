# BILLING-SERVICE

### Here  a sample project to learn:

#### - the microservices architecture
#### - how to manage a *spring* app (service) configuration with *VAULT* *CONSUL* 
#### - how to manage the keys/values of *VAULT* and *CONSUL*

## Design

    Todo: draw flow diagram

## Required

### **Java 17+**
### **Maven**
### **Git**

##### ensure that mvn use java 17
```bash
java -version #java 17
mvn -v #use java 17
# if not installed (linux)
sudo apt-get update
sudo apt install openjdk-17-jdk
sudo apt install maven
#optional:
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
echo "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> ~/.bashrc
echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc
source ~/.bashrc

```


## Installation & Configuration

### Clone the repository:
```bash
git clone git@github.com:Ouali-Alami/billing-service.git
```

### INITIALIZE CONSUL IN SERVER MODE

#### start Consul in server mode(qorum, write, read etc...) with your ip (localhost or network):

```bash
consul agent -server -bootstrap-expect=1 -data-dir=consul-data -ui -bind=YOUR_IP
#here data (kv,health etc...) are saved in consul-data directory feel free to change it with your path...
```
#### ⚠️At the restart of consul if you want a clean workspace(no kv, etc.), ensure that consul-data directory is empty

#### Adding key/value secrets in Consul

#### ⚠️ all the steps below for **consul** can be done with this script [_dev_consul_kv_generator.sh](_dev_consul_kv_generator.sh) feel free to modify it according to your needs...
 ```bash
./_dev_consul_kv_generator.sh
```
#### but more details about this script here...
 ```bash
# To put some consul kv, note: you can do it because you are a consul server with full privileges
consul kv put config/billing-service/token.refreshTokenTimeout $REFRESH_TOKEN_TIMEOUT
```

#### KV names and path are in accordance with billing-service Consul properties [application.properties](src/main/resources/application.properties)
```properties
spring.cloud.consul.config.prefixes=config
spring.cloud.consul.config.default-context=billing-service
```
#### And in accordance with this class too [MyConsulConfig.java](src/main/java/org/sid/billing/MyConsulConfig.java), kv names and directories(prefix), modify it to put some new kv/directories...
```java
@Component
@ConfigurationProperties(prefix = "token")

public class MyConsulConfig {
    private long accessTokenTimeout;
    private long refreshTokenTimeout;
```
#### TODO:  Consul screen
#### for more info about consul here : https://developer.hashicorp.com/consul/

### INITIALIZE VAULT

### start Vault in Dev mode:

```bash
vault server -dev -log-level=debug
```
#### ⚠️ A TOKEN(root) WILL BE GENERATED LOOK UP THE LOG (Root Token: hvs...)

### Adding key/value secrets in Vault

#### ⚠️ all the steps below for **vault** can be done with the script [_dev_vault_kv_generator.sh](_dev_vault_kv_generator.) feel free to modify it according to your needs...

 ```bash
./_dev_vault_kv_generator.sh YOUR_GENERATED_TOKEN
```
#### but more details about this script here...

#### If you work with terminal, export your token(non persistant variable only for the current session), your local addr, and create the kv:
```bash
export VAULT_ADDR="http://127.0.0.1:8200"
export VAULT_TOKEN = YOUR_GENERATED_TOKEN
```
```bash
vault kv put secret/billing-service user.username="example_user" user.password="example_password" user.opt="example_opt_value"
```
#### Note: VAULT context(kv name, path, etc.) is in accordance with billing-service VAULT properties [application.properties](src/main/resources/application.properties),
#### to get/put the kv between billing service <-> vault.
```properties
spring.cloud.vault.kv.backend=secret
spring.cloud.vault.kv.default-context=billing-service
```
#### And this class too [MyVaultConfig.java](src/main/java/org/sid/billing/MyVaultConfig.java) :
```java
@ConfigurationProperties(prefix = "user")
public class MyVaultConfig {
    private String username;
    private String password;
    private String otp;
```
#### You can do this by  UI too at http://localhost:8200/...

        TODO:  vault screen 

### START SERVICE

### ⚠️ before you run the service please put YOUR_GENERATED_TOKEN here [application.properties](src/main/resources/application.properties)
```properties
spring.cloud.vault.token=YOUR_GENERATED_TOKEN
```
### TEST
#### POST some new VAULT KV but this time from the service [BillingServiceApplication.java](src/main/java/org/sid/billing/BillingServiceApplication.java) with VAULT API and not with a script:
```java
//note: you can do it because you have a root token(init token dev mode no ttl..)
CommandLineRunner commandLineRunner(String[] args) {
return args1 -> {
            Versioned.Metadata resp =vaultTemplate.opsForVersionedKeyValue("secret")
            .put("keypair", Map.of("privateKey","fewfwef", "publicKey","fwe214233wer"));
		};
```
#### GET the total (VAULT + CONSUL) KV created from the script and the service:[ConsulConfigRestController.java](src/main/java/org/sid/billing/ConsulConfigRestController.java):
```java
 @GetMapping("/myConfig")
    public Map<String, Object> myConfig() {
        return Map.of("myConsulConfig",myConsulConfig, "myVaultConfig",myVaultConfig);
    }
```
```bash
mvn spring-boot:run
```
#### Now go to http://localhost:8084/myConfig to see yours KV from consul and vault.

#### Refresh the value with actuator:
```bash
curl -X POST http://localhost:8084/actuator/refresh
```
#### for more infos about vault here : https://developer.hashicorp.com/vault/
#### TODO:
#### -sample script to supply some vault appRole Token for not admin user
#### -sample script to build a cluster of 2 consul agent 1 server(ip network) / 1 client(ip localhost) 
#### -sample Script to create some certificate via vault and use it in the app
#### -sample script to create and handle JWT via vault

