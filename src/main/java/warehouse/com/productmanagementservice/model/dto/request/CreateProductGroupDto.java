package warehouse.com.productmanagementservice.model.dto.request;

import java.util.List;

public record CreateProductGroupDto(String productGroupName, List<Long> productIds) {

}
