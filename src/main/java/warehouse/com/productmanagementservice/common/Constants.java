package warehouse.com.productmanagementservice.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

  @UtilityClass
  public class Logging {
    public static final String NAME = "name";
  }

  @UtilityClass
  public class ProductManagementValidation {
    public static final String INVALID_SPECIAL_CHARACTERS_MESSAGE = "Invalid %s. Special characters are not allowed";
    public static final String SPECIAL_CHARACTER_VALIDATION_PATTERN = "[^?^\"'@#*$%&()}{\\[\\]|\\\\/~+=<>]*$";
    public static final String PRODUCT_ID_IS_REQUIRED = "{product.management.id.required}";
    public static final String PRODUCT_GROUP_NAME_IS_REQUIRED = "{warehouse.product.group.name.required}";
    public static final String WAREHOUSE_NAME_IS_REQUIRED = "{warehouse.name.required}";
    public static final String ENTITY_NOT_FOUND = "There is no entity with the passed id: %s";
    public static final String ENTITY_EXISTS = "%s with %s: %s already exists";
    public static final String PRODUCT = "Product";
    public static final String PRODUCT_NAME = "productName";


  }

}
