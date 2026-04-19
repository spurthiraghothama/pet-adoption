package com.petadoption.service;

import com.petadoption.dto.UserDTO;
import com.petadoption.model.User;
import com.petadoption.model.Volunteer;
import java.util.*;

public interface UserService {
    User registerUser(UserDTO userDTO);
    List<User> getAllUsers();
    Volunteer toggleVolunteerAvailability(Long id, Boolean status);
    List<Volunteer> getAllVolunteers();
    User getUserById(Long id);
}
