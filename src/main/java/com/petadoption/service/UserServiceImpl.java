package com.petadoption.service;

import com.petadoption.dto.UserDTO;
import com.petadoption.factory.UserFactory;
import com.petadoption.model.User;
import com.petadoption.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(UserDTO userDTO) {
        // Utilizing Factory Pattern to create user based on type
        User user = UserFactory.createUser(userDTO.getUserType());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setPassword(userDTO.getPassword());
        return userRepository.save(user);
    }

    @Override
    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
