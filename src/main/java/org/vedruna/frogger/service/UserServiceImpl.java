package org.vedruna.frogger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import org.vedruna.frogger.dto.UserDTO;
import org.vedruna.frogger.persistance.model.User;
import org.vedruna.frogger.persistance.repository.ScoreRepositoryI;
import org.vedruna.frogger.persistance.repository.UserRepositoryI;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserServiceI {

   @Autowired
private UserRepositoryI userRepository;

@Autowired
private ScoreRepositoryI scoreRepository;


    @Override
    public UserDTO getMyUser(User user) {
        return new UserDTO(user);
    }
    

    @Override
    public Page<UserDTO> searchUsersByName(String name, Pageable pageable) {
        return userRepository.findByUsernameStartingWith(name, pageable)
                .map(UserDTO::new);
    }

    @Override
    public Optional<UserDTO> getUserByName(String name) {
        return userRepository.findByUsername(name).map(UserDTO::new);
    }

    @Override
    public Optional<UserDTO> getUserById(Integer userId) {
        return userRepository.findById(userId).map(UserDTO::new);
    }

@Override
public void deleteUserById(Integer userId) {
    if (!userRepository.existsById(userId)) {
        throw new EmptyResultDataAccessException("User not found", 1);
    }
    
    // Eliminar la puntuación asociada antes de eliminar el usuario
    scoreRepository.findByUser(userRepository.findById(userId).orElseThrow())
        .ifPresent(score -> scoreRepository.delete(score));

    userRepository.deleteById(userId);
}

}
