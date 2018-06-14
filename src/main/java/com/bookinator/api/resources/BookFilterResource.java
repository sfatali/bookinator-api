package com.bookinator.api.resources;

import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Sabina on 6/13/2018.
 */
public class BookFilterResource extends ResourceSupport {
    private int bookId;
    private int ownerId;
    private double ownerAvgRating;
    private String name;
    private String holdingType;
    private String status;
    private String[] authors;
    private String yearPublished;
    private String field;
    private String[] topics;
    private String description;
    private String city;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public double getOwnerAvgRating() {
        return ownerAvgRating;
    }

    public void setOwnerAvgRating(double ownerAvgRating) {
        this.ownerAvgRating = ownerAvgRating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHoldingType() {
        return holdingType;
    }

    public void setHoldingType(String holdingType) {
        this.holdingType = holdingType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(String yearPublished) {
        this.yearPublished = yearPublished;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
