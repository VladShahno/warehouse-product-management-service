package warehouse.com.productmanagementservice.exception;

public class InsufficientStockException extends RuntimeException {

  public InsufficientStockException(String message) {
    super(message);
  }
}