package com.pranav.rest.webservices.restfulwebservices.controller;

import com.pranav.rest.webservices.restfulwebservices.dao.UserDaoService;
import com.pranav.rest.webservices.restfulwebservices.exception.UserNotFoundException;
import com.pranav.rest.webservices.restfulwebservices.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;

@RestController
public class UserResource {

  @Autowired
  UserDaoService userDaoService;

  @GetMapping("/users")
  public List<User> retrieveAllUsers(){
    return userDaoService.findAll();
  }

  @GetMapping("/users/{id}")
  public User retrieveUser(@PathVariable int id){
    User user = userDaoService.findOne(id);
    if (user == null){
        throw new UserNotFoundException("id-"+id);
    }
    return user;
  }

  @PostMapping("/users")
  public ResponseEntity<Object> createUser(@RequestBody User user){
    User savedUser = userDaoService.save(user);
    URI location = ServletUriComponentsBuilder
      .fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(savedUser.getId())
      .toUri();

    return ResponseEntity.created(location).build();
  }
}
