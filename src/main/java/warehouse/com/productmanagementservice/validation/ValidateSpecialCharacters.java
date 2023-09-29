package warehouse.com.productmanagementservice.validation;

import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.INVALID_SPECIAL_CHARACTERS_MESSAGE;
import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.SPECIAL_CHARACTER_VALIDATION_PATTERN;

import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "")
@Constraint(validatedBy = {})
public @interface ValidateSpecialCharacters {

  @OverridesAttribute(constraint = Pattern.class, name = "regexp") String regexp() default SPECIAL_CHARACTER_VALIDATION_PATTERN;

  @OverridesAttribute(constraint = Pattern.class, name = "message") String message() default INVALID_SPECIAL_CHARACTERS_MESSAGE;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
