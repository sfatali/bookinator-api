package com.bookinator.api.model;

/**
 * Created by Sabina on 5/6/2018.
 */
public class Wish {
    private int userId;
    private int bookId;

    public Wish() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
