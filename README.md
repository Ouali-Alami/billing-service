#**BILLING-SERVICE**

Here  a sample project to learn:

the microservices architecture
how to manage a *spring* app (service) configuration with *VAULT* *CONSUL* 
how to manage the key/values of *VAULT* and *CONSUL*

## Design

    Todo: draw flow diagram

## List of services who interact with the billing services 

- config service:  Manage the services configuration
- gateway-service: Gateway between services --here optional (not the learning target)
- consul-service: Like config-service

## Required

**Java 17+** (or up)
**Maven** (to manage the dependencies)
**Git** (to clone the repository)

## Installation et Configuration

Clone the git repository :
```bash
git clone git@github.com:Ouali-Alami/billing-service.git
```

## INITIALIZE CONSUL IN SERVER MODE

### start Consul in server mode(qorum, write, read etc...) with your ip (localhost or network):

```bash
consul agent -server -bootstrap-expect=1 -data-dir=consul-data -ui -bind=YOUR_IP
#here data (kv,health etc...) are saved in consul-data directory feel free to change it with your path...
```
### ⚠️ At the restart of consul if you wanna clean workspace(no kv, etc.), ensure that consul-data directory is empty

### Adding key/value secrets in Consul

WARNING !!
ALL THE STEPS BELOW FOR **CONSUL** CAN BE DONE ONLY WITH THIS SCRIPT [_dev_consul_kv_generator.sh](_dev_consul_kv_generator.sh) feel free to modify it according to your needs...
 ```bash
./_dev_consul_kv_generator.sh
```
but more details about this script here...
 ```bash
# To put sone consul kv
consul kv put token/accessTokenTimeout $ACCESS_TOKEN_TIMEOUT
consul kv put token/refreshTokenTimeout $REFRESH_TOKEN_TIMEOUT
```

KV names and path are in accordance with billing-service Consul properties [application.properties](src/main/resources/application.properties)

There is no properties for consul in the billing-service, but you can add them, here an example:

```bash
# Configuration des clés pour Consul KV
consul.kv.prefix=token # <- token folder like my prefix below
```
And in accordance with this class too [MyConsulConfig.java](src/main/java/org/sid/billing/MyConsulConfig.java), kv names and directories(prefix), modify it to put some new kv/directories...
```code
@Component
@ConfigurationProperties(prefix = "token")

public class MyConsulConfig {
    private long accessTokenTimeout;
    private long refreshTokenTimeout;
```
TODO:  Consul screen

## INITIALIZE VAULT

### start Vault in Dev mode:

```bash
vault server -dev -log-level=debug
```
### ⚠️ A TOKEN WILL BE GENERATED LOOK UP THE LOG (Root Token: hvs..............)

### Adding key/value secrets in Vault

WARNING!! 
ALL THE STEPS BELOW FOR **VAULT** CAN BE DONE ONLY WITH THIS SCRIPT [_dev_vault_kv_generator.sh](_dev_vault_kv_generator.sh) feel free to modify it according to your needs...

 ```bash
./_dev_vault_kv_generator.sh YOUR_GENERATED_TOKEN
```
but more details about this script here...

If you work with terminal, export your token(non persistant variable only for the current session), your local addr, and create the kv:
```bash
export VAULT_ADDR="http://127.0.0.1:8200"
export VAULT_TOKEN = YOUR_GENERATED_TOKEN
```
```bash
vault kv put secret/billing-service user.username="example_user" user.password="example_password" user.opt="example_opt_value"
```
⚠️ Note: VAULT context(kv name, path, etc.) is in accordance with billing-service VAULT properties [application.properties](src/main/resources/application.properties),
to get/put the kv beteween billing service <-> vault .
```bash
spring.cloud.vault.kv.backend=secret
spring.cloud.vault.kv.default-context=billing-service
```
And this class too [MyVaultConfig.java](src/main/java/org/sid/billing/MyVaultConfig.java) :
```code
@ConfigurationProperties(prefix = "user")
public class MyVaultConfig {
    private String username;
    private String password;
    private String otp;
```
You can do this by  UI too at http://localhost:8200/...

    TODO:  vault screen 

## START SERVICE

before you run the service please put YOUR_GENERATED_TOKEN here [application.properties](src/main/resources/application.properties)
```bash
spring.cloud.vault.token=YOUR_GENERATED_TOKEN
```
## TEST
-POST some new VAULT KV but this time from the service [BillingServiceApplication.java](src/main/java/org/sid/billing/BillingServiceApplication.java) with VAULT API and not with a script:
```code
CommandLineRunner commandLineRunner(String[] args) {
return args1 -> {
            Versioned.Metadata resp =vaultTemplate.opsForVersionedKeyValue("secret")
            .put("keyPair", Map.of("priveyKey","fewfwef", "pubKey","fwe214233wer"));
		};
```
-GET the total (VAULT + CONSUL ) KV created from the script and the service:[ConsulConfigRestController.java](src/main/java/org/sid/billing/ConsulConfigRestController.java):
```code
-We create some new 
 @GetMapping("/myConfig")
    public Map<String, Object> myConfig() {
        return Map.of("myConsulConfig",myConsulConfig, "myVaultConfig",myVaultConfig);
    }
```
```bash
mvn spring-boot:run
```
## RESULT
To see yours KV from consul and vault  API's Go to http://localhost:8084/myConfig
```bash
management.endpoints.web.exposure.include=*
```
We can refresh the value with actuator :
```bash
curl -X POST http://localhost:8084/actuator/refresh
```
TODO:
-sample script to build a cluster of 2 consul agent 1 server(ip network) / 1 client(ip localhost)
-sample Script to create some certificate via vault and use it in the app
-sample script to create and handle JWT via vault




