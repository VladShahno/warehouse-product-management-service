package warehouse.com.productmanagementservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import warehouse.com.productmanagementservice.model.ProductStock;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {

}