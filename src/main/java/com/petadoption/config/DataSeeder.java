package com.petadoption.config;

import com.petadoption.model.Pet;
import com.petadoption.model.Status;
import com.petadoption.repository.PetRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(PetRepository repository, com.petadoption.repository.UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                // Seed a Staff User
                com.petadoption.model.ShelterStaff staff = new com.petadoption.model.ShelterStaff();
                staff.setName("Staff Member");
                staff.setEmail("staff@pethaven.com");
                staff.setPassword("staff123");
                staff.setUserType("SHELTER_STAFF");
                userRepository.save(staff);

                // Seed an Adopter
                com.petadoption.model.Adopter adopter = new com.petadoption.model.Adopter();
                adopter.setName("John Doe");
                adopter.setEmail("john@example.com");
                adopter.setPassword("john123");
                adopter.setUserType("ADOPTER");
                userRepository.save(adopter);

                // Seed a Breeder
                com.petadoption.model.Breeder breeder = new com.petadoption.model.Breeder();
                breeder.setName("Happy Breeder");
                breeder.setEmail("breeder@example.com");
                breeder.setPassword("breeder123");
                breeder.setUserType("BREEDER");
                userRepository.save(breeder);

                // Seed a Veterinarian
                
            }
            
            if (repository.count() == 0) {
                repository.save(Pet.builder()
                        .name("Buddy")
                        .species("Dog")
                        .age(2)
                        .vaccinationStatus(true)
                        .availabilityStatus(Status.AVAILABLE)
                        .healthStatus("Excellent")
                        .imageUrl("img/hero.png")
                        .build());
                
                repository.save(Pet.builder()
                        .name("Whiskers")
                        .species("Cat")
                        .age(1)
                        .vaccinationStatus(true)
                        .availabilityStatus(Status.AVAILABLE)
                        .healthStatus("Healthy")
                        .imageUrl("img/pet.png")
                        .build());
                
                repository.save(Pet.builder()
                        .name("Cooper")
                        .species("Dog")
                        .age(4)
                        .vaccinationStatus(false)
                        .availabilityStatus(Status.AVAILABLE)
                        .healthStatus("Needs Checkup")
                        .imageUrl("img/pet.png")
                        .build());

                repository.save(Pet.builder()
                        .name("Luna")
                        .species("Cat")
                        .age(3)
                        .vaccinationStatus(true)
                        .availabilityStatus(Status.AVAILABLE)
                        .healthStatus("Good")
                        .imageUrl("img/pet.png")
                        .build());
            }
        };
    }
}
