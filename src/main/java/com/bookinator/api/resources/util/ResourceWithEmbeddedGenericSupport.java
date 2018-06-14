package com.bookinator.api.resources.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sabina on 6/14/2018.
 */
public class ResourceWithEmbeddedGenericSupport extends ResourceSupport {
    private final Map<String, Object> embedded = new HashMap<String, Object>();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("_embedded")
    public Map<String, Object> getEmbeddedResources() {
        return embedded;
    }

    public void embedResource(String relationship, Object resourceList) {
        embedded.put(relationship, resourceList);
    }
}
