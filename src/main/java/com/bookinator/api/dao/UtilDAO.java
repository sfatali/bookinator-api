package com.bookinator.api.dao;

import com.bookinator.api.model.Util;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Sabina on 5/1/2018.
 */
@Mapper
public interface UtilDAO {
    /**
     * List of supported countries
     * @return countries
     */
    List<Util> getCountries();

    /**
     * List of supported cities
     * @return cities
     */
    List<Util> getCities();

    /**
     * List of book domains
     * @return fields
     */
    List<Util> getFields();

    /**
     * List of book statuses
     * @return statuses
     */
    List<Util> getBookStatusTypes();

    /**
     * List of holding types
     * @return holding types
     */
    List<Util> getBookHoldingTypes();
}
