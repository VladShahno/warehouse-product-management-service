package warehouse.com.productmanagementservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import warehouse.com.productmanagementservice.model.ProductGroup;
import warehouse.com.productmanagementservice.model.dto.request.ProductGroupDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface ProductGroupMapper {

  ProductGroup toEntity(ProductGroupDto productGroupDto);

  ProductGroupDto toDto(ProductGroup productGroup);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  ProductGroup partialUpdate(
      ProductGroupDto productGroupDto, @MappingTarget ProductGroup productGroup);
}