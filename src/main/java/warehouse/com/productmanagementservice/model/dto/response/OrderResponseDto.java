package warehouse.com.productmanagementservice.model.dto.response;

import java.util.List;
import lombok.Data;
import warehouse.com.productmanagementservice.model.dto.request.OrderItemDto;
import warehouse.com.productmanagementservice.model.order.OrderStatus;

@Data
public class OrderResponseDto {

  private Long id;
  private List<OrderItemDto> orderItems;
  private OrderStatus status;
}
