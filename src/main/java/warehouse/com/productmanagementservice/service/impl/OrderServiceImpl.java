package warehouse.com.productmanagementservice.service.impl;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static warehouse.com.audit.starter.common.Constants.CREATED;
import static warehouse.com.audit.starter.common.Constants.UPDATED;
import static warehouse.com.productmanagementservice.common.Constants.Logging.ID;
import static warehouse.com.productmanagementservice.common.Constants.Logging.ORDER_STATUS;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.ENTITY_NOT_FOUND;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.NOT_UPDATABLE_ORDER;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.ORDER;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.PRODUCT;
import static warehouse.com.productmanagementservice.model.order.OrderStatus.CANCELLED;
import static warehouse.com.productmanagementservice.model.order.OrderStatus.COMPLETED;
import static warehouse.com.productmanagementservice.model.order.OrderStatus.RESERVED;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import warehouse.com.audit.starter.service.AuditService;
import warehouse.com.productmanagementservice.exception.InsufficientStockException;
import warehouse.com.productmanagementservice.exception.NotUpdatableOrderException;
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
import warehouse.com.reststarter.exception.NotFoundException;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final ProductStockService productStockService;
  private final ProductStockRepository productStockRepository;
  private final OrderItemMapper orderItemMapper;
  private final AuditService auditService;

  @Override
  public Order createOrder(List<OrderItemDto> orderItemDtos) {
    List<OrderItem> orderItems = new ArrayList<>();

    Map<Long, Integer> productIdToQuantity = new HashMap<>();
    Map<Long, Long> productIdToWarehouseId = new HashMap<>();

    orderItemDtos.forEach(orderItemDto -> {
      var productId = orderItemDto.getProductId();
      var requestedQuantity = orderItemDto.getQuantity();
      productIdToQuantity.put(productId, requestedQuantity);
      productIdToWarehouseId.put(productId, orderItemDto.getWarehouseId());
    });

    List<ProductStock> productStocks = productStockService.findAllByProductIdAndWarehouseId(
        productIdToQuantity.keySet(),
        new ArrayList<>(productIdToWarehouseId.values())
    );

    // Verify stock availability and build order orderItems
    productStocks.forEach(productStock -> {
      var productId = productStock.getProduct().getId();
      var requestedQuantity = productIdToQuantity.get(productId);
      var warehouseId = productIdToWarehouseId.get(productId);

      if (productStock.getQuantity() < requestedQuantity) {
        throw new InsufficientStockException(
            String.format("Not enough stock available for productId: %s", productId));
      }

      var orderItem = orderItemMapper.toEntity(
          new OrderItemDto(productId, warehouseId, requestedQuantity));
      orderItem.setProduct(productStock.getProduct());
      orderItems.add(orderItem);

      productStock.setQuantity(productStock.getQuantity() - requestedQuantity);
    });
    productStockRepository.saveAll(productStocks);

    var order = Order.builder()
        .orderItems(orderItems)
        .status(RESERVED)
        .created(new Date())
        .build();

    orderItems.forEach(orderItem -> orderItem.setOrder(order));
    var savedOrder = orderRepository.save(order);

    auditService.sendAuditEvent(savedOrder, CREATED, "Vlad", "Order successfully created");

    log.info("Creating Order with {}", kv(ID, savedOrder.getId()));
    return savedOrder;
  }

  @Override
  public void cancelExpiredOrders() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime oneDayAgo = now.minusDays(1);

    List<Order> expiredOrders = orderRepository.findAllByStatusAndCreatedBefore(
        RESERVED, Date.from(oneDayAgo.atZone(ZoneId.systemDefault()).toInstant())
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
      order.setStatus(CANCELLED);
      log.info("Updating expired Order with {} ", kv(ID, order.getId()));
    });
    orderRepository.saveAll(expiredOrders);
  }

  @Override
  public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
    var orderToUpdate = findOrderById(orderId);

    var existingOrderStatus = orderToUpdate.getStatus();

    if (CANCELLED.equals(existingOrderStatus) || COMPLETED.equals(existingOrderStatus)) {
      throw new NotUpdatableOrderException(
          String.format(NOT_UPDATABLE_ORDER, orderToUpdate.getId()));
    }

    if (CANCELLED.equals(orderStatus)) {
      List<Long> productIds = orderToUpdate.getOrderItems().stream()
          .map(orderItem -> orderItem.getProduct().getId())
          .toList();

      List<Long> warehouseIds = orderToUpdate.getOrderItems().stream()
          .map(OrderItem::getWarehouseId)
          .toList();

      List<ProductStock> productStocks = productStockService.findAllByProductIdAndWarehouseId(
          new HashSet<>(productIds), warehouseIds
      );

      productStocks.forEach(productStock -> {
        var reservedQuantity = orderToUpdate.getOrderItems().stream()
            .filter(orderItem -> orderItem.getProduct().getId()
                .equals(productStock.getProduct().getId()))
            .mapToInt(OrderItem::getQuantity)
            .sum();

        log.info(
            "Updating Product Stocks with {} and return {} to the warehouse with {}",
            kv(ID, productStock.getId()),
            kv(PRODUCT, reservedQuantity),
            kv(ID, productStock.getWarehouse().getId()));
        productStock.setQuantity(productStock.getQuantity() + reservedQuantity);
      });
      productStockRepository.saveAll(productStocks);
    }
    orderToUpdate.setStatus(orderStatus);
    orderRepository.save(orderToUpdate);

    auditService.sendAuditEvent(orderToUpdate, UPDATED, "Vlad", "Order successfully updated");

    log.info("Updating Order with {} to {}", kv(ID, orderToUpdate.getId()),
        kv(ORDER_STATUS, orderStatus));
    return orderToUpdate;
  }

  @Override
  public Order findOrderById(Long orderId) {
    return orderRepository.findById(orderId).orElseThrow(
        () -> new NotFoundException(String.format(ENTITY_NOT_FOUND, ORDER, orderId)));
  }

  @Override
  public Page<Order> getAllOrders(Pageable pageable) {
    return orderRepository.findAll(pageable);
  }
}
