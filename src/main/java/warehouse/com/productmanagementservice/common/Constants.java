package warehouse.com.productmanagementservice.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

  @UtilityClass
  public class Logging {

    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String ORDER_STATUS = "orderStatus";
  }

  @UtilityClass
  public class ProductManagementValidation {

    public static final String ID_IS_REQUIRED = "{product.management.id.required}";
    public static final String ENTITY_NOT_FOUND = "There is no %s with the passed id: %s";
    public static final String ENTITY_EXISTS = "%s with %s: %s already exists";
    public static final String NOT_UPDATABLE_ORDER = "Already canceled or completed orders with id: %s cannot be updated";
    public static final String PRODUCT = "Product";
    public static final String ORDER = "Order";
    public static final String PRODUCT_GROUP = "Product Group";
    public static final String WAREHOUSE = "Warehouse";
    public static final String PRODUCT_NAME = "productName";
    public static final String PRODUCT_GROUP_NAME = "productGroupName";
  }
}
