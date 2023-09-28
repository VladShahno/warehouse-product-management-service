package warehouse.com.productmanagementservice.model.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductDto implements Serializable {

  private final String productName;
  private final int amountOfReserved;
  private final BigDecimal purchasePrice;
  private final BigDecimal salePrice;
  private final Long productGroupId;
  private final List<Long> warehouseIds;
  private final List<ProductStockDto> stockItems;
  private final String article;
}