package com.bookinator.api.controller.helpers;

import com.bookinator.api.controller.HomeController;
import com.bookinator.api.controller.WelcomeController;
import com.bookinator.api.resources.ErrorResource;
import com.bookinator.api.resources.util.CustomLink;
import com.bookinator.api.resources.util.RequestTemplateItem;
import com.bookinator.api.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import static com.bookinator.api.security.SecurityConstants.SECRET;
import static com.bookinator.api.security.SecurityConstants.TOKEN_PREFIX;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Sabina on 6/13/2018.
 */
public class GeneralHelper {

    public static ErrorResource getErrorResource(
            int status, String error, String message, boolean isAuth) {
        ErrorResource errorResource = new ErrorResource();
        errorResource.setStatus(status);
        errorResource.setError(error);
        errorResource.setMessage(message);
        CustomLink homeLink;
        if(isAuth) {
            homeLink = new CustomLink(
                    linkTo(methodOn(HomeController.class)
                            .goHome(null, null)).toString(),
                    "home", "GET", true);
        } else {
            homeLink = new CustomLink(
                    linkTo(methodOn(WelcomeController.class)
                            .getApiEntry()).toString(),
                    "home", "GET", false);
        }
        errorResource.add(homeLink);
        return errorResource;
    }

    public static RequestTemplateItem getTemplateItem(String field, String type, boolean required,
                                               Integer minLength, Integer maxLength) {
        RequestTemplateItem item = new RequestTemplateItem(field, type, required);
        item.setMinLength(minLength);
        item.setMaxLength(maxLength);
        return item;
    }

    public static boolean isTokenValid(String token) {
        return (token == null || !token.startsWith(TOKEN_PREFIX));
    }

    public static boolean isPathUsernameValid(String pathUsername) {
        return !(pathUsername == null || pathUsername.length() < 5);
    }

    public static boolean isUserAccessingOwnResources(String token, String pathUsername) {
        String username = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();
        return pathUsername.equals(username);
    }

    public static long getUserIdFromToken(String token) {
        token = token.substring(token.indexOf(" ")+1);
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setSkipSignatureVerification()
                .build();
        try {
            JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
            return (Long) jwtClaims.getClaimsMap().get("user-id");
        } catch (Exception ex) {
            return 0;
        }
        /*Jws<Claims> jwsClaims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token);
        Claims claims = jwsClaims.getBody();

        return (Integer) claims.get("user-id");*/
    }
}
