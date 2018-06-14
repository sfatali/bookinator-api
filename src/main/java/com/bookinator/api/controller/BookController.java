package com.bookinator.api.controller;

import com.bookinator.api.dao.BookDAO;
import com.bookinator.api.model.dto.BookFilterResponse;
import com.bookinator.api.model.dto.BookFilterRequest;
import com.bookinator.api.resources.BookDtoResource;
import com.bookinator.api.resources.BookFilterResource;
import com.bookinator.api.resources.ErrorResource;
import com.bookinator.api.resources.util.CustomLink;
import com.bookinator.api.resources.util.ResourceWithEmbeddedGenericSupport;
import com.bookinator.api.resources.util.ResourceWithEmbeddedSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sabina on 6/12/2018.
 */
@RestController
@RequestMapping(value = "/books")
public class BookController {
    @Autowired
    private BookDAO bookDAO;

    /**
     * Getting all books (if no filter parameter specified).
     * Or filtered list of books
     * @param name
     * @param author
     * @param fieldId
     * @param yearPublished
     * @param cityId
     * @param topic
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET/*, produces ={"application/collection+json"}*/)
    HttpEntity filterBooks(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "author", required = false) String author,
                           @RequestParam(value = "fieldId", required = false) String fieldId,
                           @RequestParam(value = "yearPublished", required = false) String yearPublished,
                           @RequestParam(value = "cityId", required = false) String cityId,
                           @RequestParam(value = "topic", required = false) String topic) {
        BookFilterRequest request = new BookFilterRequest();
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
                        ControllerHelper.getErrorResource(400, "Bad Request", ex.getMessage()),
                        HttpStatus.BAD_REQUEST);
            }
        }
        if(yearPublished != null && yearPublished.length() != 0) {
            try {
                request.setYearPublished(Integer.parseInt(yearPublished));
            } catch (Exception ex) {
                // parsing error (HTTP 400)
                return new ResponseEntity<ErrorResource>(
                        ControllerHelper.getErrorResource(400, "Bad Request", ex.getMessage()),
                        HttpStatus.BAD_REQUEST);
            }
        }
        if(cityId != null && cityId.length() != 0) {
            try {
                request.setCityId(Integer.parseInt(cityId));
            } catch (Exception ex) {
                // parsing error (HTTP 400)
                return new ResponseEntity<ErrorResource>(
                        ControllerHelper.getErrorResource(400, "Bad Request", ex.getMessage()),
                        HttpStatus.BAD_REQUEST);
            }
        }
        // everything is validated, let's get the data now
        List<BookFilterResponse> books;
        try {
            books = bookDAO.filterBooks(request);
            if(books == null) {
                // no resources found error (HTTP 404)
                return new ResponseEntity<ErrorResource>(
                        ControllerHelper.getErrorResource(404, "Not found",
                                "No books found with your filter criteria."),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            // internal server error (HTTP 500)
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource
                            (500, "Internal server error", ex.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ResourceWithEmbeddedGenericSupport resource = new ResourceWithEmbeddedGenericSupport();
        List<BookFilterResource> bookFilterResourceList = new ArrayList<>();
        for(BookFilterResponse book : books) {
            BookFilterResource bookResource = new BookFilterResource();
            /*CustomLink self = new CustomLink(linkTo(BookController.class).slash(book.getId()).toString(),
                    "self", "GET");// linkTo(methodOn(BookController.class)
                    //.getBook(String.valueOf(book.getId()))).withSelfRel();*/
            bookResource.add(linkTo(BookController.class).slash(book.getId()).withSelfRel());
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
            bookFilterResourceList.add(bookResource);
        }

        /*CustomLink self = new CustomLink(
                linkTo(methodOn(BookController.class)
                                .filterBooks(name, author, fieldId, yearPublished, cityId, topic)).toString(),
                "self", "GET");*/
        resource.add(linkTo(methodOn(BookController.class)
                .filterBooks(name, author, fieldId, yearPublished, cityId, topic))
                .withSelfRel());
        resource.embedResource("books", bookFilterResourceList);
        return new ResponseEntity<ResourceWithEmbeddedGenericSupport>(resource, HttpStatus.OK);
    }

    /**
     * Getting a book by ID
     * @param bookIdStr
     * @return
     */
    @RequestMapping(value = "/{bookId}", method = RequestMethod.GET)
    HttpEntity getBook(@PathVariable("bookId") String bookIdStr) {
        int bookId;
        try {
            bookId = Integer.parseInt(bookIdStr);
        } catch (Exception ex) {
            // parsing error (HTTP 400)
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request", ex.getMessage()),
                            HttpStatus.BAD_REQUEST);
        }
        // input is valid, getting the data now
        com.bookinator.api.model.dto.Book book;
        try {
            book = bookDAO.getBookDTO(bookId);
            if(book == null) {
                // not found (HTTP 404)
                return new ResponseEntity<ErrorResource>(
                        ControllerHelper.getErrorResource(404, "Not found",
                                "Book with that ID does not exist"),
                                HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            // internal server error (HTTP 500)
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource
                            (500, "Internal server error", ex.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // setting up the resource
        BookDtoResource bookResource = new BookDtoResource();
        bookResource.add(linkTo(methodOn(BookController.class).getBook(bookIdStr)).withSelfRel());
        bookResource.setBookId(book.getId());
        if(!book.getAuthors().equals(""))
            bookResource.setAuthors(book.getAuthors().split(";"));
        else
            bookResource.setAuthors(new String[0]);
        bookResource.setOwnerId(book.getOwnerId());
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
        return new ResponseEntity<BookDtoResource>(bookResource, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    HttpEntity saveBook(@RequestBody com.bookinator.api.model.Book book) {
        if(book.getOwnerId() == 0) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request", "Owner must be specified"),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            bookDAO.create(book);
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(500, "Internal server error", ex.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(BookController.class).slash(book.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{bookId}", method = RequestMethod.DELETE)
    HttpEntity deleteBook(@PathVariable("bookId") String bookIdStr) {
        int bookId;
        try {
            bookId = Integer.parseInt(bookIdStr);
        } catch (Exception ex) {
            // parsing error (HTTP 400)
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request", ex.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            com.bookinator.api.model.Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                return new ResponseEntity<ErrorResource>(
                        ControllerHelper.getErrorResource(404, "Not found",
                                "Book with that ID does not exist"),
                        HttpStatus.NOT_FOUND);
            }
            bookDAO.delete(bookId);
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(500, "Internal server error", ex.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = new HttpHeaders(); // any headers?
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    HttpEntity updateBook(@RequestBody com.bookinator.api.model.Book book) {
        if(book.getOwnerId() == 0) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request",
                            "Owner must be specified"),
                    HttpStatus.BAD_REQUEST);
        }
        if(book.getId() == 0) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(400, "Bad Request",
                            "Book ID must be specified"),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            com.bookinator.api.model.Book bookFromDB = bookDAO.getBookById(book.getId());
            if (bookFromDB == null) {
                return new ResponseEntity<ErrorResource>(
                        ControllerHelper.getErrorResource(404, "Not found",
                                "Book with that ID does not exist"),
                        HttpStatus.NOT_FOUND);
            }
            bookDAO.update(book);
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    ControllerHelper.getErrorResource(500, "Internal server error", ex.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(BookController.class).slash(book.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }
}
