package com.bookinator.api.dao;

import com.bookinator.api.model.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Sabina on 5/6/2018.
 */
@Mapper
public interface MessageDAO {
    /**
     * Creating message
     * @param message message
     */
    void create(Message message);

    /**
     * Get message by id - in the way stored in db
     * @param id message id
     * @return message
     */
    Message getMessageById(@Param("id") int id);

    /**
     * Get user's messages - in a readable way
     * @param id user id
     * @return messages
     */
    List<com.bookinator.api.model.dto.Message> getMessages(@Param("id") int id);
}
