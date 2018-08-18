package com.bookinator.api.controller;

import com.bookinator.api.controller.helpers.GeneralHelper;
import com.bookinator.api.dao.UserDAO;
import com.bookinator.api.model.User;
import com.bookinator.api.model.dto.UserProfile;
import com.bookinator.api.resources.ErrorResource;
import com.bookinator.api.resources.UserResource;
import com.bookinator.api.resources.util.RequestTemplateItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Sabina on 6/14/2018.
 *
 * Covers User resource
 */
@RestController
@RequestMapping(value = "/")
public class UserController {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Create User
     * @param user
     * @return Empty response
     */
    @RequestMapping(value = "user", method = RequestMethod.POST)
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

    /**
     * Edit user
     * @param user
     * @param username
     * @param token
     * @return Empty response
     */
    @RequestMapping(value = "{username}/user", method = RequestMethod.PUT)
    HttpEntity updateUser(@RequestBody User user,
                          @PathVariable("username") String username,
                          @RequestHeader("Authorization") String token) {
        if(!GeneralHelper.isUserAccessingOwnResources(token, username)) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(403, "Forbidden",
                            "You cannot edit someone else's profile", true),
                    HttpStatus.FORBIDDEN);
        }
        if(user.getId() == 0) {
            user.setId((int) GeneralHelper.getUserIdFromToken(token));
        }
        if(user.getName() == null || user.getName().length() == 0) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "Name cannot be empty", true),
                    HttpStatus.BAD_REQUEST);
        }
        if(user.getSurname() == null || user.getSurname().length() == 0) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "Surname cannot be empty", true),
                    HttpStatus.BAD_REQUEST);
        }
        if(user.getCityId() == 0) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "City cannot be empty", true),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            User userFromDb = userDAO.getById(user.getId());
            if(userFromDb == null) {
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(404, "Not found",
                                "User with that ID does not exist", true),
                        HttpStatus.NOT_FOUND);

            }
            if(!userFromDb.getUsername().equals(username)) {
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(403, "Forbidden",
                                "You cannot edit someone else's profile", true),
                        HttpStatus.FORBIDDEN);
            }
            userDAO.update(user);
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(500, "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(methodOn(UserProfileController.class)
                .getProfile(username, token)).toUri());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    public static List<RequestTemplateItem> getEditProfileTemplate() {
        List<RequestTemplateItem> template = new ArrayList<>();
        // fields required for edit:
        RequestTemplateItem name = GeneralHelper.getTemplateItem("name", "String", true,
                2, 50);
        template.add(name);

        RequestTemplateItem surname = GeneralHelper.getTemplateItem("surname", "String", true,
                2, 50);
        template.add(surname);

        RequestTemplateItem cityId = GeneralHelper.getTemplateItem("cityId", "int", true,
                1, null);
        template.add(cityId);

        RequestTemplateItem email = GeneralHelper.getTemplateItem("email", "String", true,
                1, null);
        template.add(email);

        RequestTemplateItem phone = GeneralHelper.getTemplateItem("phone", "String", true,
                1, null);
        template.add(phone);
        return template;
    }

}
