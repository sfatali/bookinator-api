package com.bookinator.api.controller;

import com.bookinator.api.controller.helpers.GeneralHelper;
import com.bookinator.api.dao.UserDAO;
import com.bookinator.api.model.User;
import com.bookinator.api.resources.ErrorResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Sabina on 6/14/2018.
 */
@RestController
@RequestMapping(value = "/")
public class RegistrationController {
    @Autowired
    public UserDAO userDAO;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "register", method = RequestMethod.POST)
    HttpEntity register(@RequestBody User user) {
        if(user.getUsername() == null || user.getUsername().length() == 0) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "Username cannot be empty", false),
                    HttpStatus.BAD_REQUEST);
        } else if(user.getUsername().length() < 5) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "Username should be at least 5 characters long", false),
                    HttpStatus.BAD_REQUEST);
        } else {
            int count = userDAO.countByUsername(user.getUsername());
            if(count != 0) {
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(409, "Conflict",
                                "Username is already taken, choose another one", false),
                        HttpStatus.CONFLICT);
            }
        }
        if(user.getPassword() == null || user.getPassword().length() == 0) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "Password cannot be empty", false),
                    HttpStatus.BAD_REQUEST);
        } else if(user.getPassword().length() < 8) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "Password should be at least 8 characters long", false),
                    HttpStatus.BAD_REQUEST);
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        if(user.getName() == null || user.getName().length() == 0) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "Name cannot be empty", false),
                    HttpStatus.BAD_REQUEST);
        }
        if(user.getSurname() == null || user.getSurname().length() == 0) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "Surname cannot be empty", false),
                    HttpStatus.BAD_REQUEST);
        }
        if(user.getCityId() == 0) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "City cannot be empty", false),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            userDAO.create(user);
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(500,
                            "Internal server error", ex.getMessage(), false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = new HttpHeaders();
        //headers.setLocation(linkTo(UserProfileController.class).slash(user.getId()).toUri());
        headers.setLocation(linkTo(methodOn(UserProfileController.class)
                .getProfile(String.valueOf(user.getUsername()), null)).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
