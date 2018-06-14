package com.bookinator.api.resources.util;

import org.springframework.hateoas.Link;

import java.util.List;

/**
 * Created by Sabina on 6/14/2018.
 */
public class CustomLinkWithTemplate extends Link {
    private String method;
    private List<TemplateItem> requestTemplate;

    public CustomLinkWithTemplate(String href, String rel) {
        super(href, rel);
    }

    public CustomLinkWithTemplate(String href, String rel, String method) {
        super(href, rel);
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<TemplateItem> getRequestTemplate() {
        return requestTemplate;
    }

    public void setRequestTemplate(List<TemplateItem> requestTemplate) {
        this.requestTemplate = requestTemplate;
    }
}
