package com.petadoption.factory;

import com.petadoption.model.*;

public class UserFactory {
    public static User createUser(String type) {
        if (type == null) {
            return null;
        }
        switch (type.toUpperCase()) {
            case "ADOPTER":
                Adopter adopter = new Adopter();
                adopter.setUserType("ADOPTER");
                return adopter;
            case "VOLUNTEER":
                Volunteer volunteer = new Volunteer();
                volunteer.setUserType("VOLUNTEER");
                return volunteer;
            case "SHELTER_STAFF":
            case "STAFF":
                ShelterStaff staff = new ShelterStaff();
                staff.setUserType("SHELTER_STAFF");
                return staff;
            case "BREEDER":
                Breeder breeder = new Breeder();
                breeder.setUserType("BREEDER");
                return breeder;
            default:
                throw new IllegalArgumentException("Unknown user type: " + type);
        }
    }
}
