package com.petadoption.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Singleton Pattern: Manages global appointment statistics and state.
 */
public class AppointmentManager {
    private static AppointmentManager instance;
    private final AtomicInteger totalBooked = new AtomicInteger(0);

    private AppointmentManager() {}

    public static synchronized AppointmentManager getInstance() {
        if (instance == null) {
            instance = new AppointmentManager();
        }
        return instance;
    }

    public void incrementAppointments() {
        totalBooked.incrementAndGet();
    }

    public void decrementAppointments() {
        totalBooked.decrementAndGet();
    }

    public int getTotalAppointments() {
        return totalBooked.get();
    }
}
