# Sprint5 Task2 Level1 Fase 3:  Dice Game with JWT + SQL

This is your final project, an API 100% designed for you where you will apply
everything you know so far to create a complete application, from
the database to security. Apply everything you know even what is not asked for.

- Level 1

The dice game is played with two dice. If the result of the sum of both dice is 7,
the game will be won, if not lost. A player can see a list of all rolls
you have done and the percentage of success.

In order to play the game and make a spin, a user must register with a non-repeating name. When created, it is assigned a
unique numeric identifier and a registration date. If the user so wishes, you can not add any name and it will be called
 "ANONYMOUS". There may be more than one “ANONYMOUS” player.
Each player can see a list of all the rolls they have made, with the value of each data and whether they have won or not
 the match. In addition, you can know your success rate for all the rolls you have made.

You cannot delete a specific game, but you can delete the entire list of spins by a player.

The software must allow listing all the players in the system, the success rate of each player and the average success rate of all the players in the system.

The software must respect the main design patterns.
GRADES

You have to take into account the following construction details: - 

- URL's:

- POST: /players: create a player.
- PUT /players: modifies the name of the player.
- POST /players/{id}/games/ : a specific player rolls the dice.
- DELETE /players/{id}/games – Deletes the player's rolls.
- GET /players/: returns the list of all the players in the system with their average percentage of successes.
- GET /players/{id}/games: returns the list of games played by a player.
- GET /players/ranking: returns the average ranking of all players in the system. That is, the average percentage of achievements.
- GET /players/ranking/loser: returns the player with the worst success rate.
- GET /players/ranking/winner: Returns the player with the worst success rate.

 - Swagger:

   http://localhost:8080/swagger-ui/index.html#/
   springdoc.api-docs.path = /dicegame-V-sql-openapi


- Phase 1
Persistence: uses MySQL as database.
- Phase 2
Change everything you need and use MongoDB to persist the data.
- Phase 3
Add security: include JWT authentication in all accesses to the URL's of the microservice.
