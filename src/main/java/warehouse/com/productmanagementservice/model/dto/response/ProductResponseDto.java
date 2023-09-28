package warehouse.com.productmanagementservice.model.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponseDto(String productName,
                                 int amountOfReserved,
                                 BigDecimal purchasePrice,
                                 BigDecimal salePrice,
                                 ProductGroupResponseDto productGroup,
                                 List<WarehouseResponseDto> warehouses,
                                 List<ProductStockResponseDto> stockItems,
                                 String article) {

}

