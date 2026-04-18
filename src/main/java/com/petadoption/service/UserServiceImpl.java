package com.petadoption.service;

import com.petadoption.dto.UserDTO;
import com.petadoption.factory.UserFactory;
import com.petadoption.model.User;
import com.petadoption.model.Volunteer;
import com.petadoption.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(UserDTO userDTO) {
        String email = userDTO.getEmail().trim().toLowerCase();
        String type = userDTO.getUserType();

        // Check if an account with this SAME email AND SAME role already exists
        if (userRepository.findByEmailAndUserType(email, type).isPresent()) {
            throw new RuntimeException("You already have an account with this email as " + type);
        }

        // Utilizing Factory Pattern to create user based on type
        User user = UserFactory.createUser(userDTO.getUserType());
        user.setName(userDTO.getName().trim());
        user.setEmail(userDTO.getEmail().trim().toLowerCase());
        user.setPhone(userDTO.getPhone());
        user.setPassword(userDTO.getPassword().trim());
        return userRepository.save(user);
    }

    @Override
    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Volunteer toggleVolunteerAvailability(Long id, Boolean status) {
        Volunteer vol = (Volunteer) userRepository.findById(id).orElseThrow();
        vol.setAvailabilityStatus(status);
        return userRepository.save(vol);
    }

    @Override
    public java.util.List<Volunteer> getAllVolunteers() {
        return userRepository.findAllVolunteers();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}
