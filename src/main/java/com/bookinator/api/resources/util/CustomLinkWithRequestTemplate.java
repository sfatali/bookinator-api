package com.bookinator.api.resources.util;

import org.springframework.hateoas.Link;

import java.util.List;

/**
 * Created by Sabina on 6/14/2018.
 */
public class CustomLinkWithRequestTemplate extends CustomLink {
    private List<RequestTemplateItem> requestTemplate;

    public CustomLinkWithRequestTemplate(String href, String rel, String method, boolean auth) {
        super(href, rel, method, auth);
    }

    public List<RequestTemplateItem> getRequestTemplate() {
        return requestTemplate;
    }

    public void setRequestTemplate(List<RequestTemplateItem> requestTemplate) {
        this.requestTemplate = requestTemplate;
    }
}
