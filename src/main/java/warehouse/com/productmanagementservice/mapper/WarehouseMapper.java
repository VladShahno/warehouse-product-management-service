package warehouse.com.productmanagementservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import warehouse.com.productmanagementservice.model.entity.Warehouse;
import warehouse.com.productmanagementservice.model.dto.request.WarehouseDto;
import warehouse.com.productmanagementservice.model.dto.response.WarehouseResponseDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface WarehouseMapper {

  Warehouse toEntity(WarehouseDto warehouseDto);

  WarehouseResponseDto toWarehouseResponseDto(Warehouse warehouse);

  WarehouseDto toDto(Warehouse warehouse);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Warehouse partialUpdate(
      WarehouseDto warehouseDto, @MappingTarget Warehouse warehouse);
}