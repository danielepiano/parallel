package com.dp.spring.parallel.ponos.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Jacksonized
@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkplaceBookingRequestDTO {

    @NotNull
    @WorkingDayConstraint
    @FutureDateConstraint
    LocalDate bookingDate;


    /**
     * Constraint for marking the {@link WorkplaceBookingRequestDTO} booking date.
     * Constraint validated by {@link WorkplaceBookingDateValidator.WorkingDay}.
     */
    @Constraint(validatedBy = WorkplaceBookingDateValidator.WorkingDay.class)
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface WorkingDayConstraint {
        String message() default "non-working day, workplaces bookable only from Monday to Friday";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * Constraint for marking the {@link WorkplaceBookingRequestDTO} booking date.
     * Constraint validated by {@link WorkplaceBookingDateValidator.FutureDate}.
     */
    @Constraint(validatedBy = WorkplaceBookingDateValidator.FutureDate.class)
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface FutureDateConstraint {
        String message() default "cannot book on current date or before";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }


    static class WorkplaceBookingDateValidator {

        /**
         * Validator for {@link WorkingDayConstraint}: checking if the booking date is between MON-FRI.
         */
        static class WorkingDay implements ConstraintValidator<WorkingDayConstraint, LocalDate> {

            @Override
            public void initialize(WorkingDayConstraint constraint) {
                System.out.println(constraint.message());
            }

            @Override
            public boolean isValid(
                    LocalDate date,
                    ConstraintValidatorContext cxt
            ) {
                final DayOfWeek day = date.getDayOfWeek();
                return !day.equals(DayOfWeek.SATURDAY) && !day.equals(DayOfWeek.SUNDAY);
            }
        }

        /**
         * Validator for {@link FutureDateConstraint}: checking if the booking date is a future date.
         */
        static class FutureDate implements ConstraintValidator<FutureDateConstraint, LocalDate> {

            @Override
            public void initialize(FutureDateConstraint constraint) {
                System.out.println(constraint.message());
            }

            @Override
            public boolean isValid(
                    LocalDate date,
                    ConstraintValidatorContext cxt
            ) {
                return date.isAfter(LocalDate.now());
            }
        }
    }
}