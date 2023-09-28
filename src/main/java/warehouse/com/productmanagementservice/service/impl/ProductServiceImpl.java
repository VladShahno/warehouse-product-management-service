package warehouse.com.productmanagementservice.service.impl;

import static warehouse.com.productmanagementservice.common.Constants.Logging.ENTITY_NOT_FOUND;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import warehouse.com.productmanagementservice.mapper.ProductMapper;
import warehouse.com.productmanagementservice.model.Product;
import warehouse.com.productmanagementservice.model.ProductGroup;
import warehouse.com.productmanagementservice.model.ProductStock;
import warehouse.com.productmanagementservice.model.Warehouse;
import warehouse.com.productmanagementservice.model.dto.request.ProductDto;
import warehouse.com.productmanagementservice.repository.ProductRepository;
import warehouse.com.productmanagementservice.repository.ProductStockRepository;
import warehouse.com.productmanagementservice.repository.WarehouseRepository;
import warehouse.com.productmanagementservice.service.ProductGroupService;
import warehouse.com.productmanagementservice.service.ProductService;
import warehouse.com.productmanagementservice.service.WarehouseService;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final ProductGroupService productGroupService;
  private final WarehouseService warehouseService;
  private final WarehouseRepository warehouseRepository;
  private final ProductStockRepository productStockRepository;

  @Override
  public Product create(ProductDto productDto) {
    ProductGroup productGroup = productGroupService.findById(productDto.getProductGroupId());

    List<Warehouse> warehouses = productDto.getWarehouseIds()
        .stream()
        .map(warehouseRepository::findById)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();

    var product = productMapper.toEntityFromRequestDto(productDto);
    product.setProductGroup(productGroup);
    product.setWarehouses(warehouses);

    List<ProductStock> productStocks = productDto.getStockItems().stream()
        .flatMap(productStockDto ->
            warehouseRepository.findById(productStockDto.getWarehouseId())
                .map(warehouse -> ProductStock.builder()
                    .warehouse(warehouse)
                    .product(product)
                    .quantity(productStockDto.getQuantity())
                    .build())
                .stream())
        .toList();

    product.setStockItems(productStocks);

    productStockRepository.saveAll(productStocks);

    return productRepository.save(product);
  }

  @Override
  public Product update(Long id, ProductDto productDto) {

    return null;
  }

  @Override
  public Product findById(Long id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, id)));
  }

  @Override
  public Page<Product> getAll(Pageable pageable) {
    return productRepository.findAllBy(pageable);
  }

  @Override
  public void deleteById(Long id) {
    productRepository.deleteById(id);
  }
}
