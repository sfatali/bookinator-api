package com.bookinator.api.controller;

import com.bookinator.api.controller.helpers.GeneralHelper;
import com.bookinator.api.dao.UserDAO;
import com.bookinator.api.model.dto.UserProfile;
import com.bookinator.api.resources.ErrorResource;
import com.bookinator.api.resources.UserResource;
import com.bookinator.api.resources.util.CustomLink;
import com.bookinator.api.resources.util.CustomLinkWithRequestTemplate;
import com.bookinator.api.resources.util.CustomLinkWithUrlTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Sabina on 6/15/2018.
 */
@RestController
@RequestMapping(value = "/")
public class UserProfileController {
    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "{username}/profile", method = RequestMethod.GET)
    HttpEntity getProfile(@PathVariable("username") String username,
                          @RequestHeader("Authorization") String token) {
        if(!GeneralHelper.isPathUsernameValid(username)) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "Malformed URL: no username", true),
                    HttpStatus.BAD_REQUEST);
        }
        // input is valid, getting the data now
        UserProfile user;
        try {
            if(userDAO.countByUsername(username) == 0) {
                // not found (HTTP 404)
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(404, "Not found",
                                "User does not exist", true),
                        HttpStatus.NOT_FOUND);
            }
            user = userDAO.getUserProfile(userDAO.getIdByUsername(username));
        } catch (Exception ex) {
            // internal server error (HTTP 500)
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource
                            (500, "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // setting up the resource
        UserResource userResource = new UserResource();
        userResource.setUserId(user.getId());
        userResource.setName(user.getName());
        userResource.setSurname(user.getSurname());
        userResource.setCity(user.getCity());
        userResource.setEmail(user.getEmail());
        userResource.setPhone(user.getPhone());
        userResource.setScore(user.getScore());
        CustomLink self = new CustomLink(
                linkTo(methodOn(UserProfileController.class)
                        .getProfile(username, token))
                        .toString(), "self", "GET", true);
        userResource.add(self);

        // home link:
        CustomLink homeLink = new CustomLink(
                linkTo(methodOn(HomeController.class).goHome(token, username))
                        .toString(),
                "home", "GET", true);
        userResource.add(homeLink);

        if(GeneralHelper.isUserAccessingOwnResources(token, username)) {
            // adding edit link
            CustomLinkWithRequestTemplate editLink = new CustomLinkWithRequestTemplate(
                    linkTo(methodOn(UserProfileController.class)
                            .getProfile(username, token))
                            .toString(), "edit-profile", "PUT", true);
            editLink.setRequestTemplate(UserController.getEditProfileTemplate());
            userResource.add(editLink);
        }

        // adding get reviews link
        CustomLink revLink = new CustomLink(
                linkTo(methodOn(ReviewController.class).getUserReviews(token, username))
                        .toString(),
                "reviews", "GET", true);
        userResource.add(revLink);

        return new ResponseEntity<UserResource>(userResource, HttpStatus.OK);
    }
}
