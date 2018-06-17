package com.bookinator.api.resources.util;

import java.util.List;

/**
 * Created by Sabina on 6/14/2018.
 */
public class CustomLinkWithUrlTemplate extends CustomLink {
    private List<UrlTemplateItem> urlTemplate;

    public CustomLinkWithUrlTemplate(String href, String rel, String method, boolean auth) {
        super(href, rel, method, auth);
    }

    public List<UrlTemplateItem> getUrlTemplate() {
        return urlTemplate;
    }

    public void setUrlTemplate(List<UrlTemplateItem> urlTemplate) {
        this.urlTemplate = urlTemplate;
    }
}
