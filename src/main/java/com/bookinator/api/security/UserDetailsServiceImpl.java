package com.bookinator.api.security;

import com.bookinator.api.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static java.util.Collections.emptyList;

/**
 * Created by Sabina on 6/14/2018.
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDAO userDAO;

    public UserDetailsServiceImpl() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.bookinator.api.model.User userDb = userDAO.findByUsername(username);
        if (userDb == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(userDb.getUsername(), userDb.getPassword(), emptyList());
    }
}
