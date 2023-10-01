package warehouse.com.productmanagementservice.controller;

import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.ID_IS_REQUIRED;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import warehouse.com.productmanagementservice.mapper.OrderMapper;
import warehouse.com.productmanagementservice.model.dto.request.OrderItemDto;
import warehouse.com.productmanagementservice.model.dto.response.OrderResponseDto;
import warehouse.com.productmanagementservice.model.dto.response.ProductResponseDto;
import warehouse.com.productmanagementservice.model.order.OrderStatus;
import warehouse.com.productmanagementservice.service.OrderService;

@RestController
@AllArgsConstructor
@Validated
@Tag(name = "Order Controller", description = "Provides general operation with order")
@RequestMapping(path = "/v1/orders")
public class OrderController {

  private final OrderService orderService;
  private final OrderMapper orderMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Endpoint allows to create order")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successfully created order")
  })
  public OrderResponseDto createOrder(@RequestBody @Valid List<OrderItemDto> orderItems) {
    return orderMapper.toDto(orderService.createOrder(orderItems));
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Endpoint allows to get order by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Order received successfully")
  })
  public OrderResponseDto getOrderById(
      @Parameter(description = "Target order id", example = "5")
      @NotNull(message = ID_IS_REQUIRED) @PathVariable Long id) {
    return orderMapper.toDto(orderService.findOrderById(id));
  }

  @PatchMapping("/edit/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Endpoint allows to edit order")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully updated order")
  })
  public OrderResponseDto updateOrder(
      @Parameter(description = "Target order id", example = "6")
      @NotNull(message = ID_IS_REQUIRED) @PathVariable Long id,
      @RequestBody @Valid OrderStatus orderStatus) {
    return orderMapper.toDto(orderService.updateOrderStatus(id, orderStatus));
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Endpoint allows to get all orders")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Orders received successfully",
          content = {@Content(schema = @Schema(implementation = ProductResponseDto.class))}),
  })
  public Page<OrderResponseDto> findAllProducts(
      @PageableDefault(size = 25, sort = {"id"}) Pageable pageable) {
    return orderService.getAllOrders(pageable).map(orderMapper::toDto);
  }
}
