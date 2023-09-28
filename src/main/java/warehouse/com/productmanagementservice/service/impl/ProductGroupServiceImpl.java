package warehouse.com.productmanagementservice.service.impl;


import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.ENTITY_NOT_FOUND;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import warehouse.com.productmanagementservice.model.ProductGroup;
import warehouse.com.productmanagementservice.model.dto.request.ProductGroupDto;
import warehouse.com.productmanagementservice.repository.ProductGroupRepository;
import warehouse.com.productmanagementservice.service.ProductGroupService;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ProductGroupServiceImpl implements ProductGroupService {

  private final ProductGroupRepository productGroupRepository;

  @Override
  public ProductGroup create(ProductGroupDto dto) {
    return null;
  }

  @Override
  public ProductGroup update(Long id, ProductGroupDto dto) {
    return null;
  }

  @Override
  public ProductGroup findById(Long id) {
    return productGroupRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, id)));
  }

  @Override
  public Page<ProductGroup> getAll(Pageable pageable) {
    return null;
  }

  @Override
  public void deleteById(Long id) {
    productGroupRepository.deleteById(id);
  }
}