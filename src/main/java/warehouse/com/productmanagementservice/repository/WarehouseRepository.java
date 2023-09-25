package warehouse.com.productmanagementservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import warehouse.com.productmanagementservice.model.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

}