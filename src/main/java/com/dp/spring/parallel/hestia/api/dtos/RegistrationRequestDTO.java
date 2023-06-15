package com.dp.spring.parallel.hestia.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;

@Jacksonized
@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequestDTO {
    @NotBlank @Length(min = 1, max = 30)
    String firstName;
    @NotBlank @Length(min = 1, max = 30)
    String lastName;

    @NotNull
    @AdultConstraint
    LocalDate birthDate;

    // custom
    String phoneNumber;
    // custom
    String city;
    // custom
    String address;

    String jobPosition;

    @NotBlank @Email
    String email;


    /**
     * Constraint for marking the {@link RegistrationRequestDTO} birth date.
     * Constraint validated by {@link RegistrationRequestDTO.AdultValidator}.
     */
    @Constraint(validatedBy = RegistrationRequestDTO.AdultValidator.class)
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface AdultConstraint {
        String message() default "should be at least 18 years ago";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * Validator for {@link RegistrationRequestDTO.AdultConstraint}: given a birth date, checking if
     * it is at least 18 years ago.
     */
    @Slf4j
    static class AdultValidator implements ConstraintValidator<RegistrationRequestDTO.AdultConstraint, LocalDate> {
        @Override
        public void initialize(RegistrationRequestDTO.AdultConstraint constraint) {
            log.info("Birth date validation: {}", constraint.message());
        }

        @Override
        public boolean isValid(
                LocalDate date,
                ConstraintValidatorContext cxt
        ) {
            return LocalDate.now().minusYears(18).isAfter(date);
        }
    }
}
