package com.petadoption.singleton;

import com.petadoption.model.Pet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton Pattern for PetRegistry.
 * Ensures only one instance exists and provides thread-safe access to a shared pet registry.
 */
public class PetRegistry {
    private static volatile PetRegistry instance;
    private final List<Pet> registeredPets;

    private PetRegistry() {
        // Private constructor prevents instantiation from other classes
        registeredPets = Collections.synchronizedList(new ArrayList<>());
    }

    public static PetRegistry getInstance() {
        if (instance == null) {
            synchronized (PetRegistry.class) {
                if (instance == null) {
                    instance = new PetRegistry();
                }
            }
        }
        return instance;
    }

    public void registerPet(Pet pet) {
        registeredPets.add(pet);
    }

    public List<Pet> getRegisteredPets() {
        return new ArrayList<>(registeredPets);
    }
}
