package com.bookinator.api.controller;

import com.bookinator.api.controller.helpers.BookRequestsHelper;
import com.bookinator.api.controller.helpers.BooksHelper;
import com.bookinator.api.controller.helpers.GeneralHelper;
import com.bookinator.api.controller.helpers.WishlistHelper;
import com.bookinator.api.dao.BookDAO;
import com.bookinator.api.dao.UserDAO;
import com.bookinator.api.model.dto.ExploreRequest;
import com.bookinator.api.model.dto.ExploreResponse;
import com.bookinator.api.resources.BookFilterResource;
import com.bookinator.api.resources.ErrorResource;
import com.bookinator.api.resources.util.CustomLink;
import com.bookinator.api.resources.util.CustomLinkWithRequestTemplate;
import com.bookinator.api.resources.util.CustomLinkWithUrlTemplate;
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
 * Created by Sabina on 6/14/2018.
 */
@RestController
@RequestMapping(value = "/")
public class ExploreController {
    @Autowired
    private BookDAO bookDAO;
    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "/explore", method = RequestMethod.GET/*, produces ={"application/collection+json"}*/)
    HttpEntity filterBooks(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "author", required = false) String author,
                           @RequestParam(value = "fieldId", required = false) String fieldId,
                           @RequestParam(value = "yearPublished", required = false) String yearPublished,
                           @RequestParam(value = "cityId", required = false) String cityId,
                           @RequestParam(value = "topic", required = false) String topic) {
        ExploreRequest request = new ExploreRequest();
        if(name != null && name.length() != 0) {
            request.setName(name);
        }
        if(author != null && author.length() != 0) {
            request.setAuthor(author);
        }
        if(topic != null && topic.length() != 0) {
            request.setTopic(topic);
        }
        if(fieldId != null && fieldId.length() != 0) {
            try {
                request.setFieldId(Integer.parseInt(fieldId));
            } catch (Exception ex) {
                // parsing error (HTTP 400)
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(400, "Bad Request", ex.getMessage(), false),
                        HttpStatus.BAD_REQUEST);
            }
        }
        if(yearPublished != null && yearPublished.length() != 0) {
            try {
                request.setYearPublished(Integer.parseInt(yearPublished));
            } catch (Exception ex) {
                // parsing error (HTTP 400)
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(400, "Bad Request", ex.getMessage(), false),
                        HttpStatus.BAD_REQUEST);
            }
        }
        if(cityId != null && cityId.length() != 0) {
            try {
                request.setCityId(Integer.parseInt(cityId));
            } catch (Exception ex) {
                // parsing error (HTTP 400)
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(400, "Bad Request", ex.getMessage(), false),
                        HttpStatus.BAD_REQUEST);
            }
        }
        // everything is validated, let's get the data now
        List<ExploreResponse> books;
        try {
            books = bookDAO.filterBooks(request);
            if(books == null || books.size() == 0) {
                // no resources found error (HTTP 404)
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(404, "Not found",
                                "No books found with your filter criteria.", false),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            // internal server error (HTTP 500)
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource
                            (500, "Internal server error", ex.getMessage(), false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ResourceWithEmbeddedGenericSupport resource = new ResourceWithEmbeddedGenericSupport();
        List<BookFilterResource> bookFilterResourceList = new ArrayList<>();
        for(ExploreResponse book : books) {
            BookFilterResource bookResource = getBookFilterResource(book);
            bookFilterResourceList.add(bookResource);
        }

        CustomLinkWithUrlTemplate selfLink = new CustomLinkWithUrlTemplate(
                linkTo(methodOn(ExploreController.class)
                        .filterBooks(name, author, fieldId, yearPublished, cityId, topic))
                        .toString(), "self", "GET", false);
        selfLink.setUrlTemplate(BooksHelper.getFilterBooksTemplate());
        resource.add(selfLink);

        CustomLink welcome = new CustomLink(linkTo(WelcomeLinksController.class)
                .slash("/welcome").toString(),"welcome","GET",false);
        welcome.setTitle("Welcome to Bookinator!");
        welcome.setDescription("You can register, login or explore the books");
        resource.add(welcome);

        resource.embedResource("books", bookFilterResourceList);
        return new ResponseEntity<ResourceWithEmbeddedGenericSupport>(resource, HttpStatus.OK);
    }

    @RequestMapping(value = "{username}/explore", method = RequestMethod.GET)
    HttpEntity authFilterBooks(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "author", required = false) String author,
                           @RequestParam(value = "fieldId", required = false) String fieldId,
                           @RequestParam(value = "yearPublished", required = false) String yearPublished,
                           @RequestParam(value = "cityId", required = false) String cityId,
                           @RequestParam(value = "topic", required = false) String topic,
                               @PathVariable("username") String username,
                               @RequestHeader("Authorization") String token) {
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

        ExploreRequest request = new ExploreRequest();
        if(name != null && name.length() != 0) {
            request.setName(name);
        }
        if(author != null && author.length() != 0) {
            request.setAuthor(author);
        }
        if(topic != null && topic.length() != 0) {
            request.setTopic(topic);
        }
        if(fieldId != null && fieldId.length() != 0) {
            try {
                request.setFieldId(Integer.parseInt(fieldId));
            } catch (Exception ex) {
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(400, "Bad Request", ex.getMessage(), true),
                        HttpStatus.BAD_REQUEST);
            }
        }
        if(yearPublished != null && yearPublished.length() != 0) {
            try {
                request.setYearPublished(Integer.parseInt(yearPublished));
            } catch (Exception ex) {
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(400, "Bad Request", ex.getMessage(), true),
                        HttpStatus.BAD_REQUEST);
            }
        }
        if(cityId != null && cityId.length() != 0) {
            try {
                request.setCityId(Integer.parseInt(cityId));
            } catch (Exception ex) {
                // parsing error (HTTP 400)
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(400, "Bad Request", ex.getMessage(), true),
                        HttpStatus.BAD_REQUEST);
            }
        }
        // everything is validated, let's get the data now
        List<ExploreResponse> books;
        try {
            books = bookDAO.filterBooks(request);
            if(books == null || books.size() == 0) {
                // no resources found error (HTTP 404)
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(404, "Not found",
                                "No books found with your filter criteria.", true),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            // internal server error (HTTP 500)
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource
                            (500, "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ResourceWithEmbeddedGenericSupport resource = new ResourceWithEmbeddedGenericSupport();
        List<BookFilterResource> bookFilterResourceList = new ArrayList<>();
        for(ExploreResponse book : books) {
            BookFilterResource bookResource = getBookFilterResource(book);
            // checking if the book belongs to user
            if(book.getOwnerId() != GeneralHelper.getUserIdFromToken(token)) {
                // link relations to add to wishlist
                CustomLinkWithRequestTemplate wishlistLink = new CustomLinkWithRequestTemplate(
                        linkTo(methodOn(WishlistController.class)
                                .addToWishlist(null, username, token))
                                .toString(), "add-to-wishlist", "POST", true);
                wishlistLink.setRequestTemplate(WishlistHelper.getWishlistUrlTemplate());
                bookResource.add(wishlistLink);

                // link relations to add to request the book
                if(book.getStatus().equals("Available") || book.getStatus().equals("In search")) {
                    CustomLinkWithRequestTemplate reqLink = new CustomLinkWithRequestTemplate(
                            linkTo(methodOn(BookRequestController.class)
                                    .makeRequest(token, username, null))
                                    .toString(), "make-request", "POST", true);
                    reqLink.setRequestTemplate(BookRequestsHelper.getBookRequestsTemplate());
                    bookResource.add(reqLink);
                }
            }
            bookFilterResourceList.add(bookResource);
        }
        CustomLinkWithUrlTemplate selfLink = new CustomLinkWithUrlTemplate(
                linkTo(methodOn(ExploreController.class)
                        .authFilterBooks(name, author, fieldId, yearPublished, cityId, topic, username, token))
                        .toString(), "self", "GET", true);
        selfLink.setUrlTemplate(BooksHelper.getFilterBooksTemplate());
        resource.add(selfLink);
        CustomLink homeLink = new CustomLink(
                linkTo(methodOn(HomeLinksController.class).goHome(token, username))
                        .toString(),
                "home", "GET", true);
        resource.add(homeLink);
        resource.embedResource("books", bookFilterResourceList);
        return new ResponseEntity<ResourceWithEmbeddedGenericSupport>(resource, HttpStatus.OK);
    }

    public static BookFilterResource getBookFilterResource(ExploreResponse book) {
        BookFilterResource bookResource = new BookFilterResource();
        CustomLink self = new CustomLink(linkTo(BookController.class).slash("/books/"
                +book.getId()).toString(),
                "self", "GET", false);
        bookResource.add(self);
        bookResource.setBookId(book.getId());
        if(!book.getAuthors().equals(""))
            bookResource.setAuthors(book.getAuthors().split(";"));
        else
            bookResource.setAuthors(new String[0]);
        bookResource.setOwnerId(book.getOwnerId());
        bookResource.setOwnerAvgRating(book.getOwnerAvgRating());
        bookResource.setDescription(book.getDescription());
        bookResource.setField(book.getField());
        bookResource.setName(book.getName());
        bookResource.setStatus(book.getStatus());
        bookResource.setYearPublished(book.getYearPublished());
        if(!book.getTopics().equals(""))
            bookResource.setTopics(book.getTopics().split(";"));
        else
            bookResource.setTopics(new String[0]);
        bookResource.setHoldingType(book.getHoldingType());
        bookResource.setCity(book.getCity());
        return bookResource;
    }
}
