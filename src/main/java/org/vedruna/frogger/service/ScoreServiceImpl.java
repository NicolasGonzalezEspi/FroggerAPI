package org.vedruna.frogger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.vedruna.frogger.persistance.model.Score;
import org.vedruna.frogger.persistance.model.User;
import org.vedruna.frogger.persistance.repository.ScoreRepositoryI;

import java.util.Optional;

@Service
public class ScoreServiceImpl implements ScoreServiceI {

    @Autowired
    private ScoreRepositoryI scoreRepository;

    @Override
    public Page<Score> getAllScores(Pageable pageable) {
        return scoreRepository.findAllByOrderByTimeAsc(pageable); // ✅ Ahora ordena de menor a mayor
    }

    @Override
    public Optional<Score> getUserScore(User user) {
        return scoreRepository.findByUser(user);
    }

    @Override
    public Score saveScore(Score score) {
        Optional<Score> existingScore = scoreRepository.findByUser(score.getUser());
        if (existingScore.isPresent()) {
            throw new IllegalArgumentException("Este usuario ya tiene una puntuación.");
        }
        return scoreRepository.save(score);
    }

    @Override
    public Score updateScore(User user, Score newScore) {
        Score score = scoreRepository.findByUser(user)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró puntuación para este usuario."));
        
        if (newScore.getTime().isBefore(score.getTime())) {
            score.setTime(newScore.getTime());
            return scoreRepository.save(score);
        } else {
            throw new IllegalArgumentException("El nuevo tiempo debe ser menor que el actual.");
        }
    }
}
