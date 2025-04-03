
package acme.constraints;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation

@NotBlank
@Pattern(regexp = "\\d{4}$") // It only validates the numeric part

public @interface ValidFlightNumber {

	String message() default "{acme.validation.leg.flight-number.message}";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
