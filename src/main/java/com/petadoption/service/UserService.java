package com.petadoption.service;

import com.petadoption.dto.UserDTO;
import com.petadoption.model.User;

public interface UserService {
    User registerUser(UserDTO userDTO);
    java.util.List<User> getAllUsers();
}
