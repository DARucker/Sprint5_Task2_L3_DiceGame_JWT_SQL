package com.sprint5.task2.fase3.sql.service;

import com.sprint5.task2.fase3.sql.dto.Userdto;
import com.sprint5.task2.fase3.sql.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    Userdto findById(int id);
    List<User> findAll();
    //Userdto update(Userdto Userdto);

    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Userdto entityToDto(User User);
    User dtoToEntity(Userdto Userdto);

}
