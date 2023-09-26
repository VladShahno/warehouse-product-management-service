package warehouse.com.productmanagementservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import warehouse.com.productmanagementservice.model.Warehouse;

@Repository
public interface WarehouseRepository extends ReactiveCrudRepository<Warehouse, Long> {

}