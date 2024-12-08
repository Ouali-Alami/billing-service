# billing-service ()


**billing-service** is a sample service to learn how to manage the secrets via Vault and the billing-service configuration via Consul and Spring Config (2 ways) and the microservices architecture.

## Archi
  Todo: draw flow diagram
## List of services who interract with the billing services 

- config service:  Manage the services configuration
- gateway-service: Gateway betweens services --here optional (not the learning target)
- consul-service: Like config-service 


## Prérequis

- **Java 17+** (or up)
- **Maven** (to manage the dependancies)
- **Git** (to clone the repository)


## Installation et Configuration

1-Ensure that you have the same jdk17(or up) for all the servies.
2-Clone the 3 git repositories :

```bash
    git clone git@github.com:Ouali-Alami/config-service.git
    git clone git@github.com:Ouali-Alami/gateway-service.git -- here optional (not the learning target)
    git clone git@github.com:Ouali-Alami/billing-service.git
```

Ensure that you put the right properties in the files src/main/resources/application.properties of the billing-service:

```properties
spring.cloud.vault.token=YOUR_VAULT_TOKEN 
spring.config.import=optional:consul:, vault://  #in you work in DEV mode by default let this config, vault:// <==> http://localhost:8200  
spring.cloud.vault.kv.enabled=true

```

Ensure that you have the right proerties for this 2/3 services ...
You can work via the terminal or the UIs...

## START  (DEV MODE)

### start Consul in dev mode:
```bash
ip=$(hostname -I)
consul agent -server -bootstrap-expect=1 -data-dir=consul-data -ui -bind="$ip"
```
If you work by UI , default addr here:
```bash
http://localhost:8500/
```
### start Vault in Dev mode:
```bash
vault server -dev -log-level=debug
```
A TOKEN WILL BE GENERATED LOOK UP THE LOG...
 
WARNING ALL THE STEPS AFTER FOR VAULT CAN BE DONE ONLY WITH THIS SCRIPT [_dev_vault_kv_generator.sh](_dev_vault_kv_generator.sh) you're free to modify it according to your needs...
```bash
./_dev_vault_kv_generator.sh YOUR_GENERATED_TOKEN
```
more details about this script here...

If you work with terminal, export your token(non persistant variable only for the current session), your local addr, and create the kv:
```bash
export VAULT_ADDR="http://127.0.0.1:8200"
export VAULT_TOKEN = YOUR_GENERATED_TOKEN
```
## Adding key/value secrets in Vault 
```bash
vault kv put secret/billing-service user.username="example_user" user.password="example_password" user.opt="example_opt_value"
```
KV names and path are in accordance with billing-service VAULT properties [application.properties.sh](src/main/resources/application.properties) :
```bash
    spring.cloud.vault.kv.backend=secret
    spring.cloud.vault.kv.default-context=billing-service
```
And this class [MyVaultConfig.java](src/main/java/org/sid/billing/MyVaultConfig.java) :
```code
@ConfigurationProperties(prefix = "user")
public class MyVaultConfig {
    private String username;
    private String password;
    private String otp;
```
You can do this by  UI too at http://localhost:8200/...

TODO:  vault screen 

## Adding key/value secrets in Consul 
KV names and path are in accordance with billing-service Consul properties [application.properties.sh](src/main/resources/application.properties) :
```bash
    spring.cloud.vault.kv.backend=secret
    spring.cloud.vault.kv.default-context=billing-service
```
And those kv names, you can add your own config kv... :
```code
@Component
@ConfigurationProperties(prefix = "token")

public class MyConsulConfig {
    private long accessTokenTimeout;
    private long refreshTokenTimeout;
```
TODO:  Consul screen 

For each service, you must start them independently, which is precisely the goal of microservices:
```bash
mvn spring-boot:run
```







