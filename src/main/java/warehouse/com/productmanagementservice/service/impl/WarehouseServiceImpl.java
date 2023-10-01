package warehouse.com.productmanagementservice.service.impl;

import static net.logstash.logback.argument.StructuredArguments.keyValue;
import static warehouse.com.productmanagementservice.common.Constants.Logging.ID;
import static warehouse.com.productmanagementservice.common.Constants.Logging.NAME;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.ENTITY_NOT_FOUND;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.WAREHOUSE;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import warehouse.com.productmanagementservice.mapper.WarehouseMapper;
import warehouse.com.productmanagementservice.model.dto.request.WarehouseDto;
import warehouse.com.productmanagementservice.model.entity.Warehouse;
import warehouse.com.productmanagementservice.repository.WarehouseRepository;
import warehouse.com.productmanagementservice.service.WarehouseService;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class WarehouseServiceImpl implements WarehouseService {

  private final WarehouseRepository warehouseRepository;
  private final WarehouseMapper warehouseMapper;

  @Override
  public Warehouse create(WarehouseDto warehouseDto) {
    var warehouse = warehouseMapper.toEntity(warehouseDto);

    log.debug("Creating warehouse with {}", keyValue(NAME, warehouseDto.warehouseName()));
    return warehouseRepository.save(warehouse);
  }

  @Override
  public Warehouse update(Long id, WarehouseDto warehouseDto) {
    var warehouse = findById(id);
    warehouse.setWarehouseName(warehouseDto.warehouseName());

    log.debug("Updating warehouse with {}", keyValue(NAME, warehouseDto.warehouseName()));
    return warehouseRepository.save(warehouse);
  }

  @Override
  public Warehouse findById(Long id) {
    return warehouseRepository.findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, WAREHOUSE, id)));
  }

  @Override
  public Page<Warehouse> getAll(Pageable pageable) {
    return warehouseRepository.findAll(pageable);
  }

  @Override
  public void deleteById(Long id) {
    log.debug("Deleting warehouse with {}", keyValue(ID, id));
    warehouseRepository.deleteById(id);
  }
}
