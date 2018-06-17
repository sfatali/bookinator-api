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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Sabina on 6/14/2018.
 */
@RestController
@RequestMapping(value = "/")
public class UserController {
    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "{username}/profile", method = RequestMethod.PUT)
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
