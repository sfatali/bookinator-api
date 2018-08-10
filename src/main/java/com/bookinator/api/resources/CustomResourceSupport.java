package com.bookinator.api.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sabina on 8/8/2018.
 */
public class CustomResourceSupport extends ResourceSupport {
    private List<Link> links;

    @JsonProperty("_links")
    @Override
    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
