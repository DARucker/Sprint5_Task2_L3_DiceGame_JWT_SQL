package com.sprint5.task2.fase3.sql.auth;

import com.sprint5.task2.fase3.sql.controller.GameController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;


@Tag(name = "Authentication", description = "This controller allows to register, update or authenticate the user and generates the access token to play the game")
@SecurityRequirement(name = "jwtopenapi")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static Logger log = LoggerFactory.getLogger(GameController.class);

    private final AuthenticationService service;


    /**
     * This method creates a User
     *
     * @param request
     * @return ResponseEntity<AuthenticationResponse>
     */
    @Operation(summary = "Adds a new User", description = "Creates a new User and saves it in the database")
    @ApiResponse(responseCode = "200", description = "User created correctly", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = AuthenticationResponse.class))})
    @ApiResponse(responseCode = "400", description = "The User already exists", content = @Content)
    @PostMapping("/add")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(service.register(request));
        } catch (ResponseStatusException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("Message", e.getMessage());
            error.put("Reason", e.getReason());
            return new ResponseEntity<Map<String, Object>>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Authenticates the user", description = "Authenticates the user and returns the session token")
    @ApiResponse(responseCode = "200", description = "", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = AuthenticationResponse.class))})
    @ApiResponse(responseCode = "403", description = "User not authenticated", content = @Content)
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok((service.authenticate(request)));
    }

    /**
     * PUT /Users: modifica el nombre del jugador/a.
     */
    @Operation(summary = "Update User", description = "Updates the name of an existing User")
    @ApiResponse(responseCode = "201", description = "User updated correctly", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = RegisterRequest.class))})
    @ApiResponse(responseCode = "403", description = "User not authenticated", content = @Content)
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    @PutMapping(value = "/update/")
    public ResponseEntity<?> updateUser(@RequestBody RegisterRequest request) {
        log.info("update User: " + request);
        try {
            service.update(request);
            return ResponseEntity.ok(request);
        } catch (ResponseStatusException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("Message", e.getMessage());
            error.put("Reason", e.getReason());
            return new ResponseEntity<Map<String, Object>>(error, e.getStatusCode());
        }
    }
}