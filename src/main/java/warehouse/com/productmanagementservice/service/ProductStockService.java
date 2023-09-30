package warehouse.com.productmanagementservice.service;

import java.util.List;
import java.util.Set;
import warehouse.com.productmanagementservice.model.entity.ProductStock;

public interface ProductStockService {

  ProductStock findByProductIdAndWarehouseId(Long productId, Long warehouseId);

  List<ProductStock> findAllByProductIdAndWarehouseId(Set<Long> productIds,
      List<Long> warehouseIds);
}
