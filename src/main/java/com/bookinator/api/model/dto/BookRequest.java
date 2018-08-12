package com.bookinator.api.model.dto;

import com.bookinator.api.model.Util;

/**
 * Created by Sabina on 5/6/2018.
 */
public class BookRequest {
    private int id;
    private Util sender;
    private Util book;
    private Util parentBook;
    private String date;
    private String requestMessage;

    public BookRequest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Util getSender() {
        return sender;
    }

    public void setSender(Util sender) {
        this.sender = sender;
    }

    public Util getBook() {
        return book;
    }

    public void setBook(Util book) {
        this.book = book;
    }

    public Util getParentBook() {
        return parentBook;
    }

    public void setParentBook(Util parentBook) {
        this.parentBook = parentBook;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }
}