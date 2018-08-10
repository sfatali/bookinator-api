package com.bookinator.api.resources.util;

import org.springframework.hateoas.Link;

import java.util.List;

/**
 * Created by Sabina on 6/13/2018.
 */
public class CustomLink extends Link {
    private String method;
    private boolean authRequired;
    private String description;
    private String title;

    public CustomLink(String href, String rel) {
        super(href, rel);
    }

    public CustomLink(String href, String rel, String method, boolean auth) {
        super(href, rel);
        this.method = method;
        this.authRequired = auth;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isAuthRequired() {
        return authRequired;
    }

    public void setAuthRequired(boolean authRequired) {
        this.authRequired = authRequired;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
