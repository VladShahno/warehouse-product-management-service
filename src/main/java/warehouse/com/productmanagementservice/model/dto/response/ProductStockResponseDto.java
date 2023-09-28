package warehouse.com.productmanagementservice.model.dto.response;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductStockResponseDto implements Serializable {

  private final String productName;
  private final String warehouseName;
  private final int quantity;
}