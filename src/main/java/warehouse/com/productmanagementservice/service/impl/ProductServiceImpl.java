package warehouse.com.productmanagementservice.service.impl;


import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.ENTITY_EXISTS;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.ENTITY_NOT_FOUND;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.PRODUCT;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.PRODUCT_NAME;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.stream.Collectors;
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

  @Override
  public Product create(ProductDto productDto) {
    validateCreateRequest(productDto);

    var productGroup = productGroupService.findById(productDto.getProductGroupId());

    var warehouses = productDto.getWarehouseIds()
        .stream()
        .map(warehouseRepository::findById)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();

    var product = productMapper.toEntityFromRequestDto(productDto);
    product.setProductGroup(productGroup);
    product.setWarehouses(warehouses);

    var productStocks = productDto.getStockItems().stream()
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
    var productToUpdate = findById(id);

    validateUpdateRequest(productToUpdate, productDto);

    var updatedProduct = updateProduct(productToUpdate, productDto);

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

    var warehouses = productDto.getWarehouseIds()
        .stream()
        .map(warehouseRepository::findById)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());

    var productStocks = productDto.getStockItems()
        .stream()
        .flatMap(productStockDto -> productStockRepository.findByProduct_IdAndWarehouse_Id(
                productStockDto.getProductId(),
                productStockDto.getWarehouseId())
            .map(productStock -> {
              productStock.setQuantity(productStockDto.getQuantity());
              return productStock;
            })
            .stream())
        .collect(Collectors.toList());

    productToUpdate.setProductGroup(productGroup);
    productToUpdate.setAmountOfReserved(productDto.getAmountOfReserved());
    productToUpdate.setWarehouses(warehouses);
    productToUpdate.setStockItems(productStocks);
    productToUpdate.setProductName(productDto.getProductName());
    productToUpdate.setPurchasePrice(productDto.getPurchasePrice());
    productToUpdate.setSalePrice(productDto.getSalePrice());
    productToUpdate.setArticle(productDto.getArticle());

    return productToUpdate;
  }
}
