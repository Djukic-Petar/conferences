package validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import org.primefaces.validate.bean.ClientConstraint;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
@ClientConstraint(resolvedBy = PasswordClientValidationConstraint.class)
public @interface Password {

    String message() default "Your password must have between 8 and 12 characters, At least one capital letter, three lowercase letters, a number and a special character. It must start"
            + "with a letter or a number, and it cannot have more than two consecutive characters.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}