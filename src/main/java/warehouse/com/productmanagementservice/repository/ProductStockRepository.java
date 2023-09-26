package warehouse.com.productmanagementservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import warehouse.com.productmanagementservice.model.ProductStock;

@Repository
public interface ProductStockRepository extends ReactiveCrudRepository<ProductStock, Long> {

}