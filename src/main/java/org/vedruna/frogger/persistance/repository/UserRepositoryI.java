package org.vedruna.frogger.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.vedruna.frogger.persistance.model.User;

import java.util.Optional;


@Repository
public interface UserRepositoryI extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Page<User> findByUsernameStartingWith(String username, Pageable pageable); // ✅ Agrega esta línea

}