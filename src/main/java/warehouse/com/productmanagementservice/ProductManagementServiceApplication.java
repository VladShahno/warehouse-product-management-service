package warehouse.com.productmanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties
@ComponentScan("warehouse.com")
public class ProductManagementServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductManagementServiceApplication.class, args);
  }

}
