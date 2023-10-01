package warehouse.com.productmanagementservice.exception;


import warehouse.com.reststarter.exception.CustomRuntimeException;

public class InsufficientStockException extends CustomRuntimeException {

  public InsufficientStockException(String message) {
    super(message);
  }

  public InsufficientStockException(String message, Throwable cause) {
    super(message, cause);
  }
}