package org.vedruna.frogger.service;


import org.vedruna.frogger.dto.UserDTO; // IMPORTANTE
import org.vedruna.frogger.persistance.model.User; // IMPORTANTE

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserServiceI {
    UserDTO getMyUser(User user);
    Page<UserDTO> searchUsersByName(String name, Pageable pageable);
    Optional<UserDTO> getUserByName(String name);
    Optional<UserDTO> getUserById(Integer userId);
    void deleteUserById(Integer userId);
}