package com.bookinator.api.controller;

import com.bookinator.api.resources.ErrorResource;
import com.bookinator.api.resources.util.CustomLink;

/**
 * Created by Sabina on 6/13/2018.
 */
public class ControllerHelper {

    public static ErrorResource getErrorResource(int status, String error, String message) {
        ErrorResource errorResource = new ErrorResource();
        errorResource.setStatus(status);
        errorResource.setError(error);
        errorResource.setMessage(message);
        CustomLink homeLink = new CustomLink("localhost:8080", "home");
        homeLink.setMethod("GET");
        errorResource.add(homeLink);
        return errorResource;
    }
}
