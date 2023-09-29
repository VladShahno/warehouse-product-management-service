package warehouse.com.productmanagementservice.service.impl;


import static net.logstash.logback.argument.StructuredArguments.keyValue;
import static warehouse.com.productmanagementservice.common.Constants.Logging.ID;
import static warehouse.com.productmanagementservice.common.Constants.Logging.NAME;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.ENTITY_EXISTS;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.ENTITY_NOT_FOUND;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.PRODUCT;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.PRODUCT_NAME;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import warehouse.com.productmanagementservice.mapper.ProductMapper;
import warehouse.com.productmanagementservice.model.Product;
import warehouse.com.productmanagementservice.model.ProductStock;
import warehouse.com.productmanagementservice.model.dto.request.ProductDto;
import warehouse.com.productmanagementservice.repository.ProductRepository;
import warehouse.com.productmanagementservice.repository.ProductStockRepository;
import warehouse.com.productmanagementservice.service.ProductGroupService;
import warehouse.com.productmanagementservice.service.ProductService;
import warehouse.com.productmanagementservice.service.ProductStockService;
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
  private final ProductStockRepository productStockRepository;
  private final ProductStockService productStockService;

  @Override
  public Product findById(Long id) {
    return productRepository.findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, PRODUCT, id)));
  }

  @Override
  public Page<Product> getAll(Pageable pageable) {
    return productRepository.findAllBy(pageable);
  }

  @Override
  public void deleteById(Long id) {
    log.debug("Deleting product with {}", keyValue(ID, id));
    productRepository.deleteById(id);
  }

  @Override
  public Product create(ProductDto productDto) {
    validateCreateRequest(productDto);

    var productGroup = productGroupService.findById(productDto.getProductGroupId());

    var product = productMapper.toEntityFromRequestDto(productDto);
    product.setProductGroup(productGroup);

    var productStocks = productDto.getStockItems()
        .stream()
        .flatMap(productStockDto -> {
          var warehouse = warehouseService.findById(productStockDto.getWarehouseId());
          ProductStock productStock = ProductStock.builder()
              .warehouse(warehouse)
              .product(product)
              .quantity(productStockDto.getQuantity())
              .build();
          return Stream.of(productStock);
        })
        .toList();

    product.setStockItems(productStocks);

    log.debug("Creating ProductStocks with {}",
        keyValue(ID, productStocks.stream()
            .map(ProductStock::getId)
            .toList()));
    productStockRepository.saveAll(productStocks);

    log.debug("Creating Product with {}", keyValue(NAME, product.getProductName()));
    return productRepository.save(product);
  }

  @Override
  public Product update(Long id, ProductDto productDto) {
    var productToUpdate = findById(id);

    validateUpdateRequest(productToUpdate, productDto);

    var updatedProduct = updateProduct(productToUpdate, productDto);

    log.debug("Updating product with {}", keyValue(NAME, updatedProduct.getProductName()));
    return productRepository.save(updatedProduct);
  }

  private void validateCreateRequest(ProductDto requestDto) {
    var productName = requestDto.getProductName();
    if (productRepository.existsByProductName(productName)) {
      throw new EntityExistsException(
          String.format(ENTITY_EXISTS, PRODUCT, PRODUCT_NAME, productName));
    }
  }

  private void validateUpdateRequest(Product productToUpdate, ProductDto requestDto) {
    var productName = requestDto.getProductName();
    if (productRepository.existsByProductName(productName) &&
        !productName.equalsIgnoreCase(productToUpdate.getProductName())) {
      throw new EntityExistsException(
          String.format(ENTITY_EXISTS, PRODUCT, PRODUCT_NAME, productName));
    }
  }

  private Product updateProduct(Product productToUpdate, ProductDto productDto) {
    var productGroup = productGroupService.findById(productDto.getProductGroupId());

    var productStocks = productDto.getStockItems()
        .stream()
        .flatMap(productStockDto -> {
          var productStock = productStockService.findByProductIdAndWarehouseId(
              productStockDto.getProductId(),
              productStockDto.getWarehouseId()
          );
          productStock.setQuantity(productStockDto.getQuantity());
          return Stream.of(productStock);
        })
        .collect(Collectors.toList());

    productMapper.partialUpdate(productDto, productToUpdate);
    productToUpdate.setProductGroup(productGroup);
    productToUpdate.setStockItems(productStocks);

    return productToUpdate;
  }
}
