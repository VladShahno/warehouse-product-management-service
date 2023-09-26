package warehouse.com.productmanagementservice.model.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for {@link warehouse.com.productmanagementservice.model.Product}
 */
@AllArgsConstructor
@Getter
public class ProductDto implements Serializable {

  private final Long id;
  private final String productName;
  private final int amountOfReserved;
  private final BigDecimal purchasePrice;
  private final BigDecimal salePrice;
  private final ProductGroupDto productGroup;
  private final List<WarehouseDto> warehouses;
  private final List<ProductStockDto> stockItems;
  private final String article;
}