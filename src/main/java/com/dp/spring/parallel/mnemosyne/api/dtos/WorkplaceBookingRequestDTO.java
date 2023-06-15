package com.dp.spring.parallel.mnemosyne.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;

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
     * Constraint validated by {@link BookingDateValidator.WorkingDay}.
     */
    @Constraint(validatedBy = BookingDateValidator.WorkingDay.class)
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface WorkingDayConstraint {
        String message() default "non-working day, bookings are allowed only from Monday to Friday";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * Constraint for marking the {@link WorkplaceBookingRequestDTO} booking date.
     * Constraint validated by {@link BookingDateValidator.FutureDate}.
     */
    @Constraint(validatedBy = BookingDateValidator.FutureDate.class)
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface FutureDateConstraint {
        String message() default "cannot book on current date or before";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    static class BookingDateValidator {

        /**
         * Validator for {@link WorkingDayConstraint}: checking if the booking date is between MON-FRI.
         */
        @Slf4j
        static class WorkingDay implements ConstraintValidator<WorkingDayConstraint, LocalDate> {
            @Override
            public void initialize(WorkingDayConstraint constraint) {
                log.info("Booking costraint validation: {}", constraint.message());
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
        @Slf4j
        static class FutureDate implements ConstraintValidator<FutureDateConstraint, LocalDate> {
            @Override
            public void initialize(FutureDateConstraint constraint) {
                log.info("Booking costraint validation: {}", constraint.message());
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