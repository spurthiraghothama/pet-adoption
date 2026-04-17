package com.petadoption.repository;

import com.petadoption.model.User;
import com.petadoption.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUserType(String userType);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndUserType(String email, String userType);

    @Query("SELECT v FROM Volunteer v")
    List<Volunteer> findAllVolunteers();
}
