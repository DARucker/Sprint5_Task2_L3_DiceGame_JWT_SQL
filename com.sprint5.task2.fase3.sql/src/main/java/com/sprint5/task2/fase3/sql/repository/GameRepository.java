package com.sprint5.task2.fase3.sql.repository;

import com.sprint5.task2.fase3.sql.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    List<Game> findAllByUserId(int id);
    @Transactional
    void deleteByUserId(int id);
}
