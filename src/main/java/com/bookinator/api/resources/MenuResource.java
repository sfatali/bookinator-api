package com.bookinator.api.resources;

import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Sabina on 6/14/2018.
 */
public class MenuResource extends ResourceSupport {
    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
