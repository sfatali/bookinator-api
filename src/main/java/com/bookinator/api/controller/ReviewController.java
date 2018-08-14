package com.bookinator.api.controller;

import com.bookinator.api.controller.helpers.GeneralHelper;
import com.bookinator.api.controller.helpers.ReviewHelper;
import com.bookinator.api.dao.ReviewDAO;
import com.bookinator.api.dao.UserDAO;
import com.bookinator.api.model.Review;
import com.bookinator.api.resources.BookHoldingResource;
import com.bookinator.api.resources.ErrorResource;
import com.bookinator.api.resources.ReviewResource;
import com.bookinator.api.resources.util.CustomLink;
import com.bookinator.api.resources.util.ResourceWithEmbeddedGenericSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Sabina on 6/17/2018.
 */
@RestController
@RequestMapping(value = "/")
public class ReviewController {
    @Autowired
    private ReviewDAO reviewDAO;
    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "{username}/reviews", method = RequestMethod.GET,
            produces ={"application/hal+json"})
    public HttpEntity getUserReviews(@RequestHeader("Authorization") String token,
                             @PathVariable("username") String username) {
        List<com.bookinator.api.model.dto.Review> reviews;
        try {
            int id;
            if(!GeneralHelper.isUserAccessingOwnResources(token, username)) {
                id = userDAO.getIdByUsername(username);
            } else {
                id = (int) GeneralHelper.getUserIdFromToken(token);
            }
            reviews = reviewDAO.getUserReviews(id);
            if (reviews == null || reviews.size() == 0) {
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(404, "Not found",
                                "No reviews found.", true),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(500, "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<ReviewResource> revResources = new ArrayList<>();
        for(com.bookinator.api.model.dto.Review review : reviews) {
            ReviewResource revResource = ReviewHelper.getReviewResource(review);
            revResources.add(revResource);
        }

        ResourceWithEmbeddedGenericSupport resource = new ResourceWithEmbeddedGenericSupport();
        CustomLink self = new CustomLink(linkTo(methodOn(ReviewController.class)
                .getUserReviews(token, username))
                .toString(), "self", "GET", true);
        resource.add(self);

        // user profile:
        CustomLink profileLink = new CustomLink(linkTo(methodOn(UserProfileController.class)
                .getProfile(username, token))
                .toString(), "profile", "GET", true);
        resource.add(profileLink);


        // home:
        CustomLink homeLink;
        if(!GeneralHelper.isUserAccessingOwnResources(token, username)) {
            homeLink = new CustomLink(
                    linkTo(methodOn(HomeLinksController.class)
                            .goHome(token, userDAO.getById((int) GeneralHelper.getUserIdFromToken(token)).getUsername()))
                            .toString(),
                    "home", "GET", true);
        } else {
            homeLink = new CustomLink(
                    linkTo(methodOn(HomeLinksController.class).goHome(token, username))
                            .toString(),
                    "home", "GET", true);
        }

        resource.add(homeLink);
        resource.embedResource("reviews", revResources);
        return new ResponseEntity<ResourceWithEmbeddedGenericSupport>(resource, HttpStatus.OK);
    }

    @RequestMapping(value = "{username}/reviews", method = RequestMethod.POST, produces ={"application/hal+json"})
    public HttpEntity addUserReview(@RequestHeader("Authorization") String token,
                                    @PathVariable("username") String username,
                                     @RequestBody Review review) {

        return null;
    }
}
