package com.pranav.rest.webservices.restfulwebservices.controller;

import com.pranav.rest.webservices.restfulwebservices.dao.UserDaoService;
import com.pranav.rest.webservices.restfulwebservices.exception.UserNotFoundException;
import com.pranav.rest.webservices.restfulwebservices.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.Resource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Locale;

@RestController
public class UserResource {

  @Autowired
  UserDaoService userDaoService;

  @Autowired
  private MessageSource messageSource;

  @GetMapping("/users")
  public List<User> retrieveAllUsers(){
    return userDaoService.findAll();
  }

  @GetMapping("/users-internationalization")
  public String userInternationlization(){
    return messageSource.getMessage("good.morning.message",null, LocaleContextHolder.getLocale());
  }


  @GetMapping("/users/{id}")
  public Resource<User> retrieveUser(@PathVariable int id){
    User user = userDaoService.findOne(id);
    if (user == null){
        throw new UserNotFoundException("id-"+id);
    }
    Resource<User> userResource = new Resource<User>(user);

    ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
    userResource.add(linkTo.withRel("all-user"));
    return userResource;
  }

  @PostMapping("/users")
  public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
    User savedUser = userDaoService.save(user);
    URI location = ServletUriComponentsBuilder
      .fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(savedUser.getId())
      .toUri();

    return ResponseEntity.created(location).build();
  }

  @DeleteMapping("/users/{id}")
  public void deleteUser(@PathVariable int id) {
    User user = userDaoService.deletebyId(id);
    if(user==null)
      throw new UserNotFoundException("id");
  }

}
