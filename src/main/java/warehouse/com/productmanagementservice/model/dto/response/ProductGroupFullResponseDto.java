package warehouse.com.productmanagementservice.model.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class ProductGroupFullResponseDto {

  private Long id;
  private String productGroupName;
  private List<String> productNames;

}
