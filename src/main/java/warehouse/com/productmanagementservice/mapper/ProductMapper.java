package warehouse.com.productmanagementservice.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import warehouse.com.productmanagementservice.model.Product;
import warehouse.com.productmanagementservice.model.dto.request.ProductDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface ProductMapper {

  Product toEntity(ProductDto productDto);

  @AfterMapping
  default void linkStockItems(@MappingTarget Product product) {
    product.getStockItems().forEach(stockItem -> stockItem.setProduct(product));
  }

  ProductDto toDto(Product product);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Product partialUpdate(
      ProductDto productDto, @MappingTarget Product product);
}