package com.bookinator.api.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Sabina on 6/17/2018.
 */
public class ReviewController {

    @RequestMapping(value = "{username}/profile/reviews", method = RequestMethod.GET, produces ={"application/hal+json"})
    public HttpEntity getUserReviews(@RequestHeader("Authorization") String token,
                             @PathVariable("username") String username) {
        return null;
    }
}
