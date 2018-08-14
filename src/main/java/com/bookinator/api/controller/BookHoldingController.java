package com.bookinator.api.controller;

import com.bookinator.api.controller.helpers.BookRequestsHelper;
import com.bookinator.api.controller.helpers.GeneralHelper;
import com.bookinator.api.dao.BookHoldingsDAO;
import com.bookinator.api.model.dto.BookRequest;
import com.bookinator.api.resources.BookHoldingResource;
import com.bookinator.api.resources.ErrorResource;
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
 * Created by Sabina on 6/15/2018.
 */
@RestController
@RequestMapping(value = "/")
public class BookHoldingController {

    @Autowired
    private BookHoldingsDAO bookHoldingsDAO;

    @RequestMapping(value = "{username}/holdings", method = RequestMethod.GET, produces ={"application/hal+json"})
    HttpEntity getHoldings(@RequestHeader("Authorization") String token,
                           @PathVariable("username") String username) {
        if(!GeneralHelper.isUserAccessingOwnResources(token, username)) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(403, "Forbidden",
                            "You cannot access someone else's book requests", true),
                    HttpStatus.FORBIDDEN);
        }
        List<BookRequest> holdings;
        try {
            int id = (int) GeneralHelper.getUserIdFromToken(token);
            holdings = bookHoldingsDAO.getApprovedRequests(id);
            if (holdings == null || holdings.size() == 0) {
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(404, "Not found",
                                "You have no requests regarding your book posts.", true),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(500, "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<BookHoldingResource> holdResources = new ArrayList<>();
        for(BookRequest holding : holdings) {
            BookHoldingResource res = BookRequestsHelper.getHoldingResource(holding);
            CustomLink self = new CustomLink(linkTo(methodOn(BookRequestController.class)
                    .getRequest(token, username, String.valueOf(holding.getId())))
                    .toString(), "self", "GET", true);
            res.add(self);
            // add a review:
            /*CustomLink review = new CustomLink(linkTo(methodOn(ReviewController.class)
                    .addUserReview(token, holding.getSender().getName(), null))
                    .toString(), "add-review", "POST", true);
            res.add(review);
            holdResources.add(res);*/
        }
        ResourceWithEmbeddedGenericSupport resource = new ResourceWithEmbeddedGenericSupport();
        CustomLink selfLink = new CustomLink(
                linkTo(methodOn(BookHoldingController.class)
                        .getHoldings(token, username))
                        .toString(), "self", "GET", true);
        resource.add(selfLink);
        CustomLink homeLink = new CustomLink(
                linkTo(methodOn(HomeLinksController.class).goHome(token, username))
                        .toString(),
                "home", "GET", true);
        resource.add(homeLink);
        resource.embedResource("holdings", holdResources);
        return new ResponseEntity<ResourceWithEmbeddedGenericSupport>(resource, HttpStatus.OK);
    }
}
