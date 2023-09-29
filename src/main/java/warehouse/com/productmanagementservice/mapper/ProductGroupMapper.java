package warehouse.com.productmanagementservice.mapper;

import java.util.List;
import java.util.Set;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import warehouse.com.productmanagementservice.model.Product;
import warehouse.com.productmanagementservice.model.ProductGroup;
import warehouse.com.productmanagementservice.model.dto.request.CreateProductGroupDto;
import warehouse.com.productmanagementservice.model.dto.request.ProductGroupDto;
import warehouse.com.productmanagementservice.model.dto.response.ProductGroupFullResponseDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface ProductGroupMapper {

  ProductGroup toEntity(CreateProductGroupDto productGroupDto);

  ProductGroupDto toDto(ProductGroup productGroup);

  @Mapping(target = "productNames", source = "products", qualifiedByName = "mapProductNames")
  ProductGroupFullResponseDto toProductGroupFullResponseDto(ProductGroup productGroup);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  ProductGroup partialUpdate(
      ProductGroupDto productGroupDto, @MappingTarget ProductGroup productGroup);

  @Named("mapProductNames")
  default List<String> mapStockItemIds(Set<Product> products) {
    return products.stream()
        .map(Product::getProductName).toList();
  }
}