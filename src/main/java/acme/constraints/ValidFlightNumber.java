
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
@Pattern(regexp = "^[A-Z]{3}\\d{4}$")

public @interface ValidFlightNumber {

	String message() default "{acme.validation.leg.flight-number.message}";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
