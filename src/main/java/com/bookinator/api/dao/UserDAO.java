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
    void create(User user);
    void update(User user);
    void delete(@Param("id") int id);
    UserProfile getUserProfile(@Param("id") int id);
    User getById(@Param("id") int id);
    int countByUsername(String name);
}
