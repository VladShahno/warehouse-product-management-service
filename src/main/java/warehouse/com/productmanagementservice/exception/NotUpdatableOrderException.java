package warehouse.com.productmanagementservice.exception;


import warehouse.com.reststarter.exception.CustomRuntimeException;

public class NotUpdatableOrderException extends CustomRuntimeException {

  public NotUpdatableOrderException(String message) {
    super(message);
  }
}
