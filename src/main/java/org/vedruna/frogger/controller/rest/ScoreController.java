package org.vedruna.frogger.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vedruna.frogger.persistance.model.Score;
import org.vedruna.frogger.persistance.model.User;
import org.vedruna.frogger.persistance.repository.UserRepositoryI;
import org.vedruna.frogger.service.ScoreServiceI;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/score")
@CrossOrigin
public class ScoreController {

    @Autowired
    private ScoreServiceI scoreService;

    @Autowired
    private UserRepositoryI userRepository;

    private static final String UNAUTHORIZED_MESSAGE = "Usuario no autenticado.";

    @GetMapping("/me")
    public ResponseEntity<?> getMyScore(Principal principal) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UNAUTHORIZED_MESSAGE);
        }

        Optional<Score> score = scoreService.getUserScore(user.get());
        return score.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<Score>> getAllScores(Pageable pageable) {
        Page<Score> scores = scoreService.getAllScores(pageable);
        return ResponseEntity.ok(scores.getContent());
    }

    @PostMapping
    public ResponseEntity<?> createScore(Principal principal, @RequestBody Score score) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UNAUTHORIZED_MESSAGE);
        }

        if (scoreService.getUserScore(user.get()).isPresent()) {
            return ResponseEntity.badRequest().body("Este usuario ya tiene una puntuación.");
        }

        try {
            score.setUser(user.get());
            return ResponseEntity.ok(scoreService.saveScore(score));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateScore(Principal principal, @RequestBody Score newScore) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UNAUTHORIZED_MESSAGE);
        }

        try {
            return ResponseEntity.ok(scoreService.updateScore(user.get(), newScore));
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); 
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
