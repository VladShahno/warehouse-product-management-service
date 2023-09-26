package warehouse.com.productmanagementservice.exception;

public class EntityNotFoundException extends RuntimeException{
  public EntityNotFoundException() {
    super("The entity with the specified id was not found");
  }
}
