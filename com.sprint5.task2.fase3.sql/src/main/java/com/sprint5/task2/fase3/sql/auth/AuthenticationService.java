package com.sprint5.task2.fase3.sql.auth;

import com.sprint5.task2.fase3.sql.config.JwtService;
import com.sprint5.task2.fase3.sql.entity.Role;
import com.sprint5.task2.fase3.sql.entity.User;
import com.sprint5.task2.fase3.sql.repository.UserRepository;
import com.sprint5.task2.fase3.sql.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;

    public AuthenticationResponse register(RegisterRequest request){
        if(!request.getName().equals("")) {
            Optional<User> UserDbName = userService.findByName(request.getName());
            if (UserDbName.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Te user with name " + request.getName() + " or email " + request.getEmail() + " exists.");
            }
        }
            Optional<User> UserDbEmail = userService.findByEmail(request.getEmail());
            if (UserDbEmail.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Te user with name " + request.getName() + " or email " + request.getEmail() + " exists.");
        }
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .registDate(Calendar.getInstance())
                .role(Role.USER) // In this exercise I am hardcoding the role
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * This method updates the name of the player
     * @param  request
     * @return void
     */

    public AuthenticationResponse update(RegisterRequest request){

        Optional<User> userDbEmail = userService.findByEmail(request.getEmail());
        if (!userDbEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Te user with email " + request.getEmail() + " is not registered in database.");
        }
        User userUpdate = userDbEmail.get();
        userUpdate.setName(request.getName());
        repository.save(userUpdate);
        var jwtToken = jwtService.generateToken(userUpdate);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }



}
