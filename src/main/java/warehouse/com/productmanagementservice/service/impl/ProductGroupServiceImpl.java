package warehouse.com.productmanagementservice.service.impl;


import static net.logstash.logback.argument.StructuredArguments.keyValue;
import static warehouse.com.productmanagementservice.common.Constants.Logging.ID;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.ENTITY_EXISTS;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.ENTITY_NOT_FOUND;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.PRODUCT_GROUP;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.PRODUCT_GROUP_NAME;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import warehouse.com.productmanagementservice.mapper.ProductGroupMapper;
import warehouse.com.productmanagementservice.model.dto.request.CreateProductGroupDto;
import warehouse.com.productmanagementservice.model.entity.Product;
import warehouse.com.productmanagementservice.model.entity.ProductGroup;
import warehouse.com.productmanagementservice.repository.ProductGroupRepository;
import warehouse.com.productmanagementservice.repository.ProductRepository;
import warehouse.com.productmanagementservice.service.ProductGroupService;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ProductGroupServiceImpl implements ProductGroupService {

  private final ProductGroupRepository productGroupRepository;
  private final ProductGroupMapper productGroupMapper;
  private final ProductRepository productRepository;

  @Override
  public ProductGroup create(CreateProductGroupDto requestDto) {
    var productGroupName = requestDto.productGroupName();
    if (productGroupRepository.existsByProductGroupName(productGroupName)) {
      throw new EntityExistsException(
          String.format(ENTITY_EXISTS, PRODUCT_GROUP, PRODUCT_GROUP_NAME, productGroupName));
    }

    var products = new HashSet<>(productRepository.findAllById(requestDto.productIds()));

    var productGroup = productGroupMapper.toEntity(requestDto);
    updateRelationBetweenProductAndProductGroup(products, productGroup);

    log.debug("Creating product group with {}", keyValue(PRODUCT_GROUP_NAME, productGroupName));
    return productGroupRepository.save(productGroup);
  }

  @Override
  public ProductGroup update(Long id, CreateProductGroupDto requestDto) {
    var updatedProductGroup = updateProductGroup(findById(id), requestDto);

    log.debug("Updating product group with {}",
        keyValue(PRODUCT_GROUP_NAME, updatedProductGroup.getProductGroupName()));
    productGroupRepository.save(updatedProductGroup);

    return updatedProductGroup;
  }

  @Override
  public ProductGroup findById(Long id) {
    return productGroupRepository.findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, PRODUCT_GROUP, id)));
  }

  @Override
  public Page<ProductGroup> getAll(Pageable pageable) {
    return productGroupRepository.findAll(pageable);
  }

  @Override
  public void deleteById(Long id) {
    log.debug("Deleting product group with {}", keyValue(ID, id));
    productGroupRepository.deleteById(id);
  }

  private ProductGroup updateProductGroup(ProductGroup productGroupToUpdate,
      CreateProductGroupDto requestDto) {
    validateUpdateRequest(productGroupToUpdate, requestDto);

    var products = new HashSet<>(productRepository.findAllById(requestDto.productIds()));
    updateRelationBetweenProductAndProductGroup(products, productGroupToUpdate);

    productGroupToUpdate.setProductGroupName(requestDto.productGroupName());

    return productGroupToUpdate;
  }

  private void validateUpdateRequest(ProductGroup productGroupToUpdate,
      CreateProductGroupDto requestDto) {
    var productGroupName = requestDto.productGroupName();
    if (productGroupRepository.existsByProductGroupName(productGroupName) &&
        !productGroupName.equalsIgnoreCase(productGroupToUpdate.getProductGroupName())) {
      throw new EntityExistsException(
          String.format(ENTITY_EXISTS, PRODUCT_GROUP, PRODUCT_GROUP_NAME,
              productGroupName));
    }
  }

  private void updateRelationBetweenProductAndProductGroup(HashSet<Product> products,
      ProductGroup productGroup) {
    if (CollectionUtils.isNotEmpty(products)) {
      products.forEach(product -> product.setProductGroup(productGroup));
      productGroup.setProducts(products);
    }
  }
}