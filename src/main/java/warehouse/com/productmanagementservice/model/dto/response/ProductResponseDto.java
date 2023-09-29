package warehouse.com.productmanagementservice.model.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponseDto(String productName,
                                 Integer amountOfReserved,
                                 BigDecimal purchasePrice,
                                 BigDecimal salePrice,
                                 ProductGroupResponseDto productGroup,
                                 List<ProductStockResponseDto> stockItems,
                                 String article) {

}

