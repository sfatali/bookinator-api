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
    void create(Message message);
    Message getMessageById(@Param("id") int id);
    List<com.bookinator.api.model.dto.Message> getMessages(@Param("id") int id);
}
