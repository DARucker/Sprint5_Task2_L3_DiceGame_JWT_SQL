package com.sprint5.task2.fase3.sql.service;

import com.sprint5.task2.fase3.sql.dto.Gamedto;
import com.sprint5.task2.fase3.sql.dto.Userdto;
import com.sprint5.task2.fase3.sql.dto.Ranking;
import com.sprint5.task2.fase3.sql.entity.Game;

import java.util.List;

public interface IGameService {

    int rollDice();
    Gamedto playGame(Userdto userdto);
    Game saveGame(Gamedto gamedto);
    void deleteGamesByUserId(int id);
    Gamedto entityToDto(Game game);
    Game dtoToEntity(Gamedto gamedto);
    List<Gamedto> findAllByUserId(int userId);
    List<Ranking> listAllRanking();
    public int rankingAvg();
    public Ranking worstUser();
    public Ranking bestUser();
}
