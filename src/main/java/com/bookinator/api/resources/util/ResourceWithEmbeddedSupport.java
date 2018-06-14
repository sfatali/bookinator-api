package com.bookinator.api.resources.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sabina on 6/13/2018.
 */
public class ResourceWithEmbeddedSupport extends ResourceSupport {
    private final Map<String, ResourceSupport> embedded = new HashMap<String, ResourceSupport>();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("_embedded")
    public Map<String, ResourceSupport> getEmbeddedResources() {
        return embedded;
    }

    public void embedResource(String relationship, ResourceSupport resource) {
        embedded.put(relationship, resource);
    }
}
