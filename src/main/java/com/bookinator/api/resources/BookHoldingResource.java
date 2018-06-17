package com.bookinator.api.resources;

import com.bookinator.api.model.Util;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Sabina on 6/17/2018.
 */
public class BookHoldingResource extends ResourceSupport {
    private int requestId;
    private Util sender;
    private Util book;
    private Util parentBook;
    private String date;
    private String requestMessage;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
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
