package com.bookinator.api.controller;

import com.bookinator.api.controller.helpers.BookRequestsHelper;
import com.bookinator.api.controller.helpers.BooksHelper;
import com.bookinator.api.controller.helpers.GeneralHelper;
import com.bookinator.api.controller.helpers.WishlistHelper;
import com.bookinator.api.dao.BookDAO;
import com.bookinator.api.dao.UserDAO;
import com.bookinator.api.model.dto.Book;
import com.bookinator.api.model.dto.BookFilterResponse;
import com.bookinator.api.model.dto.BookFilterRequest;
import com.bookinator.api.resources.BookDtoResource;
import com.bookinator.api.resources.BookFilterResource;
import com.bookinator.api.resources.ErrorResource;
import com.bookinator.api.resources.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sabina on 6/10/2018.
 */
@RestController
@RequestMapping(value = "/")
public class BookController {
    @Autowired
    private BookDAO bookDAO;
    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "{username}/books", method = RequestMethod.GET)
    HttpEntity getUsersBooks(@PathVariable("username") String username,
                             @RequestHeader("Authorization") String token) {
        if(!GeneralHelper.isPathUsernameValid(username)) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "Malformed URL: no username", true),
                    HttpStatus.BAD_REQUEST);
        }
        List<Book> books; // getting books
        try{
            if(userDAO.countByUsername(username) == 0) {
                // not found (HTTP 404)
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(404, "Not found",
                                "User does not exist", true),
                        HttpStatus.NOT_FOUND);
            }
            books = bookDAO.getUserBooks(userDAO.getIdByUsername(username));
        } catch(Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource
                            (500, "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // setting up resources now
        List<BookDtoResource> bookResources = new ArrayList<>();
        for (Book book : books) {
            BookDtoResource bookResource = BooksHelper.getBookDtoResource(book);
            if(GeneralHelper.isUserAccessingOwnResources(token, username)) {
                // create/edit/delete book
                // Link for creating book post:
                CustomLinkWithRequestTemplate postLink = new CustomLinkWithRequestTemplate(
                        linkTo(methodOn(BookController.class).saveBook(null, username, token)).toString(), "post-book",
                        "POST", true);
                postLink.setRequestTemplate(BooksHelper.getBookPostTemplate());
                bookResource.add(postLink);
                // Link for editing book post:
                CustomLinkWithRequestTemplate editLink = new CustomLinkWithRequestTemplate(
                        linkTo(methodOn(BookController.class).updateBook(null, username, token, String.valueOf(book.getId())))
                                .toString(),
                        "edit-book", "PUT", true);
                editLink.setRequestTemplate(BooksHelper.getBookPostTemplate());
                bookResource.add(editLink);
                // Link for deleting book post
                CustomLinkWithUrlTemplate deleteLink = new CustomLinkWithUrlTemplate(
                        linkTo(methodOn(BookController.class).deleteBook(String.valueOf(book.getId()), username, token))
                                .toString(), "delete-book",
                        "DELETE", true);
                deleteLink.setUrlTemplate(BooksHelper.getBookDeleteTemplate());
                bookResource.add(deleteLink);
            } else {
                // link relations to add to wishlist
                CustomLinkWithRequestTemplate wishlistLink = new CustomLinkWithRequestTemplate(
                        linkTo(methodOn(UserWishlistController.class)
                                .addToWishlist(null, username, token))
                                .toString(), "add-to-wishlist", "POST", true);
                wishlistLink.setRequestTemplate(WishlistHelper.getWishlistUrlTemplate());
                bookResource.add(wishlistLink);

                // link relations to add to request the book
                if(book.getStatus().equals("Available") || book.getStatus().equals("In search")) {
                    CustomLinkWithRequestTemplate reqLink = new CustomLinkWithRequestTemplate(
                            linkTo(methodOn(UserBookRequestsController.class)
                                    .makeRequest(token, username, null))
                                    .toString(), "make-request", "POST", true);
                    reqLink.setRequestTemplate(BookRequestsHelper.getBookRequestsTemplate());
                    bookResource.add(reqLink);
                }
            }
            // self:
            /*CustomLink self = new CustomLink(linkTo(methodOn(BookController.class)
                    .getBook(String.valueOf(book.getId())))
                    .toString(), "self", "GET", true);
            bookResource.add(self);*/
            bookResources.add(bookResource);
        }

        // wrapping everything into embedded:
        ResourceWithEmbeddedGenericSupport resource = new ResourceWithEmbeddedGenericSupport();
        CustomLink selfLink = new CustomLink(
                linkTo(methodOn(BookController.class)
                        .getUsersBooks(username, token))
                        .toString(), "self", "GET", true);
        resource.add(selfLink);
        CustomLink homeLink = new CustomLink(
                linkTo(methodOn(HomeController.class).goHome(token, username))
                        .toString(),
                "home", "GET", true);
        resource.add(homeLink);
        resource.embedResource("books", bookResources);
        return new ResponseEntity<ResourceWithEmbeddedGenericSupport>(resource, HttpStatus.OK);
    }

    /**
     * Getting a book by ID
     * @param bookIdStr
     * @return
     */
    @RequestMapping(value = "books/{bookId}", method = RequestMethod.GET)
    public HttpEntity getBook(@PathVariable("bookId") String bookIdStr) {
        int bookId;
        try {
            bookId = Integer.parseInt(bookIdStr);
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request", ex.getMessage(), true),
                            HttpStatus.BAD_REQUEST);
        }
        // input is valid, getting the data now
        com.bookinator.api.model.dto.Book book;
        try {
            book = bookDAO.getBookDTO(bookId);
            if(book == null) {
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(404, "Not found",
                                "Book with that ID does not exist", true),
                                HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource
                            (500, "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<BookDtoResource>(BooksHelper.getBookDtoResource(book),
                HttpStatus.OK);
    }

    @RequestMapping(value = "{username}/books", method = RequestMethod.POST)
    HttpEntity saveBook(@RequestBody com.bookinator.api.model.Book book,
                        @PathVariable("username") String username,
                        @RequestHeader("Authorization") String token) {
        if(!GeneralHelper.isPathUsernameValid(username)) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "Malformed URL: no username", true),
                    HttpStatus.BAD_REQUEST);
        }
        if(book.getOwnerId() == 0) {
            book.setOwnerId((int) GeneralHelper.getUserIdFromToken(token));
        }
        if(book.getName() == null || book.getAuthors() == null || book.getFieldId() == 0) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400,
                            "Bad Request", "Name, authors and field of the book must be specified", true),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            bookDAO.create(book);
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(500,
                            "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(BookController.class).slash("books/"+book.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{username}/books/{bookId}", method = RequestMethod.DELETE)
    HttpEntity deleteBook(@PathVariable("bookId") String bookIdStr,
                          @PathVariable String username,
                          @RequestHeader("Authorization") String token) {
        try {
            int bookId = Integer.parseInt(bookIdStr);
            // checking that the book exists:
            com.bookinator.api.model.Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(404, "Not found",
                                "Book with that ID does not exist", true),
                        HttpStatus.NOT_FOUND);
            } else {
                // checking that the book belongs to the token's user:
                int userId = (int) GeneralHelper.getUserIdFromToken(token);
                if(userId != book.getOwnerId()) {
                    return new ResponseEntity<ErrorResource>(
                            GeneralHelper.getErrorResource(403, "Forbidden",
                                    "You cannot delete someone else's book", true),
                            HttpStatus.FORBIDDEN);
                }
                // let's finally delete it:
                bookDAO.delete(bookId);
            }
        } catch (NumberFormatException ex) {
            // parsing error (HTTP 400)
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request", ex.getMessage(), true),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(500,
                            "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = new HttpHeaders(); // any headers?
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "{username}/books/{bookId}", method = RequestMethod.PUT)
    HttpEntity updateBook(@RequestBody com.bookinator.api.model.Book book,
                          @PathVariable String username,
                          @RequestHeader("Authorization") String token,
                          @PathVariable("bookId") String bookIdStr) {
        // checking that the book belongs to the user:
        int userId = (int) GeneralHelper.getUserIdFromToken(token);
        if(userId != book.getOwnerId()) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(403, "Forbidden",
                            "You cannot update someone else's book", true),
                    HttpStatus.FORBIDDEN);
        }
        if(book.getOwnerId() == 0) {
            book.setOwnerId((int) GeneralHelper.getUserIdFromToken(token));
        }
        if(book.getId() == 0) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            "Book ID must be specified", true),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            com.bookinator.api.model.Book bookFromDB = bookDAO.getBookById(book.getId());
            if (bookFromDB == null) {
                // then we should create it
                bookDAO.create(book);
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(linkTo(BookController.class).slash(book.getId()).toUri());
                return new ResponseEntity<>(headers, HttpStatus.CREATED);
            }
            bookDAO.update(book);
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(500,
                            "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(BookController.class).slash("books/"+book.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }
}
