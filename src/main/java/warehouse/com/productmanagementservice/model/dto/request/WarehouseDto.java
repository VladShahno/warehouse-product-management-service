package warehouse.com.productmanagementservice.model.dto.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WarehouseDto implements Serializable {

  private final Long id;
  private final String warehouseName;
}