
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nullable;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation

@Nullable
@Pattern(regexp = "^[A-Z]{4}-[0-9]{2}$")

public @interface ValidPromotionCode {

	String message() default "{acme.validation.service.promotioncode.message}";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
