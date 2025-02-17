package org.vedruna.frogger.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vedruna.frogger.dto.UserDTO;
import org.vedruna.frogger.persistance.model.User;
import org.vedruna.frogger.persistance.repository.UserRepositoryI;
import org.vedruna.frogger.service.UserServiceI;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserServiceI userService;

    @Autowired
    private UserRepositoryI userRepository;

    private static final String UNAUTHORIZED_MESSAGE = "Usuario no autenticado.";

    // Obtener información del usuario logueado
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyUser(Principal principal) {
        Optional<UserDTO> user = userService.getUserByName(principal.getName());
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Buscar usuarios por nombre
    @GetMapping("/search")
    public ResponseEntity<Page<UserDTO>> searchUsers(@RequestParam String name, Pageable pageable) {
        return ResponseEntity.ok(userService.searchUsersByName(name, pageable));
    }

    // Obtener perfil de un usuario por nombre
    @GetMapping("/profile/{name}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable String name) {
        Optional<UserDTO> user = userService.getUserByName(name);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener usuario por ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer userId) {
        Optional<UserDTO> user = userService.getUserById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar usuario por ID
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para seguir a un usuario
    @PostMapping("/follow/{userId}")
    public ResponseEntity<?> followUser(Principal principal, @PathVariable Integer userId) {
        Optional<User> loggedUser = userRepository.findByUsername(principal.getName());
        Optional<User> userToFollow = userRepository.findById(userId);

        if (loggedUser.isEmpty() || userToFollow.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado.");
        }

        User user = loggedUser.get();
        User target = userToFollow.get();

        if (user.getUsersIFollow().contains(target)) {
            return ResponseEntity.badRequest().body("Ya sigues a este usuario.");
        }

        user.getUsersIFollow().add(target);
        userRepository.save(user);
        return ResponseEntity.ok("Ahora sigues a " + target.getUsername());
    }

    // Endpoint para dejar de seguir a un usuario
    @DeleteMapping("/unfollow/{userId}")
    public ResponseEntity<?> unfollowUser(Principal principal, @PathVariable Integer userId) {
        Optional<User> loggedUser = userRepository.findByUsername(principal.getName());
        Optional<User> userToUnfollow = userRepository.findById(userId);

        if (loggedUser.isEmpty() || userToUnfollow.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado.");
        }

        User user = loggedUser.get();
        User target = userToUnfollow.get();

        if (!user.getUsersIFollow().contains(target)) {
            return ResponseEntity.badRequest().body("No sigues a este usuario.");
        }

        user.getUsersIFollow().remove(target);
        userRepository.save(user);
        return ResponseEntity.ok("Has dejado de seguir a " + target.getUsername());
    }
}
