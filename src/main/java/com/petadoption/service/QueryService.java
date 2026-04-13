package com.petadoption.service;

import com.petadoption.dto.QueryDTO;
import com.petadoption.model.Query;

public interface QueryService {
    Query askQuery(QueryDTO queryDTO);
    Query respondQuery(Long questionId, String answer);
    java.util.List<Query> getAllQueries();
}
