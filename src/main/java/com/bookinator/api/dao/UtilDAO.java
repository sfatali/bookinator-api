package com.bookinator.api.dao;

import com.bookinator.api.model.Util;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Sabina on 5/1/2018.
 */
@Mapper
public interface UtilDAO {
    List<Util> getCountries();
    List<Util> getCities();
    List<Util> getFields();
    List<Util> getBookStatusTypes();
    List<Util> getBookHoldingTypes();
}
