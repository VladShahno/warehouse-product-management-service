package warehouse.com.productmanagementservice.service.impl;

import static warehouse.com.productmanagementservice.common.Constants.Logging.ENTITY_NOT_FOUND;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import warehouse.com.productmanagementservice.model.Warehouse;
import warehouse.com.productmanagementservice.model.dto.request.WarehouseDto;
import warehouse.com.productmanagementservice.repository.WarehouseRepository;
import warehouse.com.productmanagementservice.service.WarehouseService;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class WarehouseServiceImpl implements WarehouseService {

  private final WarehouseRepository warehouseRepository;

  @Override
  public Warehouse create(WarehouseDto dto) {
    return null;
  }

  @Override
  public Warehouse update(Long id, WarehouseDto dto) {
    return null;
  }

  @Override
  public Warehouse findById(Long id) {
    return warehouseRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, id)));
  }

  @Override
  public Page<Warehouse> getAll(Pageable pageable) {
    return warehouseRepository.findAll(pageable);
  }

  @Override
  public void deleteById(Long id) {
    warehouseRepository.deleteById(id);
  }
}
