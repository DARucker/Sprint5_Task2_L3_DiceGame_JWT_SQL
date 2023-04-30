package com.sprint5.task2.fase3.sql.service;

import com.sprint5.task2.fase3.sql.dto.Gamedto;
import com.sprint5.task2.fase3.sql.dto.Ranking;
import com.sprint5.task2.fase3.sql.dto.Userdto;
import com.sprint5.task2.fase3.sql.entity.Game;
import com.sprint5.task2.fase3.sql.entity.User;
import com.sprint5.task2.fase3.sql.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements IGameService {

    private static final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

    private final GameRepository gameRepository;
    @Bean
    public ModelMapper modelMapper1() {
        return new ModelMapper();
    }

    @Autowired
    private IUserService userService;

    @Override
    public Gamedto playGame(Userdto userdto) {
        Gamedto gamedto = new Gamedto();
        gamedto.setUserdto(userdto);
        gamedto.setPoints(rollDice());

        int points = rollDice();
        if(gamedto.getPoints()==7){
            gamedto.setResultGame("Win");
        }else {
            gamedto.setResultGame("Loose");
        }
        Game savedGame = new Game();
        savedGame = saveGame(gamedto);
        Gamedto savedGamedto = entityToDto(savedGame);
        return savedGamedto;

    }

    @Override
    public Game saveGame(Gamedto gamedto){
        Game saved = dtoToEntity(gamedto);
        saved.setUser(userService.dtoToEntity(gamedto.getUserdto()));
        gameRepository.save(saved);
        return saved;
    }

    /*
     * DELETE /players/{id}/games: elimina las tiradas del jugador/a.
     */

    public void deleteGamesByUserId(int id){

        if(findAllByUserId(id).isEmpty()){
            log.error("error deleting list of games for player " + id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No games were found for user: " + id);
        }
        gameRepository.deleteByUserId(id);
    }





    /**
     * findAllByPlayerId
     * @param userId
     * @return List<Gamedto>
     */
    @Override
    public List<Gamedto> findAllByUserId(int userId) {
        List<Game> gamesByUserId = gameRepository.findAllByUserId(userId);
        if(gamesByUserId.isEmpty()){
            log.info("There are no games por user with id: " + userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user was found with id: " + userId);
        }
        List<Gamedto> gamedtosByUserId = new ArrayList<>();
        gamedtosByUserId = gamesByUserId.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return gamedtosByUserId;
    }

    /**
     * **  GET /users/: devuelve el listado de todos los jugadores/as
     * del sistema con su porcentaje medio de éxitos.
     */

    public List<Ranking> listAllRanking(){
        List<Ranking> allRanking = new ArrayList<>();
        List<Game> gamesPlayed = gameRepository.findAll();
        List<User> UserList = userService.findAll();
        for (User User : UserList){
            int id = User.getId();
            String name = User.getName();
            if(name.equals("")){
                name="Anonymous";
            }
            Long win = gamesPlayed.stream().filter(x -> x.getUser().getId() == id && x.getResultGame().equals("Win")).count();
            Long played = gamesPlayed.stream().filter(x -> x.getUser().getId()==id).count();
            double calcularRatio = 0;
            if(win>0){calcularRatio =  (double) win /played*100;}
            int ratio = (int) calcularRatio;
            Ranking ranking = new Ranking(0, id,name, win, played, ratio);
            allRanking.add(ranking);
            log.info("ranking "+ ranking);
        }
        return allRanking;
    }

    /**
     * GET /players/ranking: devuelve el ranking medio de todos los jugadores/as del sistema. Es decir, el porcentaje medio de logros.
     */

    public int rankingAvg(){
        List<Game> gamesPlayed = gameRepository.findAll();
        List<User> userList = userService.findAll();

        int gamesWon = (int) gamesPlayed.stream().filter(x -> x.getResultGame().equals("Win")).count();
        double calcularRatio = 0;
        if(gamesWon>0){
            calcularRatio =  (double) gamesWon /gamesPlayed.size()*100;
        }
        return (int) calcularRatio;
    }
    /**
     *     GET /players/ranking/loser: devuelve al jugador/a con peor porcentaje de éxito.
     */
    public Ranking worstUser(){
        List<Ranking> worstRankings = listAllRanking().stream()
                .filter(x -> x.getPlayed()>0)
                .sorted(Comparator.comparingInt(Ranking::getRatio))
                .limit(1)
                .collect(Collectors.toList());
        if(worstRankings.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No games were stored");
        }
        return worstRankings.get(0);
    }

    /**
     *     GET /players/ranking/winer: devuelve al jugador/a con mejor porcentaje de éxito.
     */
    public Ranking bestUser(){
        List<Ranking> bestRankings = listAllRanking()
                .stream()
                .sorted(Comparator.comparingInt(Ranking::getRatio).reversed())
                .limit(1)
                .collect(Collectors.toList());
        if(bestRankings.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No games were stored");
        }
        return bestRankings.get(0);
    }

    @Override
    public int rollDice() {

        int dice1 = (int) (Math.random()*6);
        int dice2 = (int) (Math.random()*6);
        return dice1+dice2;
    }

    /**
     * This method recibes a Game and return a DTO objecto of Game
     * @param game
     * @return gamedto
     */
    @Override
    public Gamedto entityToDto(Game game) {
        Gamedto gamedto = modelMapper1().map(game, Gamedto.class);
        return gamedto;
    }

    /**
     * This method recives a DTO objecto to trasnform it into an entity
     * @param gamedto
     * @return game
     */
    @Override
    public Game dtoToEntity(Gamedto gamedto) {
        Game game = modelMapper1().map(gamedto, Game.class);
        return game;
    }

}
