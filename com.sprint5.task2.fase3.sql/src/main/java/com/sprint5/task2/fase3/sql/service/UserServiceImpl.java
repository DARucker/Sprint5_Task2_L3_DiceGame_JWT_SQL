package com.sprint5.task2.fase3.sql.service;

import com.sprint5.task2.fase3.sql.dto.Userdto;
import com.sprint5.task2.fase3.sql.entity.User;
import com.sprint5.task2.fase3.sql.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    public ModelMapper modelMapper(){return new ModelMapper();}


    @Override
    public Userdto findById(int id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            log.info("player found with id: " + id);
            return entityToDto(user.get());
        }else {
            log.info("No player was found with id: " + id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No player was found with id: " + id);
        }
    }

    @Override
    public List<User> findAll() {

        return userRepository.findAll();
    }

    /**
     * This method search for the User into the database
     * @param name
     * @return Userdto
     * @throws ResponseStatusException
     */
    @Override
    public Optional<User> findByName(String name) { //throws ResponseStatusException {
        return userRepository.findByName(name);
    }

    @Override
    public Optional<User> findByEmail(String email) { //throws ResponseStatusException {
        return userRepository.findByEmail(email);
    }


    /**
     * This method transform an entity into a DTO
     * @param User
     * @return Userdto
     */
    @Override
    public Userdto entityToDto(User User) {
        Userdto Userdto = modelMapper().map(User, Userdto.class);
        if(Userdto.getName().equals("")){
            Userdto.setName("Anonymous");
        }
        return Userdto;
    }
    /**
     * This method recives a DTO object to transform it into an entity
     * @param Userdto
     * @return User
     */
    @Override
    public User dtoToEntity(Userdto Userdto) {
        User User = modelMapper().map(Userdto, User.class);
        return User;
    }
}
