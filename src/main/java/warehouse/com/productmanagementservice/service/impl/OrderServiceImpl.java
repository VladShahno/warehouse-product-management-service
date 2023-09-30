package warehouse.com.productmanagementservice.service.impl;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static warehouse.com.productmanagementservice.common.Constants.Logging.ID;
import static warehouse.com.productmanagementservice.common.Constants.Logging.ORDER_STATUS;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.ENTITY_NOT_FOUND;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.ORDER;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.PRODUCT;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import warehouse.com.productmanagementservice.exception.InsufficientStockException;
import warehouse.com.productmanagementservice.mapper.OrderItemMapper;
import warehouse.com.productmanagementservice.model.dto.request.OrderItemDto;
import warehouse.com.productmanagementservice.model.entity.Order;
import warehouse.com.productmanagementservice.model.entity.OrderItem;
import warehouse.com.productmanagementservice.model.entity.ProductStock;
import warehouse.com.productmanagementservice.model.order.OrderStatus;
import warehouse.com.productmanagementservice.repository.OrderRepository;
import warehouse.com.productmanagementservice.repository.ProductStockRepository;
import warehouse.com.productmanagementservice.service.OrderService;
import warehouse.com.productmanagementservice.service.ProductStockService;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final ProductStockService productStockService;
  private final ProductStockRepository productStockRepository;
  private final OrderItemMapper orderItemMapper;

  @Override
  public Order createOrder(List<OrderItemDto> orderItemDtos) {
    List<ProductStock> productStocksToUpdate = new ArrayList<>();
    List<OrderItem> items = new ArrayList<>();

    orderItemDtos.forEach(orderItemDto -> {
      var productId = orderItemDto.getProductId();
      var requestedQuantity = orderItemDto.getQuantity();

      var productStock = productStockService.findByProductIdAndWarehouseId(productId,
          orderItemDto.getWarehouseId());

      if (Objects.isNull(productStock) || productStock.getQuantity() < requestedQuantity) {
        throw new InsufficientStockException(
            String.format("Not enough stock available for productId: %s", productId));
      }

      var orderItem = orderItemMapper.toEntity(orderItemDto);
      orderItem.setProduct(productStock.getProduct());
      items.add(orderItem);

      productStock.setQuantity(productStock.getQuantity() - requestedQuantity);

      productStocksToUpdate.add(productStock);
    });
    productStockRepository.saveAll(productStocksToUpdate);

    var order = Order.builder()
        .orderItems(items)
        .status(OrderStatus.RESERVED)
        .created(new Date())
        .build();

    items.forEach(orderItem -> orderItem.setOrder(order));

    var savedOrder = orderRepository.save(order);

    log.info("Creating Order with {}", kv(ID, savedOrder.getId()));
    return savedOrder;
  }

  @Override
  public void cancelExpiredOrders() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime oneDayAgo = now.minusDays(1);

    List<Order> expiredOrders = orderRepository.findAllByStatusAndCreatedBefore(
        OrderStatus.RESERVED, Date.from(oneDayAgo.atZone(ZoneId.systemDefault()).toInstant())
    );

    expiredOrders.forEach(order -> {
      order.getOrderItems().forEach(orderItem -> {
        var product = orderItem.getProduct();
        var reservedQuantity = orderItem.getQuantity();
        var productStock = productStockService.findByProductIdAndWarehouseId(
            product.getId(), orderItem.getWarehouseId()
        );

        if (Objects.nonNull(productStock)) {
          productStock.setQuantity(productStock.getQuantity() + reservedQuantity);
          log.info(
              "Updating Product Stock with {} amd return {} to the warehouse with {}",
              kv(ID, productStock.getId()),
              kv(PRODUCT, reservedQuantity),
              kv(ID, orderItem.getWarehouseId()));
          productStockRepository.save(productStock);
        }
      });
      order.setStatus(OrderStatus.CANCELLED);
      log.info("Updating expired Order with {} ", kv(ID, order.getId()));
    });
    orderRepository.saveAll(expiredOrders);
  }

  @Override
  public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
    var orderToUpdate = findOrderById(orderId);

    if (OrderStatus.CANCELLED.equals(orderStatus)) {
      orderToUpdate.getOrderItems().forEach(orderItem -> {
        var product = orderItem.getProduct();
        var reservedQuantity = orderItem.getQuantity();
        var productStock = productStockService.findByProductIdAndWarehouseId(
            product.getId(), orderItem.getWarehouseId()
        );

        if (Objects.nonNull(productStock)) {
          productStock.setQuantity(productStock.getQuantity() + reservedQuantity);
          log.info(
              "Updating Product Stock with {} amd return {} to the warehouse with {}",
              kv(ID, productStock.getId()),
              kv(PRODUCT, reservedQuantity),
              kv(ID, orderItem.getWarehouseId()));
          productStockRepository.save(productStock);
        }
      });
      orderToUpdate.setStatus(OrderStatus.CANCELLED);
      log.info("Updating Order to {} with {} ", kv(ORDER_STATUS, orderStatus),
          kv(ID, orderToUpdate.getId()));

    }

    log.info("Updating Order to {} with {} ", kv(ORDER_STATUS, orderStatus),
        kv(ID, orderToUpdate.getId()));
    return orderRepository.save(orderToUpdate);
  }

  @Override
  public Order findOrderById(Long orderId) {
    return orderRepository.findById(orderId).orElseThrow(
        () -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, ORDER, orderId)));
  }

  @Override
  public Page<Order> getAllOrders(Pageable pageable) {
    return orderRepository.findAll(pageable);
  }
}
