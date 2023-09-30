package warehouse.com.productmanagementservice.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import warehouse.com.productmanagementservice.model.dto.request.OrderItemDto;
import warehouse.com.productmanagementservice.model.entity.Order;
import warehouse.com.productmanagementservice.model.order.OrderStatus;

public interface OrderService {

  Order createOrder(List<OrderItemDto> orderItemDtos);

  void cancelExpiredOrders();

  Order updateOrderStatus(Long orderId, OrderStatus orderStatus);

  Order findOrderById(Long id);

  Page<Order> getAllOrders(Pageable pageable);
}
