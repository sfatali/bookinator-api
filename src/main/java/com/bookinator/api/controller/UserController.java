package com.bookinator.api.controller;

import com.bookinator.api.dao.UserDAO;
import com.bookinator.api.model.User;
import com.bookinator.api.model.dto.UserProfile;
import com.bookinator.api.resources.ErrorResource;
import com.bookinator.api.resources.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Sabina on 6/14/2018.
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    UserDAO userDAO;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    HttpEntity getUser(@PathVariable("userId") String userIdStr) {
        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (Exception ex) {
            // parsing error (HTTP 400)
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request", ex.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        // input is valid, getting the data now
        UserProfile user;
        try {
            user = userDAO.getUserProfile(userId);
            if(user == null) {
                // not found (HTTP 404)
                return new ResponseEntity<ErrorResource>(
                        ControllerHelper.getErrorResource(404, "Not found",
                                "User with that ID does not exist"),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            // internal server error (HTTP 500)
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource
                            (500, "Internal server error", ex.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // setting up the resource
        UserResource userResource = new UserResource();
        userResource.add(linkTo(methodOn(UserController.class).getUser(userIdStr)).withSelfRel());
        userResource.setUserId(user.getId());
        userResource.setName(user.getName());
        userResource.setSurname(user.getSurname());
        userResource.setCity(user.getCity());
        userResource.setEmail(user.getEmail());
        userResource.setPhone(user.getPhone());
        userResource.setScore(user.getScore());
        return new ResponseEntity<UserResource>(userResource, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    HttpEntity saveUser(@RequestBody User user) {
        if(user.getUsername() == null || user.getUsername().length() == 0) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request",
                            "Username cannot be empty"),
                    HttpStatus.BAD_REQUEST);
        } else if(user.getUsername().length() < 5) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request",
                            "Username should be at least 5 characters long"),
                    HttpStatus.BAD_REQUEST);
        } else {
            int count = userDAO.countByUsername(user.getUsername());
            if(count != 0) {
                return new ResponseEntity<ErrorResource>(
                        ControllerHelper.getErrorResource(400, "Bad Request",
                                "Username is already taken, choose another one"),
                        HttpStatus.BAD_REQUEST);
            }
        }
        if(user.getPassword() == null || user.getPassword().length() == 0) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request",
                            "Password cannot be empty"),
                    HttpStatus.BAD_REQUEST);
        }
        if(user.getName() == null || user.getName().length() == 0) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request",
                            "Name cannot be empty"),
                    HttpStatus.BAD_REQUEST);
        }
        if(user.getSurname() == null || user.getSurname().length() == 0) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request",
                            "Surname cannot be empty"),
                    HttpStatus.BAD_REQUEST);
        }
        if(user.getCityId() == 0) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request",
                            "City cannot be empty"),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            userDAO.create(user);
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(500, "Internal server error", ex.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(BookController.class).slash(user.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    HttpEntity updateUser(@RequestBody User user) {
        if(user.getId() == 0) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request",
                            "User ID must be specified"),
                    HttpStatus.BAD_REQUEST);
        }
        if(user.getName() == null || user.getName().length() == 0) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request",
                            "Name cannot be empty"),
                    HttpStatus.BAD_REQUEST);
        }
        if(user.getSurname() == null || user.getSurname().length() == 0) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request",
                            "Surname cannot be empty"),
                    HttpStatus.BAD_REQUEST);
        }
        if(user.getCityId() == 0) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request",
                            "City cannot be empty"),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            userDAO.update(user);
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(500, "Internal server error", ex.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(UserController.class).slash(user.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

}
