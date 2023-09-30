package warehouse.com.productmanagementservice.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OrderItemDto {

  private Long productId;
  private Long warehouseId;
  private Integer quantity;
}