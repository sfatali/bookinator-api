package com.bookinator.api.model;

/**
 * Created by Sabina on 4/14/2018.
 */
public class Message {
    private int id;
    private int holdingRequestId;
    private int senderId;
    private int receiverId;
    private String messageText;

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHoldingRequestId() {
        return holdingRequestId;
    }

    public void setHoldingRequestId(int holdingRequestId) {
        this.holdingRequestId = holdingRequestId;
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

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
