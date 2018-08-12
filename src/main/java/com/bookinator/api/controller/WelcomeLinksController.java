package com.bookinator.api.controller;

import com.bookinator.api.controller.helpers.BooksHelper;
import com.bookinator.api.controller.helpers.GeneralHelper;
import com.bookinator.api.resources.util.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Sabina on 6/11/2018.
 */
@RestController
@RequestMapping(value = "/")
public class WelcomeLinksController {

    @RequestMapping(value = "welcome", method = RequestMethod.GET, produces ={"application/hal+json"})
    public HttpEntity getApiEntry() {
        ResourceSupport resource = new ResourceSupport();

        // Self link:
        CustomLink self = new CustomLink(linkTo(WelcomeLinksController.class)
                            .slash("/welcome").toString(),"self","GET",false);
        self.setTitle("Welcome to Bookinator!");
        self.setDescription("You can register, login or explore the books");
        resource.add(self);

        // Register link:
        CustomLinkWithRequestTemplate registerLink =
                new CustomLinkWithRequestTemplate(linkTo(UserController.class)
                        .slash("/user").toString(),
                        "register", "POST", false);
        registerLink.setTitle("Register");
        registerLink.setDescription("Create your user account and embrace the full power of Bookinator!");
        registerLink.setRequestTemplate(getRegistrationTemplate());
        resource.add(registerLink);

        // Login link:
        CustomLinkWithRequestTemplate loginLink =
                new CustomLinkWithRequestTemplate("http://localhost:8080/login",
                        "login", "POST", false);
        loginLink.setTitle("Log in");
        loginLink.setDescription("Enter your username and password");
        loginLink.setRequestTemplate(getLoginTemplate());
        resource.add(loginLink);

        // Explore books link:
        CustomLinkWithUrlTemplate exploreBooksLink = new CustomLinkWithUrlTemplate(
                linkTo(methodOn(ExploreController.class)
                        .filterBooks(null, null, null, null, null, null))
                        .toString(), "explore", "GET", false);
        exploreBooksLink.setTitle("Explore books");
        exploreBooksLink.setDescription("Use filter to get the most accurate results");
        exploreBooksLink.setUrlTemplate(BooksHelper.getFilterBooksTemplate());
        resource.add(exploreBooksLink);

        // Post a book you look for ==> will be added if there is any time left => never ^_^

        return new ResponseEntity<ResourceSupport>(resource, HttpStatus.OK);
    }

    /*@RequestMapping(value = "welcome_old", method = RequestMethod.GET, produces ={"application/hal+json"})
    public HttpEntity getApiEntryOld() {
        // Register:
        MenuResource regItem = new MenuResource();
        regItem.setTitle("Register");
        regItem.setDescription("Create your user account and embrace the full power of Bookinator!");
        CustomLinkWithRequestTemplate regSelfLink =
                new CustomLinkWithRequestTemplate(linkTo(RegistrationController.class).slash("/register").toString(),
                        "self", "POST", false);
        regSelfLink.setRequestTemplate(getRegistrationTemplate());
        regItem.add(regSelfLink);

        // Log in:
        MenuResource loginItem = new MenuResource();
        loginItem.setTitle("Log in");
        loginItem.setDescription("Enter your username and password");
        CustomLinkWithRequestTemplate logSelfLink =
                new CustomLinkWithRequestTemplate("http://localhost:8080/login",
                        "self", "POST", false);
        logSelfLink.setRequestTemplate(getLoginTemplate());
        loginItem.add(logSelfLink);

        // Explore books:
        MenuResource exploreItem = new MenuResource();
        exploreItem.setTitle("Explore books");
        exploreItem.setDescription("Use filter to get the most accurate results");
        CustomLinkWithUrlTemplate expLink = new CustomLinkWithUrlTemplate(
                linkTo(methodOn(ExploreController.class)
                .filterBooks(null, null, null, null, null, null))
                .toString(), "self", "GET", false);
        expLink.setUrlTemplate(BooksHelper.getFilterBooksTemplate());
        exploreItem.add(expLink);

        // Post a book you look for ==> will be added if there is any time left

        // Wrap all options in embedded:
        ResourceWithEmbeddedSupport resource = new ResourceWithEmbeddedSupport();
        CustomLink self = new CustomLink(
                linkTo(methodOn(WelcomeController.class)
                        .getApiEntry()).toString(),"self","GET",false);
        resource.add(self);
        resource.embedResource("register", regItem);
        resource.embedResource("login", loginItem);
        resource.embedResource("explore", exploreItem);
        return new ResponseEntity<ResourceWithEmbeddedSupport>(resource, HttpStatus.OK);
    }*/

    private List<RequestTemplateItem> getRegistrationTemplate() {
        List<RequestTemplateItem> registrationTemplate = new ArrayList<>();
        // fields required for registration:
        RequestTemplateItem username = GeneralHelper.getTemplateItem("username", "String", true,
                5, 10);
        registrationTemplate.add(username);

        RequestTemplateItem password = GeneralHelper.getTemplateItem("password", "String", true,
                8, null);
        registrationTemplate.add(password);

        RequestTemplateItem name = GeneralHelper.getTemplateItem("name", "String", true,
                2, 50);
        registrationTemplate.add(name);

        RequestTemplateItem surname = GeneralHelper.getTemplateItem("surname", "String", true,
                2, 50);
        registrationTemplate.add(surname);

        RequestTemplateItem cityId = GeneralHelper.getTemplateItem("cityId", "int", true,
                1, null);
        registrationTemplate.add(cityId);

        RequestTemplateItem email = GeneralHelper.getTemplateItem("email", "String", true,
                1, null);
        registrationTemplate.add(email);

        RequestTemplateItem phone = GeneralHelper.getTemplateItem("phone", "String", true,
                1, null);
        registrationTemplate.add(phone);
        return registrationTemplate;
    }

    private List<RequestTemplateItem> getLoginTemplate() {
        List<RequestTemplateItem> loginTemplate = new ArrayList<>();
        // fields required for login:
        RequestTemplateItem username = GeneralHelper.getTemplateItem("username", "String", true,
                5, 10);
        loginTemplate.add(username);

        RequestTemplateItem password = GeneralHelper.getTemplateItem("password", "String", true,
                8, null);
        loginTemplate.add(password);
        return loginTemplate;
    }
}
