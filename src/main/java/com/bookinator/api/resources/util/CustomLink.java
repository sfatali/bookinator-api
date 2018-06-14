package com.bookinator.api.resources.util;

import org.springframework.hateoas.Link;

import java.util.List;

/**
 * Created by Sabina on 6/13/2018.
 */
public class CustomLink extends Link {
    private String method;

    public CustomLink(String href, String rel) {
        super(href, rel);
    }

    public CustomLink(String href, String rel, String method) {
        super(href, rel);
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
