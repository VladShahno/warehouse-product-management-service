package warehouse.com.productmanagementservice.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import warehouse.com.productmanagementservice.model.entity.Order;
import warehouse.com.productmanagementservice.model.order.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findAllByStatusAndCreatedBefore(OrderStatus orderStatus, Date date);
}
