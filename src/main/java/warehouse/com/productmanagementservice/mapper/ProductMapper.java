package warehouse.com.productmanagementservice.mapper;

import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import warehouse.com.productmanagementservice.model.dto.request.ProductDto;
import warehouse.com.productmanagementservice.model.dto.response.ProductResponseDto;
import warehouse.com.productmanagementservice.model.dto.response.ProductStockResponseDto;
import warehouse.com.productmanagementservice.model.entity.Product;
import warehouse.com.productmanagementservice.model.entity.ProductStock;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface ProductMapper {

  Product toEntity(ProductResponseDto productResponseDto);

  Product toEntityFromRequestDto(ProductDto productDto);

  @AfterMapping
  default void linkStockItems(@MappingTarget Product product) {
    product.getStockItems().forEach(stockItem -> stockItem.setProduct(product));
  }

  @Mapping(target = "stockItems", source = "stockItems", qualifiedByName = "mapStockItemIds")
  ProductResponseDto toDto(Product product);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void partialUpdate(
      ProductDto productDto, @MappingTarget Product product);

  @Named("mapStockItemIds")
  default List<ProductStockResponseDto> mapStockItemIds(List<ProductStock> stockItems) {
    return stockItems.stream()
        .map(productStock -> ProductStockResponseDto.builder()
            .productName(productStock.getProduct().getProductName())
            .quantity(productStock.getQuantity())
            .warehouseName(productStock.getWarehouse().getWarehouseName())
            .build())
        .toList();
  }
}