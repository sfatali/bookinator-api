package com.bookinator.api.controller;

import com.bookinator.api.controller.helpers.BooksHelper;
import com.bookinator.api.controller.helpers.GeneralHelper;
import com.bookinator.api.model.Wishlist;
import com.bookinator.api.model.dto.UserProfile;
import com.bookinator.api.resources.ErrorResource;
import com.bookinator.api.resources.MenuResource;
import com.bookinator.api.resources.util.CustomLink;
import com.bookinator.api.resources.util.CustomLinkWithUrlTemplate;
import com.bookinator.api.resources.util.ResourceWithEmbeddedSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Sabina on 6/12/2018.
 */
@RestController
@RequestMapping(value = "/")
public class HomeController {
    @RequestMapping(value = "{username}/home", method = RequestMethod.GET, produces ={"application/hal+json"})
    public HttpEntity goHome(@RequestHeader("Authorization") String token,
                      @PathVariable("username") String username) {
        // Checking username if is valid
        if(!GeneralHelper.isPathUsernameValid(username)) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "Malformed URL: no username", true),
                    HttpStatus.BAD_REQUEST);
        }

        // Checking if username belongs to user
        if(!GeneralHelper.isUserAccessingOwnResources(token, username)) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(403, "Forbidden",
                            "You should use your username to access this resource", true),
                    HttpStatus.FORBIDDEN);
        }

        // Explore books:
        MenuResource exploreItem = new MenuResource();
        exploreItem.setTitle("Explore books");
        exploreItem.setDescription("Use filter to get the most accurate results");
        CustomLinkWithUrlTemplate expLink = new CustomLinkWithUrlTemplate(
                linkTo(methodOn(ExploreController.class)
                        .authFilterBooks(null, null, null, null,
                                null, null, username, token))
                        .toString(), "self", "GET", true);
        expLink.setUrlTemplate(BooksHelper.getFilterBooksTemplate());
        exploreItem.add(expLink);

        // Your book posts:
        MenuResource yourBooksItem = new MenuResource();
        yourBooksItem.setTitle("Book posts");
        yourBooksItem.setDescription("Books user possesses or is in search for");
        CustomLink ybLink = new CustomLink(
                linkTo(methodOn(BookController.class)
                        .getUsersBooks(username, token))
                        .toString(), "self", "GET", true);
        yourBooksItem.add(ybLink);

        // view your wishlist:
        MenuResource wishlistItem = new MenuResource();
        wishlistItem.setTitle("Wishlist");
        wishlistItem.setDescription("Books user wishes or plans to take");
        CustomLink wLink = new CustomLink(
                linkTo(methodOn(UserWishlistController.class)
                        .getUserWishlist(username, token))
                        .toString(), "self", "GET", true);
        wishlistItem.add(wLink);

        // view profile:
        MenuResource profileItem = new MenuResource();
        profileItem.setTitle("User profile");
        profileItem.setDescription("User personal information, plus overall rating");
        CustomLink pLink = new CustomLink(
                linkTo(methodOn(UserProfileController.class)
                        .getProfile(username, token))
                        .toString(), "self", "GET", true);
        profileItem.add(pLink);

        // view book requests:
        MenuResource reqItem = new MenuResource();
        reqItem.setTitle("Book requests");
        reqItem.setDescription("See latest requests related to your book posts");
        CustomLink rLink = new CustomLink(
                linkTo(methodOn(UserBookRequestsController.class)
                        .getRequests(token, username))
                        .toString(), "self", "GET", true);
        reqItem.add(rLink);

        // view book holdings:
        MenuResource holdItem = new MenuResource();
        holdItem.setTitle("Book holdings");
        holdItem.setDescription("See the list of approved book requests");
        CustomLink hLink = new CustomLink(
                linkTo(methodOn(UserBookHoldingsController.class)
                        .getHoldings(token, username))
                        .toString(), "self", "GET", true);
        holdItem.add(hLink);

        // log out (basically, just go to to api entry):
        MenuResource logoutItem = new MenuResource();
        logoutItem.setTitle("Log out");
        logoutItem.setDescription("");
        CustomLink logLink = new CustomLink(
                linkTo(methodOn(WelcomeController.class)
                        .getApiEntry())
                        .toString(), "self", "GET", false);
        logoutItem.add(logLink);

        // Wrap all options in embedded:
        ResourceWithEmbeddedSupport resource = new ResourceWithEmbeddedSupport();
        CustomLink selfLink = new CustomLink(
                linkTo(methodOn(HomeController.class)
                        .goHome(token, username))
                        .toString(), "self", "GET", true);
        resource.add(selfLink);
        resource.embedResource("explore", exploreItem);
        resource.embedResource("book-posts", yourBooksItem);
        resource.embedResource("wishlist", wishlistItem);
        resource.embedResource("profile", profileItem);
        resource.embedResource("book-requests", reqItem);
        resource.embedResource("book-holdings", holdItem);
        resource.embedResource("logout", logoutItem);
        return new ResponseEntity<ResourceWithEmbeddedSupport>(resource, HttpStatus.OK);
    }
}
