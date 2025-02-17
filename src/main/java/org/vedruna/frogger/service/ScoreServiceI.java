package org.vedruna.frogger.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vedruna.frogger.persistance.model.Score;
import org.vedruna.frogger.persistance.model.User;

import java.util.Optional;

public interface ScoreServiceI {
    Page<Score> getAllScores(Pageable pageable);
    Optional<Score> getUserScore(User user);
    Score saveScore(Score score);
    Score updateScore(User user, Score newScore);
}
