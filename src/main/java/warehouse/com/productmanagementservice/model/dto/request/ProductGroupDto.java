package warehouse.com.productmanagementservice.model.dto.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductGroupDto implements Serializable {

  private final Long id;
}