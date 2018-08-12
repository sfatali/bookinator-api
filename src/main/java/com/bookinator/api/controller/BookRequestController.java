package com.bookinator.api.controller;

import com.bookinator.api.controller.helpers.GeneralHelper;
import com.bookinator.api.dao.BookDAO;
import com.bookinator.api.dao.BookHoldingsDAO;
import com.bookinator.api.model.Book;
import com.bookinator.api.model.dto.BookRequest;
import com.bookinator.api.model.dto.UpdateBookRequestStatus;
import com.bookinator.api.resources.BookHoldingRawResource;
import com.bookinator.api.resources.BookHoldingResource;
import com.bookinator.api.resources.ErrorResource;
import com.bookinator.api.resources.util.CustomLink;
import com.bookinator.api.resources.util.ResourceWithEmbeddedGenericSupport;
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
 * Created by Sabina on 6/15/2018.
 */
@RestController
@RequestMapping(value = "/")
public class BookRequestController {

    @Autowired
    private BookHoldingsDAO bookHoldingsDAO;
    @Autowired
    private BookDAO bookDAO;

    @RequestMapping(value = "{username}/requests", method = RequestMethod.GET, produces ={"application/hal+json"})
    HttpEntity getRequests(@RequestHeader("Authorization") String token,
                      @PathVariable("username") String username) {
        if(!GeneralHelper.isUserAccessingOwnResources(token, username)) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(403, "Forbidden",
                            "You cannot access someone else's book requests", true),
                    HttpStatus.FORBIDDEN);
        }
        List<BookRequest> requests;
        try {
            int id = (int) GeneralHelper.getUserIdFromToken(token);
            requests = bookHoldingsDAO.getFreshRequests(id);
            if (requests == null || requests.size() == 0) {
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
        List<BookHoldingResource> reqResources = new ArrayList<>();
        for(BookRequest req : requests) {
            BookHoldingResource res = getHoldingResource(req);
            CustomLink self = new CustomLink(linkTo(methodOn(BookRequestController.class)
                    .getRequest(token, username, String.valueOf(req.getId())))
                    .toString(), "self", "GET", true);
            res.add(self);
            CustomLink deleteLink = new CustomLink(linkTo(methodOn(BookRequestController.class)
                    .getRequest(token, username, String.valueOf(req.getId())))
                    .toString(), "decline-request", "PUT", true);
            res.add(deleteLink);
            reqResources.add(res);
        }
        ResourceWithEmbeddedGenericSupport resource = new ResourceWithEmbeddedGenericSupport();
        CustomLink selfLink = new CustomLink(
                linkTo(methodOn(BookRequestController.class)
                        .getRequests(token, username))
                        .toString(), "self", "GET", true);
        resource.add(selfLink);
        CustomLink homeLink = new CustomLink(
                linkTo(methodOn(HomeLinksController.class).goHome(token, username))
                        .toString(),
                "home", "GET", true);
        resource.add(homeLink);
        resource.embedResource("requests", reqResources);
        return new ResponseEntity<ResourceWithEmbeddedGenericSupport>(resource, HttpStatus.OK);
    }

    @RequestMapping(value = "{username}/requests/{reqID}", method = RequestMethod.GET, produces ={"application/hal+json"})
    HttpEntity getRequest(@RequestHeader("Authorization") String token,
                           @PathVariable("username") String username,
                          @PathVariable("reqID") String reqIDStr) {
        if(!GeneralHelper.isUserAccessingOwnResources(token, username)) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(403, "Forbidden",
                            "You cannot access someone else's book requests", true),
                    HttpStatus.FORBIDDEN);
        }
        int reqId;
        com.bookinator.api.model.HoldingRequest request;
        try {
            reqId = Integer.parseInt(reqIDStr);
            request = bookHoldingsDAO.getHoldingRequestById(reqId);
            if (request == null) {
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(404, "Not found",
                                "Request with this ID does not exist.", true),
                        HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            ex.getMessage(), true),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(500, "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        BookHoldingRawResource resource = new BookHoldingRawResource();
        resource.setBookId(request.getBookId());
        resource.setParentBookId(request.getParentBookId());
        resource.setReceiverId(request.getReceiverId());
        resource.setRequestMessage(request.getRequestMessage());
        resource.setRequestId(request.getId());
        resource.setSenderId(request.getSenderId());
        resource.setStatusId(resource.getStatusId());
        CustomLink self = new CustomLink(linkTo(methodOn(BookRequestController.class)
                .getRequest(token, username, reqIDStr))
                .toString(), "self", "GET", true);
        resource.add(self);
        CustomLink homeLink = new CustomLink(
                linkTo(methodOn(HomeLinksController.class).goHome(token, username))
                        .toString(),
                "home", "GET", true);
        resource.add(homeLink);
        return new ResponseEntity<BookHoldingRawResource>(resource, HttpStatus.OK);
    }

    @RequestMapping(value = "{username}/requests", method = RequestMethod.POST, produces ={"application/hal+json"})
    HttpEntity makeRequest(@RequestHeader("Authorization") String token,
                           @PathVariable("username") String username,
                           @RequestBody com.bookinator.api.model.HoldingRequest request) {
        if(!GeneralHelper.isUserAccessingOwnResources(token, username)
                || request.getSenderId() != GeneralHelper.getUserIdFromToken(token)) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(403, "Forbidden",
                            "You cannot make book request under someone else's username", true),
                    HttpStatus.FORBIDDEN);
        }

        // checking if book exists
        Book book = bookDAO.getBookById(request.getBookId());
        if(book == null) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(404, "Not found",
                            "Book with this ID does not exist.", true),
                    HttpStatus.NOT_FOUND);
        }
        //request.setReceiverId(book.getOwnerId());

        // checking if book is available
        if(!(book.getStatusId() == 1 || book.getStatusId() == 5)) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(409, "Conflict",
                            "This book is currently not available for request or suggestion.",
                            true),
                    HttpStatus.CONFLICT);
        }

        try {
            request.setStatusId(1);
            bookHoldingsDAO.create(request);
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(500, "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(methodOn(BookRequestController.class)
                        .getRequest(token, username, String.valueOf(request.getId()))).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{username}/requests/{reqID}", method = RequestMethod.PUT, produces ={"application/hal+json"})
    HttpEntity declineRequest(@RequestHeader("Authorization") String token,
                              @PathVariable("username") String username,
                              @PathVariable("reqID") String reqIDStr,
                              @RequestBody com.bookinator.api.model.HoldingRequest request) {
        if(!GeneralHelper.isUserAccessingOwnResources(token, username)) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(403, "Forbidden",
                            "You cannot decline someone else's book request", true),
                    HttpStatus.FORBIDDEN);
        }
        int reqId;
        com.bookinator.api.model.HoldingRequest requestFromDB;
        try {
            reqId = Integer.parseInt(reqIDStr);
            requestFromDB = bookHoldingsDAO.getHoldingRequestById(reqId);
            if (requestFromDB == null || reqId != requestFromDB.getId()) {
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(404, "Not found",
                                "Request with this ID does not exist.", true),
                        HttpStatus.NOT_FOUND);
            }
            if(requestFromDB.getReceiverId() != request.getReceiverId()
                    || requestFromDB.getReceiverId() != GeneralHelper.getUserIdFromToken(token)) {
                return new ResponseEntity<ErrorResource>(
                        GeneralHelper.getErrorResource(403, "Forbidden",
                                "Only receiver of request can decline it.", true),
                        HttpStatus.FORBIDDEN);
            }
            UpdateBookRequestStatus updateHoldingRequestStatus = new UpdateBookRequestStatus();
            updateHoldingRequestStatus.setHoldingRequestId(reqId);
            updateHoldingRequestStatus.setStatusId(3);
            bookHoldingsDAO.changeHoldingStatus(updateHoldingRequestStatus);
        } catch (NumberFormatException ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(400, "Bad Request",
                            ex.getMessage(), true),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResource>(
                    GeneralHelper.getErrorResource(500, "Internal server error", ex.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(methodOn(BookRequestController.class)
                .getRequest(token, username, String.valueOf(request.getId()))).toUri());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    private BookHoldingResource getHoldingResource(BookRequest request) {
        BookHoldingResource res = new BookHoldingResource();
        res.setRequestId(request.getId());
        res.setBook(request.getBook());
        res.setDate(request.getDate());
        res.setParentBook(request.getParentBook());
        res.setRequestMessage(request.getRequestMessage());
        res.setSender(request.getSender());
        return res;
    }
}
