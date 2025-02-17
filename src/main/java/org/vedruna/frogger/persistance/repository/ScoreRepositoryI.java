package org.vedruna.frogger.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.vedruna.frogger.persistance.model.Score;
import org.vedruna.frogger.persistance.model.User;

import java.util.Optional;

@Repository
public interface ScoreRepositoryI extends JpaRepository<Score, Integer> {
    // Método modificado para devolver los puntajes ordenados de menor a mayor
    Page<Score> findAllByOrderByTimeAsc(Pageable pageable);
    Optional<Score> findByUser(User user);
}
