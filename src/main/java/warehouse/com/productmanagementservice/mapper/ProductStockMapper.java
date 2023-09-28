package warehouse.com.productmanagementservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import warehouse.com.productmanagementservice.model.ProductStock;
import warehouse.com.productmanagementservice.model.dto.request.ProductStockDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface ProductStockMapper {

  ProductStock toEntity(ProductStockDto productStockDto);

  ProductStockDto toDto(ProductStock productStock);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  ProductStock partialUpdate(
      ProductStockDto productStockDto, @MappingTarget ProductStock productStock);
}