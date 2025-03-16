package kg.nurtelecom.coffee_sale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class CoffeeSaleBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeeSaleBackendApplication.class, args);
	}

}
