package com.bookinator.api.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Sabina on 6/15/2018.
 */
@RestController
@RequestMapping(value = "/")
public class UserBookHoldingsController {

    @RequestMapping(value = "{username}/holdings", method = RequestMethod.GET, produces ={"application/hal+json"})
    HttpEntity getHoldings(@RequestHeader("Authorization") String token,
                           @PathVariable("username") String username) {
        return null;
    }
}
