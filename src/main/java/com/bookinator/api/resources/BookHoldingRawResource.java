package com.bookinator.api.resources;

import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Sabina on 6/17/2018.
 */
public class BookHoldingRawResource extends ResourceSupport {
    private int requestId;
    private int senderId;
    private int receiverId;
    private int bookId;
    private int parentBookId;
    private int statusId;
    private String requestMessage;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getParentBookId() {
        return parentBookId;
    }

    public void setParentBookId(int parentBookId) {
        this.parentBookId = parentBookId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }
}
