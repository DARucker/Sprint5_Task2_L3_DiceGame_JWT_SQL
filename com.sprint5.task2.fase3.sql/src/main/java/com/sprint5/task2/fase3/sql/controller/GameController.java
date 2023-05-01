package com.sprint5.task2.fase3.sql.controller;

import com.sprint5.task2.fase3.sql.auth.AuthenticationResponse;
import com.sprint5.task2.fase3.sql.auth.AuthenticationService;
import com.sprint5.task2.fase3.sql.auth.RegisterRequest;
import com.sprint5.task2.fase3.sql.dto.Gamedto;
import com.sprint5.task2.fase3.sql.dto.Ranking;
import com.sprint5.task2.fase3.sql.dto.Userdto;
import com.sprint5.task2.fase3.sql.service.IGameService;
import com.sprint5.task2.fase3.sql.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/User") // TODO replace endpoint
@RestController
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Spring 5 - Task 2 - Dice Game", description = "This controller contains the methods to play the game")
public class GameController {

    private static Logger log = LoggerFactory.getLogger(GameController.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private IGameService gameService;
    @Autowired
    private AuthenticationService service;

    /**
     * GAMES
     * POST /Users/{id}/games/ : un jugador/a específico realiza un tirón de los dados.
     */
    @Operation(summary= "Roll dices", description = "If dice 1 + dice 2 = 7, then the User wins. The result is saved in the database")
    @ApiResponse(responseCode = "200", description = "Game added to the database", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Gamedto.class))})
    @ApiResponse(responseCode = "403", description = "User not authenticated", content = @Content)
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    @PostMapping("/{id}/games/")
    public ResponseEntity<?> rollDice(@PathVariable int id){
        Userdto userPlaying;
        Gamedto gamedto;
        try {
            userPlaying = userService.findById(id);
        }catch(ResponseStatusException e){
            Map<String, Object> error = new HashMap<>();
            error.put("Message", e.getMessage());
            error.put("Reason", e.getReason());
            return new ResponseEntity<Map<String,Object>>(error, HttpStatus.NOT_FOUND);
        }
        gamedto = gameService.playGame(userPlaying);
        return ResponseEntity.ok(gamedto);
    }

    /**
     * DELETE /Users/{id}/games: elimina las tiradas del jugador/a.
     */
    @Operation(summary= "Delete selected games", description = "deletes all games of selected User.")
    @ApiResponse(responseCode = "200", description = "Games deleted", content = @Content)
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    @DeleteMapping("/{id}/games/")
    public ResponseEntity<?> deleteGamesByUserId(@PathVariable int id){
        try {
            gameService.deleteGamesByUserId(id);
            return ResponseEntity.ok(id);
        }catch(ResponseStatusException e){
            Map<String, Object> error = new HashMap<>();
            error.put("Message", e.getMessage());
            error.put("Reason", e.getReason());
            return new ResponseEntity<Map<String, Object>>(error, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /Users/{id}/games: devuelve el listado de jugadas por un jugador/a.
     * @param id
     * @return
     */
    @Operation(summary= "List all dice rolls for a certein User", description = "Returns the complete list of each User and the result of their dice rolls.")
    @ApiResponse(responseCode = "200", description = "List of rolls", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Gamedto.class))})
    @ApiResponse(responseCode = "403", description = "User not authenticated", content = @Content)
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    @GetMapping("/{id}/games/")
    public ResponseEntity<?> findAllGames(@PathVariable int id) {
        List<Gamedto> gamedtos;
        try {
            gamedtos = gameService.findAllByUserId(id);
        } catch (ResponseStatusException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("Message", e.getMessage());
            error.put("Reason", e.getReason());
            return new ResponseEntity<Map<String, Object>>(error, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(gamedtos);
    }

    /**
     * **  GET /Users/: returns the list of all the Users in the system
     *   with its average percentage of successes..
     */

    @Operation(summary= "List the results of every User", description = "returns the list of all the Users in the system\n" +
            "  with its average success rate.")
    @ApiResponse(responseCode = "200", description = "List of results of all Users", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Ranking.class))})
    @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    @GetMapping("/Users/")
    public ResponseEntity<?> findAllRanking() {
        return ResponseEntity.ok(gameService.listAllRanking());
    }

    /**
     * GET /Users/ranking: devuelve el ranking medio de todos los jugadores/as del sistema. Es decir, el porcentaje medio de logros.
     */
    @Operation(summary= "Average of Users rankings", description = "Returns the average success rate of all Users")
    @ApiResponse(responseCode = "200", description = "Average", content = @Content)
    @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    @GetMapping("/Users/ranking")
    public ResponseEntity<Integer> rankgingAvg(){
        return ResponseEntity.ok(gameService.rankingAvg());
    }

    /**
     *  GET /Users/ranking/loser: Returns the User with the lowest success rate.
     */
    @Operation(summary= "User with the worst ranking", description = "Returns the User with the lowest success rate")
    @ApiResponse(responseCode = "200", description = "User id and games results", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Ranking.class))})
    @ApiResponse(responseCode = "204", description = "No content. There are no games saved in the database", content = @Content)
    @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    @GetMapping("/ranking/looser")
    public ResponseEntity<?> worstUser(){
        Ranking worstUser;
        try {
            worstUser = gameService.worstUser();
        } catch (ResponseStatusException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("Message", e.getMessage());
            error.put("Reason", e.getReason());
            return new ResponseEntity<Map<String, Object>>(error, e.getStatusCode());
        }
        return ResponseEntity.ok(worstUser);
    }

    /**
     * GET /Users/ranking/winenr: Returns the User with the lowest success rate.
     */
    @Operation(summary= "User with the best ranking", description = "Returns the User with the highest success rate")
    @ApiResponse(responseCode = "200", description = "User id and games results", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Ranking.class))})
    @ApiResponse(responseCode = "204", description = "No content. There are no games saved in the database", content = @Content)
    @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    @GetMapping("ranking/winner")
    public ResponseEntity<?> bestUser(){
        Ranking bestUser;
        try {
            bestUser = gameService.bestUser();
        } catch (ResponseStatusException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("Message", e.getMessage());
            error.put("Reason", e.getReason());
            return new ResponseEntity<Map<String, Object>>(error, e.getStatusCode());
        }
        return ResponseEntity.ok(bestUser);
    }
}
