package warehouse.com.productmanagementservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import warehouse.com.productmanagementservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}