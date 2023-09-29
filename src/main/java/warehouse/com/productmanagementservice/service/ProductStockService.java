package warehouse.com.productmanagementservice.service;

import warehouse.com.productmanagementservice.model.entity.ProductStock;

public interface ProductStockService {

  ProductStock findByProductIdAndWarehouseId(Long productId, Long warehouseId);
}
