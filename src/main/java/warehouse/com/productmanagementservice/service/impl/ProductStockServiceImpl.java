package warehouse.com.productmanagementservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import warehouse.com.productmanagementservice.model.ProductStock;
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
}
