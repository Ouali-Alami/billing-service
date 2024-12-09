package org.sid.billing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.Versioned;

import java.util.Map;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BillingServiceApplication {

	@Autowired
	private VaultTemplate vaultTemplate;

	public static void main(String[] args) {
		SpringApplication.run(BillingServiceApplication.class, args);
	}

	CommandLineRunner commandLineRunner(String[] args) {
		return args1 -> {
			Versioned.Metadata resp =vaultTemplate.opsForVersionedKeyValue("secret")
					.put("keyPair", Map.of("priveyKey","fewfwef", "pubKey","fwe214233wer"));

			// here for a specific version of the key in vault
			//vaultTemplate.opsForKeyValue("secret", VaultKeyValueOperationsSupport.KeyValueBackend.versioned())
			//	.put("mySecret.p1", "fgrego434");
		};
	};

}
