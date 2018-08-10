package com.bookinator.api.dao;

import com.bookinator.api.model.User;
import com.bookinator.api.model.dto.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Sabina on 4/15/2018.
 */
@Mapper
public interface UserDAO {
    /**
     * Create user
     * @param user user
     */
    void create(User user);

    /**
     * Update user
     * @param user user
     */
    void update(User user);

    /**
     * Delete user
     * @param id user id
     */
    void delete(@Param("id") int id);

    /**
     * Get user profile - user + extra data
     * @param id user id
     * @return user profile
     */
    UserProfile getUserProfile(@Param("id") int id);

    /**
     * Get user by id - in a way stored in database
     * @param id user id
     * @return user
     */
    User getById(@Param("id") int id);

    /**
     * Check if username is taken
     * @param name username
     * @return number of users with that username
     */
    int countByUsername(String name);

    /**
     * Get user by username
     * @param name username
     * @return user
     */
    User findByUsername(String name);

    /**
     * Get user id by username
     * @param name username
     * @return user id
     */
    int getIdByUsername(String name);
}
