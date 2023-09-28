package warehouse.com.productmanagementservice.model.dto.request;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductStockDto implements Serializable {

  private final Long productId;
  private final Long warehouseId;
  private final int quantity;
}