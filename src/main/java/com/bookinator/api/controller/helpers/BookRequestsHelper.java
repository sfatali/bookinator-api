package com.bookinator.api.controller.helpers;

import com.bookinator.api.resources.util.RequestTemplateItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sabina on 6/16/2018.
 */
public class BookRequestsHelper {

    public static List<RequestTemplateItem> getBookRequestsTemplate() {
        List<RequestTemplateItem> reqTemplate = new ArrayList<>();
        RequestTemplateItem senderId = new RequestTemplateItem("senderId", "int", true);
        senderId.setMinLength(1);
        reqTemplate.add(senderId);
        RequestTemplateItem receiverId = new RequestTemplateItem("receiverId", "int", true);
        receiverId.setMinLength(1);
        reqTemplate.add(receiverId);
        RequestTemplateItem bookId = new RequestTemplateItem("bookId", "int", true);
        bookId.setMinLength(1);
        reqTemplate.add(bookId);
        RequestTemplateItem parentBookId = new RequestTemplateItem("parentBookId", "int",
                false);
        reqTemplate.add(parentBookId);
        RequestTemplateItem requestMessage = new RequestTemplateItem("requestMessage", "int", false);
        requestMessage.setMinLength(1);
        requestMessage.setMaxLength(500);
        reqTemplate.add(requestMessage);
        return reqTemplate;
    }
}
