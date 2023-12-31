package warehouse.com.productmanagementservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import warehouse.com.productmanagementservice.model.entity.ProductStock;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {

  @Transactional
  Optional<ProductStock> findByProduct_IdAndWarehouse_Id(Long productId, Long warehouseId);

  @Transactional
  List<ProductStock> findAllByProduct_IdInAndWarehouse_IdIn(Set<Long> productIds,
      List<Long> warehouseIds);
}