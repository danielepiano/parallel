package com.dp.spring.parallel.hestia.api.dtos;

import com.dp.spring.parallel.hestia.utils.RandomPasswordUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

@Jacksonized
@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@ChangePasswordRequestDTO.PasswordMatchConstraint
public class ChangePasswordRequestDTO {
    @NotBlank
    String current;

    @NotBlank
    @Pattern(
            regexp = RandomPasswordUtils.DEFAULT_PASSWORD_REGEX,
            message = "deve contenere almeno 8 caratteri, di cui almeno una lettera maiuscola, " +
                    "una lettera minuscola, un numero, un carattere speciale tra @#$%^&+=!")
    String updated;

    @NotBlank
    String confirm;


    /**
     * Constraint for marking the {@link ChangePasswordRequestDTO}.<br>
     * Constraint validated by {@link PasswordMatchValidator}.
     */
    @Constraint(validatedBy = PasswordMatchValidator.class)
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PasswordMatchConstraint {
        String message() default "passwords don't match";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * Validator for {@link PasswordMatchConstraint}: given a {@link ChangePasswordRequestDTO}, checking if
     * the new password and the confirmation field match.
     */
    public static class PasswordMatchValidator
            implements ConstraintValidator<PasswordMatchConstraint, ChangePasswordRequestDTO> {

        @Override
        public void initialize(PasswordMatchConstraint constraint) {
            System.out.println(constraint.message());
        }

        @Override
        public boolean isValid(
                ChangePasswordRequestDTO request,
                ConstraintValidatorContext cxt
        ) {
            return Objects.equals(request.getUpdated(), request.getConfirm());
        }
    }
}
