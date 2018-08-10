package com.bookinator.api.dao;

import com.bookinator.api.model.HoldingRequest;
import com.bookinator.api.model.dto.UpdateHoldingRequestStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Sabina on 5/6/2018.
 */
@Mapper
public interface BookHoldingsDAO {
    /**
     * Creating "book request"
     * @param request book request
     */
    void create(HoldingRequest request);

    /**
     * Getting "book request" - in a way stored in database
     * @param id request id
     * @return book request
     */
    HoldingRequest getHoldingRequestById(@Param("id") int id);

    /**
     * Changing status of the request
     * @param request request
     */
    void changeHoldingStatus(UpdateHoldingRequestStatus request);

    /**
     * Getting user's unresponded requests - in a readable way
     * @param userId user id
     * @return requests
     */
    List<com.bookinator.api.model.dto.HoldingRequest> getFreshRequests(@Param("userId") int userId);

    /**
     * Getting user's approved requests - in a readable way
     * @param userId user id
     * @return requests
     */
    List<com.bookinator.api.model.dto.HoldingRequest> getApprovedRequests(@Param("userId") int userId);
}
