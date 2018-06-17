package com.bookinator.api.controller.helpers;

import com.bookinator.api.resources.util.RequestTemplateItem;
import com.bookinator.api.resources.util.UrlTemplateItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sabina on 6/16/2018.
 */
public class WishlistHelper {

    public static List<RequestTemplateItem> getWishlistUrlTemplate() {
        List<RequestTemplateItem> reqTemplate = new ArrayList<>();
        RequestTemplateItem userId = new RequestTemplateItem("userId", "int", true);
        userId.setMinLength(1);
        reqTemplate.add(userId);
        RequestTemplateItem bookId = new RequestTemplateItem("bookId", "int", true);
        bookId.setMinLength(1);
        reqTemplate.add(bookId);
        return reqTemplate;
    }
}
