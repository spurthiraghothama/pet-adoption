package com.petadoption.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public final class AppointmentScheduleValidator {

    private static final Set<LocalTime> ALLOWED_SLOT_STARTS = Set.of(
            LocalTime.of(10, 0),
            LocalTime.of(11, 0),
            LocalTime.of(12, 0),
            LocalTime.of(13, 0),
            LocalTime.of(15, 0),
            LocalTime.of(16, 0),
            LocalTime.of(17, 0),
            LocalTime.of(18, 0),
            LocalTime.of(19, 0)
    );

    private AppointmentScheduleValidator() {
    }

    public static void validate(LocalDate date, LocalTime time) {
        if (date == null) {
            throw new RuntimeException("Appointment date is required");
        }

        if (time == null) {
            throw new RuntimeException("Appointment time is required");
        }

        if (!ALLOWED_SLOT_STARTS.contains(time)) {
            throw new RuntimeException(
                    "Appointments must use 1-hour slots from 10 AM to 8 PM, with no bookings from 2 PM to 3 PM");
        }
    }
}
