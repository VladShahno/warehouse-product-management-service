package warehouse.com.productmanagementservice.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDto {
  private Long productId;
  private Long warehouseId;
  private Integer quantity;
}