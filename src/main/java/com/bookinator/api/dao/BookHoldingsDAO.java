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
    void create(HoldingRequest request);
    HoldingRequest getHoldingRequestById(@Param("id") int id);
    void changeHoldingStatus(UpdateHoldingRequestStatus request);
    List<com.bookinator.api.model.dto.HoldingRequest> getFreshRequests(@Param("userId") int userId);
    List<com.bookinator.api.model.dto.HoldingRequest> getApprovedRequests(@Param("userId") int userId);
}
