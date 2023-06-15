package com.dp.spring.parallel.agora.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Jacksonized
@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@CreateUpdateEventRequestDTO.TimeIntervalConstraint
@CreateUpdateEventRequestDTO.TimeIntervalDurationConstraint
public class CreateUpdateEventRequestDTO {

    @NotBlank
    String name;

    @NotNull
    @WorkingDayConstraint
    @FutureDateConstraint
    LocalDate eventDate;

    @NotNull
    LocalTime startTime;

    @NotNull
    LocalTime endTime;

    @NotNull @Min(2) @Max(50)
    Integer maxPlaces;


    /**
     * Constraint for marking the {@link CreateUpdateEventRequestDTO} event date.
     * Constraint validated by {@link CreateUpdateEventRequestDTO.CreateEventRequestDTODateAndTimeValidator.WorkingDay}.
     */
    @Constraint(validatedBy = CreateUpdateEventRequestDTO.CreateEventRequestDTODateAndTimeValidator.WorkingDay.class)
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface WorkingDayConstraint {
        String message() default "non-working day, events can only be scheduled from Monday to Friday";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * Constraint for marking the {@link CreateUpdateEventRequestDTO} event date.
     * Constraint validated by {@link CreateUpdateEventRequestDTO.CreateEventRequestDTODateAndTimeValidator.FutureDate}.
     */
    @Constraint(validatedBy = CreateUpdateEventRequestDTO.CreateEventRequestDTODateAndTimeValidator.FutureDate.class)
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface FutureDateConstraint {
        String message() default "cannot set current date or before";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * Constraint for marking the {@link CreateUpdateEventRequestDTO}.<br>
     * Constraint validated by {@link CreateUpdateEventRequestDTO.CreateEventRequestDTODateAndTimeValidator.TimeInterval}.
     */
    @Constraint(validatedBy = CreateUpdateEventRequestDTO.CreateEventRequestDTODateAndTimeValidator.TimeInterval.class)
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface TimeIntervalConstraint {
        String message() default "end-time should be after start-time";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * Constraint for marking the {@link CreateUpdateEventRequestDTO}.<br>
     * Constraint validated by {@link CreateUpdateEventRequestDTO.CreateEventRequestDTODateAndTimeValidator.TimeIntervalDuration}.
     */
    @Constraint(validatedBy = CreateUpdateEventRequestDTO.CreateEventRequestDTODateAndTimeValidator.TimeIntervalDuration.class)
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface TimeIntervalDurationConstraint {
        String message() default "event should be at least half-an-hour long";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    static class CreateEventRequestDTODateAndTimeValidator {

        /**
         * Validator for {@link CreateUpdateEventRequestDTO.WorkingDayConstraint}: checking if the event date is between MON-FRI.
         */
        @Slf4j
        static class WorkingDay implements ConstraintValidator<CreateUpdateEventRequestDTO.WorkingDayConstraint, LocalDate> {
            @Override
            public void initialize(CreateUpdateEventRequestDTO.WorkingDayConstraint constraint) {
                log.info("Headquarters-event costraint validation: {}", constraint.message());
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
         * Validator for {@link CreateUpdateEventRequestDTO.FutureDateConstraint}: checking if the event date is a future date.
         */
        @Slf4j
        static class FutureDate implements ConstraintValidator<CreateUpdateEventRequestDTO.FutureDateConstraint, LocalDate> {
            @Override
            public void initialize(CreateUpdateEventRequestDTO.FutureDateConstraint constraint) {
                log.info("Headquarters-event costraint validation: {}", constraint.message());
            }

            @Override
            public boolean isValid(
                    LocalDate date,
                    ConstraintValidatorContext cxt
            ) {
                return date.isAfter(LocalDate.now());
            }
        }

        /**
         * Validator for {@link CreateUpdateEventRequestDTO.TimeIntervalConstraint}: checking if start-time is before end-time.
         */
        @Slf4j
        static class TimeInterval implements ConstraintValidator<CreateUpdateEventRequestDTO.TimeIntervalConstraint, CreateUpdateEventRequestDTO> {
            @Override
            public void initialize(CreateUpdateEventRequestDTO.TimeIntervalConstraint constraint) {
                log.info("Headquarters-event costraint validation: {}", constraint.message());
            }

            @Override
            public boolean isValid(
                    CreateUpdateEventRequestDTO request,
                    ConstraintValidatorContext cxt
            ) {
                try {
                    return request.getStartTime().isBefore(request.getEndTime());
                } catch (Exception exception) {
                    return false;
                }
            }
        }

        /**
         * Validator for {@link CreateUpdateEventRequestDTO.TimeIntervalDurationConstraint}: checking the duration of the event.
         */
        @Slf4j
        static class TimeIntervalDuration implements ConstraintValidator<CreateUpdateEventRequestDTO.TimeIntervalDurationConstraint, CreateUpdateEventRequestDTO> {
            @Override
            public void initialize(CreateUpdateEventRequestDTO.TimeIntervalDurationConstraint constraint) {
                log.info("Headquarters-event costraint validation: {}", constraint.message());
            }

            @Override
            public boolean isValid(
                    CreateUpdateEventRequestDTO request,
                    ConstraintValidatorContext cxt
            ) {
                try {
                    return Duration.between(request.getStartTime(), request.getEndTime()).toMinutes() >= 30;
                } catch (Exception exception) {
                    return false;
                }
            }
        }
    }
}
