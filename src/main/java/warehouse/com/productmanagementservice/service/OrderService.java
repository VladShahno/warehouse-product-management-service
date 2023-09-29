package warehouse.com.productmanagementservice.service;

import java.util.List;
import warehouse.com.productmanagementservice.model.dto.request.OrderItemDto;
import warehouse.com.productmanagementservice.model.entity.Order;

public interface OrderService {

  Order createOrder(List<OrderItemDto> orderItemDtos);

  public void cancelExpiredOrders();
}
