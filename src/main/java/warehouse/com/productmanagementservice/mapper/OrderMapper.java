package warehouse.com.productmanagementservice.mapper;

import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import warehouse.com.productmanagementservice.model.dto.request.OrderItemDto;
import warehouse.com.productmanagementservice.model.dto.response.OrderResponseDto;
import warehouse.com.productmanagementservice.model.entity.Order;
import warehouse.com.productmanagementservice.model.entity.OrderItem;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface OrderMapper {

  Order toEntity(OrderResponseDto orderResponseDto);

  @Mapping(target = "orderItems", source = "orderItems", qualifiedByName = "mapOrderItems")
  OrderResponseDto toDto(Order order);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Order partialUpdate(
      OrderResponseDto orderResponseDto, @MappingTarget Order order);

  @Named("mapOrderItems")
  default List<OrderItemDto> mapOrderItems(List<OrderItem> orderItems) {
    return orderItems.stream()
        .map(orderItem -> OrderItemDto.builder()
            .productId(orderItem.getProduct().getId())
            .quantity(orderItem.getQuantity())
            .warehouseId(orderItem.getWarehouseId())
            .build())
        .toList();
  }
}