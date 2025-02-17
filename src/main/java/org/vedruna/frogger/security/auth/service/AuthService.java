package org.vedruna.frogger.security.auth.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vedruna.frogger.persistance.model.Rol;
import org.vedruna.frogger.persistance.model.User;
import org.vedruna.frogger.persistance.repository.RolRepositoryI;
import org.vedruna.frogger.persistance.repository.UserRepositoryI;
import org.vedruna.frogger.security.auth.dto.AuthResponseDTO;
import org.vedruna.frogger.security.auth.dto.LoginRequestDTO;
import org.vedruna.frogger.security.auth.dto.RegisterRequestDTO;

import java.util.Optional;

@Service
public class AuthService implements AuthServiceI {

    @Autowired
    private UserRepositoryI userRepo;

    @Autowired
    private RolRepositoryI rolRepo;

    @Autowired
    private JWTServiceImpl jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword()));
        User user=userRepo.findByUsername(request.getName()).orElseThrow();
        return new AuthResponseDTO(jwtService.getToken(user));
    }

    public void register(RegisterRequestDTO request) {
        Rol rol = rolRepo.findByRolName("USER")
                    .orElseThrow(() -> new RuntimeException("Rol not found"));
        User user = new User();
        user.setUsername(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setUserRol(rol);
        userRepo.save(user);
    }
}
