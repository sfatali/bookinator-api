package com.bookinator.api.controller;

import com.bookinator.api.controller.helpers.BookRequestsHelper;
import com.bookinator.api.controller.helpers.BooksHelper;
import com.bookinator.api.controller.helpers.GeneralHelper;
import com.bookinator.api.controller.helpers.WishlistHelper;
import com.bookinator.api.dao.WishlistDAO;
import com.bookinator.api.model.Wishlist;
import com.bookinator.api.model.dto.BookFilterResponse;
import com.bookinator.api.resources.BookFilterResource;
import com.bookinator.api.resources.ErrorResource;
import com.bookinator.api.resources.util.*;
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
public class UserWishlistController {
    @Autowired
    private WishlistDAO wishlistDAO;

    @RequestMapping(value = "{username}/wishlist", method = RequestMethod.GET, produces ={"application/hal+json"})
    HttpEntity getUserWishlist(@PathVariable("username") String username,
                               @RequestHeader("Authorization") String token) {
        if(!GeneralHelper.isPathUsernameValid(username)) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "Malformed URL: no username", true),
                    HttpStatus.BAD_REQUEST);
        }
        // checking that user is accessing their own wishlist:
        if(!GeneralHelper.isUserAccessingOwnResources(token, username)) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(403, "Forbidden",
                            "You cannot access someone else's wishlist.", true),
                    HttpStatus.FORBIDDEN);
        }
        List<BookFilterResponse> wishlistBooks;
        try {
            // getting user id from token
            int userId = (int) GeneralHelper.getUserIdFromToken(token);
            // getting wishlist now:
            wishlistBooks = wishlistDAO.getUserWishlist(userId);
            if(wishlistBooks == null || wishlistBooks.size() == 0) {
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(404, "Not found",
                                "No books in your wishlist. Go ahead and explore more!", true),
                        HttpStatus.NOT_FOUND);
            }
        }  catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource
                            (500, "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<BookFilterResource> bookResources = new ArrayList<>();
        for (BookFilterResponse book : wishlistBooks) {
            BookFilterResource bookResource = ExploreController.getBookFilterResource(book);
            CustomLinkWithUrlTemplate removeLink = new CustomLinkWithUrlTemplate(
                    linkTo(methodOn(UserWishlistController.class)
                            .removeFromWishlist(username, String.valueOf(book.getId()), token)).toString(),
                    "remove", "DELETE", true);
            removeLink.setUrlTemplate(getBookRemoveTemplate());
            bookResource.add(removeLink);
            CustomLinkWithRequestTemplate reqLink = new CustomLinkWithRequestTemplate(
                    linkTo(methodOn(UserBookRequestsController.class)
                            .makeRequest(token, username, null))
                            .toString(), "make-request", "POST", true);
            reqLink.setRequestTemplate(BookRequestsHelper.getBookRequestsTemplate());
            bookResource.add(reqLink);
            bookResources.add(bookResource);
        }

        ResourceWithEmbeddedGenericSupport resource = new ResourceWithEmbeddedGenericSupport();
        resource.embedResource("books", bookResources);
        CustomLink selfLink = new CustomLink(
                linkTo(methodOn(UserWishlistController.class)
                        .getUserWishlist(username, token)).toString(), "self", "GET", true);
        //selfLink.setRequestTemplate(WishlistHelper.getWishlistUrlTemplate());
        resource.add(selfLink);
        CustomLink homeLink = new CustomLink(
                linkTo(methodOn(HomeController.class).goHome(token, username))
                .toString(),
                "home", "GET", true);
        resource.add(homeLink);
        return new ResponseEntity<ResourceWithEmbeddedGenericSupport>(resource, HttpStatus.OK);
    }

    @RequestMapping(value = "{username}/wishlist", method = RequestMethod.POST)
    public HttpEntity addToWishlist(@RequestBody Wishlist wishlist,
                             @PathVariable("username") String username,
                             @RequestHeader("Authorization") String token) {
        if(wishlist.getBookId() == 0) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400,
                            "Bad Request", "Book must be specified", true),
                    HttpStatus.BAD_REQUEST);
        }
        int userId = (int) GeneralHelper.getUserIdFromToken(token);
        if (userId != wishlist.getUserId()) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(403,
                            "Bad Request", "You cannot add a book to someone else's wishlist.", true),
                    HttpStatus.FORBIDDEN);
        }
        try {
            // checking if the book is already there
            if(wishlistDAO.count(wishlist) > 0) {
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(409, "Conflict",
                                "Book is already in wishlist", false),
                        HttpStatus.CONFLICT);
            }
            wishlistDAO.addToWishlist(wishlist);
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(500,
                            "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(BookController.class).slash("/books/"+wishlist.getBookId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{username}/wishlist/{bookId}", method = RequestMethod.DELETE)
    HttpEntity removeFromWishlist(@PathVariable("username") String username,
                                  @PathVariable("bookId") String bookIdStr,
                                  @RequestHeader("Authorization") String token) {
        int bookId;
        try {
            bookId = Integer.parseInt(bookIdStr);
        } catch (Exception ex) {
            // parsing error (HTTP 400)
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request", ex.getMessage(), true),
                    HttpStatus.BAD_REQUEST);
        }
        Wishlist wishlist = new Wishlist();
        wishlist.setUserId((int) GeneralHelper.getUserIdFromToken(token));
        wishlist.setBookId(bookId);
        try {
            // check if the book is really in wishlist
            if (wishlistDAO.count(wishlist) == 0) {
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(404, "Not found",
                                "Book with that ID does not exist in user's wishlist.", true),
                        HttpStatus.NOT_FOUND);
            }
            wishlistDAO.removeFromWishlist(wishlist);
            // removing
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(500, "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = new HttpHeaders(); // any headers?
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    public List<UrlTemplateItem> getBookRemoveTemplate() {
        List<UrlTemplateItem> urlTemplate = new ArrayList<>();
        UrlTemplateItem bookId = new UrlTemplateItem("bookId", "int", true);
        urlTemplate.add(bookId);
        return urlTemplate;
    }
}
