package com.bookinator.api.controller.helpers;

import com.bookinator.api.controller.BookController;
import com.bookinator.api.model.dto.Book;
import com.bookinator.api.model.dto.BookFilterRequest;
import com.bookinator.api.resources.BookDtoResource;
import com.bookinator.api.resources.util.CustomLink;
import com.bookinator.api.resources.util.RequestTemplateItem;
import com.bookinator.api.resources.util.UrlTemplateItem;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Sabina on 6/14/2018.
 */
public class BooksHelper {
    public static List<UrlTemplateItem> getFilterBooksTemplate() {
        List<UrlTemplateItem> urlTemplate = new ArrayList<>();
        // url params for books filter
        UrlTemplateItem name = new UrlTemplateItem("name", "String", false);
        urlTemplate.add(name);

        UrlTemplateItem author = new UrlTemplateItem("author", "String", false);
        urlTemplate.add(author);

        UrlTemplateItem fieldId = new UrlTemplateItem("fieldId", "int", false);
        urlTemplate.add(fieldId);

        UrlTemplateItem yearPublished = new UrlTemplateItem("yearPublished",
                "int", false);
        urlTemplate.add(yearPublished);

        UrlTemplateItem cityId = new UrlTemplateItem("cityId", "int", false);
        urlTemplate.add(cityId);

        UrlTemplateItem topic = new UrlTemplateItem("topic", "String", false);
        urlTemplate.add(topic);
        return urlTemplate;
    }

    public static BookDtoResource getBookDtoResource(Book book) {
        // setting up the resource
        BookDtoResource bookResource = new BookDtoResource();
        CustomLink self = new CustomLink(
                linkTo(methodOn(BookController.class).
                        getBook(String.valueOf(book.getId()))).toString(),
                "self", "GET", false);
        bookResource.add(self);
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
        return bookResource;
    }

    public static List<RequestTemplateItem> getBookPostTemplate() {
        List<RequestTemplateItem> reqTemplate = new ArrayList<>();

        RequestTemplateItem ownerId = GeneralHelper.getTemplateItem
                ("ownerId", "int", true, 1, null);
        reqTemplate.add(ownerId);

        RequestTemplateItem name = GeneralHelper.getTemplateItem
                ("name", "String", true, 2, 350);
        reqTemplate.add(name);

        RequestTemplateItem holdingTypeId = GeneralHelper.getTemplateItem
                ("holdingTypeId", "int", false, 1, null);
        reqTemplate.add(holdingTypeId);

        RequestTemplateItem statusId = GeneralHelper.getTemplateItem
                ("statusId", "int", false, 1, null);
        reqTemplate.add(statusId);

        RequestTemplateItem authors = GeneralHelper.getTemplateItem
                ("authors", "Strings separated by ;", true, 1, null);
        reqTemplate.add(authors);

        RequestTemplateItem yearPublished = GeneralHelper.getTemplateItem
                ("yearPublished", "int", false, 1, null);
        reqTemplate.add(yearPublished);

        RequestTemplateItem fieldId = GeneralHelper.getTemplateItem
                ("fieldId", "int", true, 1, null);
        reqTemplate.add(fieldId);

        RequestTemplateItem topics = GeneralHelper.getTemplateItem
                ("topics", "Strings separated by ;", false, 1, null);
        reqTemplate.add(topics);

        RequestTemplateItem description = GeneralHelper.getTemplateItem
                ("description", "Strings", false, 1, null);
        reqTemplate.add(description);
        return reqTemplate;
    }

    public static List<UrlTemplateItem> getBookDeleteTemplate() {
        List<UrlTemplateItem> urlTemplate = new ArrayList<>();
        UrlTemplateItem bookId = new UrlTemplateItem("bookId", "int", true);
        urlTemplate.add(bookId);
        return urlTemplate;
    }

    public static List<UrlTemplateItem> getUserBooksUrlTemplate() {
        List<UrlTemplateItem> urlTemplate = new ArrayList<>();
        UrlTemplateItem username = new UrlTemplateItem("username", "String", true);
        urlTemplate.add(username);
        return urlTemplate;
    }
}
