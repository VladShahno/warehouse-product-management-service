package warehouse.com.productmanagementservice.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import warehouse.com.productmanagementservice.model.ProductStock;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {

  @Transactional
  Optional<ProductStock> findByProduct_IdAndWarehouse_Id(Long productId, Long warehouseId);
}