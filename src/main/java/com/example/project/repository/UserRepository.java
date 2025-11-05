package com.example.project.repository;

import com.example.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User by their username.
     * Spring Data JPA automatically generates the query for this method based on its name.
     *
     * @param username The username to search for.
     * @return The User object wrapped in an Optional, or an empty Optional if not found.
     */
    Optional<User> findByUsername(String username);

}