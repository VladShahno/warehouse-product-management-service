package warehouse.com.productmanagementservice.model.dto.request;

import lombok.Data;

@Data
public class OrderItemDto {

  private Long id;
  private Long productId;
  private Long warehouseId;
  private Integer quantity;
}
