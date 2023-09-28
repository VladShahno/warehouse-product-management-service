package warehouse.com.productmanagementservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import warehouse.com.productmanagementservice.model.ProductStock;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {

}