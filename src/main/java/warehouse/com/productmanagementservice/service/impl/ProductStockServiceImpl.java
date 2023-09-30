package warehouse.com.productmanagementservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import warehouse.com.productmanagementservice.model.entity.ProductStock;
import warehouse.com.productmanagementservice.repository.ProductStockRepository;
import warehouse.com.productmanagementservice.service.ProductStockService;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ProductStockServiceImpl implements ProductStockService {

  private final ProductStockRepository productStockRepository;

  @Override
  public ProductStock findByProductIdAndWarehouseId(Long productId, Long warehouseId) {
    return productStockRepository.findByProduct_IdAndWarehouse_Id(productId, warehouseId)
        .orElseThrow(
            () -> new EntityNotFoundException(String.format(
                "There is no Product Stock with the passed productId: %s and warehouseId: %s",
                productId, warehouseId)));
  }

  @Override
  public List<ProductStock> findAllByProductIdAndWarehouseId(Set<Long> productIds,
      List<Long> warehouseIds) {
    var productStocks = productStockRepository.findAllByProduct_IdInAndWarehouse_IdIn(productIds,
        warehouseIds);
    if (CollectionUtils.isEmpty(productStocks)) {
      throw new EntityNotFoundException(String.format(
          "There is no Product Stock with the passed productId: %s and warehouseId: %s",
          productIds, warehouseIds));
    }
    return productStocks;
  }
}
