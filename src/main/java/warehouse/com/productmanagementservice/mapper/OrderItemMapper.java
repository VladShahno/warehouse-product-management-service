package warehouse.com.productmanagementservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import warehouse.com.productmanagementservice.model.dto.request.OrderItemDto;
import warehouse.com.productmanagementservice.model.entity.OrderItem;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface OrderItemMapper {

  OrderItem toEntity(OrderItemDto orderItemDto);

  OrderItemDto toDto(OrderItem orderItem);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  OrderItem partialUpdate(
      OrderItemDto orderItemDto, @MappingTarget OrderItem orderItem);
}